package de.hsbremen.chat.server;

import de.hsbremen.chat.core.ErrorHandler;
import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IClientConnectionListener;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.events.listeners.IServerListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
import de.hsbremen.chat.network.transferableObjects.ClientInfoSendingReason;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by cschaf on 28.03.2015.
 * ServerDispatcher class is purposed to listen for messages received
 * from clients and to dispatch them to all the clients connected to the
 * chat server.
 */
public class ServerDispatcher extends Thread implements IDisposable {
    protected EventListenerList listeners = new EventListenerList();
    private Vector<ClientHandler> clients;
    private Vector<ITransferable> messageQueue;
    private boolean disposed;
    private ErrorHandler errorHandler;

    public ServerDispatcher(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.disposed = false;
        this.clients = new Vector<ClientHandler>();
        this.messageQueue = new Vector<ITransferable>();
    }

    public ArrayList<String> getUsers(){
        ArrayList<String> result = new ArrayList<String>();
        for(ClientHandler handler : this.clients){
            result.add(handler.getUsername() + "(" + handler.getSocket().getPort() + ")");
        }

        return result;
    }

    public ErrorHandler getErrorHandler(){
        return this.errorHandler;
    }

    /**
     * Adds given client to the server's client list.
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        this.clients.add(clientHandler);
        ITransferable serverMessage = TransferableObjectFactory.CreateServerMessage(clientHandler.getSocket().getInetAddress().getHostAddress() + ":" + clientHandler.getSocket().getPort() + " has connected", MessageType.Info);
        clientHasConnected(new EventArgs<ITransferable>(this, serverMessage));
    }

    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void deleteClient(ClientHandler clientHandler) {
        int clientIndex = this.clients.indexOf(clientHandler);
        if (clientIndex != -1) {
            this.clients.removeElementAt(clientIndex);
            ITransferable serverMessage = TransferableObjectFactory.CreateServerMessage(clientHandler.getSocket().getInetAddress().getHostAddress() + ":" + clientHandler.getSocket().getPort() + " (" + clientHandler.getUsername() + ")" + " has disconnected", MessageType.Info);
            clientHasDisconnected(new EventArgs<ITransferable>(this, serverMessage));
            // send to client the disconnected user info
            this.sendMessageToAllClients(TransferableObjectFactory.CreateClientInfo(clientHandler.getUsername(), clientHandler.getSocket().getInetAddress().getHostAddress(), clientHandler.getSocket().getPort(), ClientInfoSendingReason.Disconnect), clientHandler);
        }
    }

    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void dispatchMessage(ITransferable transferableObject) {
        this.messageQueue.add(transferableObject);
        objectReceived(new EventArgs<ITransferable>(this, transferableObject));
        notify();
    }


    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized ITransferable getNextMessageFromQueue() throws InterruptedException {
        while (this.messageQueue.size() == 0) {
            wait();
        }
        ITransferable object = this.messageQueue.get(0);
        this.messageQueue.removeElementAt(0);
        return object;
    }

    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    public synchronized void sendMessageToAllClients(ITransferable transferableObject, ClientHandler excludedClient) {
        for (int i = 0; i < this.clients.size(); i++) {
            ClientHandler clientHandler = this.clients.get(i);
            if (clientHandler != excludedClient){
                clientHandler.getClientSender().sendMessage(transferableObject);
            }
        }
    }

    public synchronized void send(ClientHandler client, ITransferable transferableObject) {
        for (int i = 0; i < this.clients.size(); i++) {
            ClientHandler clientHandler = this.clients.get(i);
            if (clientHandler == client){
                clientHandler.getClientSender().sendMessage(transferableObject);
                break;
            }

        }
    }

    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */
    public void run() {
        try {
            while (!this.disposed) {
                ITransferable object = this.getNextMessageFromQueue();
                this.sendMessageToAllClients(object, null);
            }
        } catch (InterruptedException ie) {
            this.errorHandler.errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("ServerDispatcher thread interrupted, stopped its execution", MessageType.Error)));
            this.dispose();
        }
    }

    public void addClientObjectReceivedListener(IClientObjectReceivedListener listener) {
        this.listeners.add(IClientObjectReceivedListener.class, listener);
    }

    public void removeClientObjectReceivedListener(IClientObjectReceivedListener listener) {
        this.listeners.remove(IClientObjectReceivedListener.class, listener);
    }


    public void addClientConnectionListener(IClientConnectionListener listener) {
        this.listeners.add(IClientConnectionListener.class, listener);
    }

    public void removeClientConnectionListener(IClientConnectionListener listener) {
        this.listeners.remove(IClientConnectionListener.class, listener);
    }

     private void clientHasConnected(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IClientConnectionListener.class) {
                ((IClientConnectionListener) listeners[i+1]).onClientHasConnected(eventArgs);
            }
        }
    }

    private void clientHasDisconnected(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IClientConnectionListener.class) {
                ((IClientConnectionListener) listeners[i+1]).onClientHasDisconnected(eventArgs);
            }
        }
    }

    private void objectReceived(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IClientObjectReceivedListener.class) {
                ((IClientObjectReceivedListener) listeners[i+1]).onObjectReceived(eventArgs);
            }
        }
    }

    public void clientHasSetName(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IClientConnectionListener.class) {
                ((IClientConnectionListener) listeners[i+1]).onClientHasSetName(eventArgs);
            }
        }
    }

    public void clientHasSignedIn(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IClientConnectionListener.class) {
                ((IClientConnectionListener) listeners[i+1]).onClientHasSignedIn(eventArgs);
            }
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
        for (ClientHandler clients : this.clients) {
            clients.dispose();
        }
    }
}
