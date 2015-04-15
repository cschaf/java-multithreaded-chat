package de.hsbremen.chat.client;

import de.hsbremen.chat.core.ErrorHandler;
import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;
import de.hsbremen.chat.server.Server;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by cschaf on 01.04.2015.
 */
public class Listener extends Thread implements IDisposable {
    private final ErrorHandler errorHandler;
    protected EventListenerList listeners = new EventListenerList();
    private ObjectInputStream in = null;
    private boolean disposed;
    public Listener(ObjectInputStream in, ErrorHandler errorHandler){
        this.errorHandler = errorHandler;
        this.disposed = false;
        this.in = in;
    }

    public void run(){
        try {
            // Read messages from the server and print them
            ITransferable receivedObj;
            while (!this.disposed && (receivedObj = (ITransferable) in.readObject()) != null) {
                objectReceived(new EventArgs<ITransferable>(this, receivedObj));
                switch (receivedObj.getType()){
                    case Messgage:
                        Message message = (Message)receivedObj;
                        messageObjectReceived(new EventArgs<Message>(this, message));
                        break;
                    case ServerMessage:
                        ServerMessage serverMessage = (ServerMessage) receivedObj;
                        serverMessageObjectReceived(new EventArgs<ServerMessage>(this, serverMessage));
                        break;
                    case ServerInfo:
                        ServerInfo serverInfo = (ServerInfo) receivedObj;
                        if (serverInfo.getUsers().size() >0){
                            serverInfoObjectReceived(new EventArgs<ServerInfo>(this, serverInfo));
                        }
                        break;
                    case ClientInfo:
                        ClientInfo clientInfo = (ClientInfo) receivedObj;
                        clientInfoObjectReceived(new EventArgs<ClientInfo>(this, clientInfo));
                        break;
                }
            }
        } catch (IOException e) {
            errorHandler.errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("Connection to server has been broken.", MessageType.Error)));
        } catch (ClassNotFoundException e) {
            errorHandler.errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage(e.getMessage(), MessageType.Error)));
        }
    }

    public void addServerObjectReceivedListener(IServerObjectReceivedListener listener) {
        this.listeners.add(IServerObjectReceivedListener.class, listener);
    }

    public void removeServerObjectReceivedListener(IServerObjectReceivedListener listener) {
        this.listeners.remove(IServerObjectReceivedListener.class, listener);
    }

    private void objectReceived(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IServerObjectReceivedListener.class) {
                ((IServerObjectReceivedListener) listeners[i+1]).onObjectReceived(eventArgs);
            }
        }
    }
    private void messageObjectReceived(EventArgs<Message> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IServerObjectReceivedListener.class) {
                ((IServerObjectReceivedListener) listeners[i+1]).onMessageObjectReceived(eventArgs);
            }
        }
    }
    private void serverMessageObjectReceived(EventArgs<ServerMessage> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IServerObjectReceivedListener.class) {
                ((IServerObjectReceivedListener) listeners[i+1]).onServerMessageObjectReceived(eventArgs);
            }
        }
    }

    private void serverInfoObjectReceived(EventArgs<ServerInfo> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IServerObjectReceivedListener.class) {
                ((IServerObjectReceivedListener) listeners[i+1]).onServerInfoObjectReceived(eventArgs);
            }
        }
    }
    private void clientInfoObjectReceived(EventArgs<ClientInfo> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == IServerObjectReceivedListener.class) {
                ((IServerObjectReceivedListener) listeners[i+1]).onClientInfoObjectReceived(eventArgs);
            }
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
