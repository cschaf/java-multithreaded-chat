package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.net.Socket;
import java.util.Vector;

/**
 * Created by cschaf on 28.03.2015.
 * ServerDispatcher class is purposed to listen for messages received
 * from clients and to dispatch them to all the clients connected to the
 * chat server.
 */
public class ServerDispatcher extends Thread implements IDisposable {
    private Vector<ClientHandler> clients;
    private Vector<ITransferable> messageQueue;
    private boolean disposed;

    public ServerDispatcher() {
        this.disposed = false;
        this.clients = new Vector<ClientHandler>();
        this.messageQueue = new Vector<ITransferable>();
    }

    /**
     * Adds given client to the server's client list.
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        this.clients.add(clientHandler);
    }

    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void deleteClient(ClientHandler clientHandler) {
        int clientIndex = this.clients.indexOf(clientHandler);
        if (clientIndex != -1) {
            this.clients.removeElementAt(clientIndex);
            System.out.println(clientHandler.getSocket().getInetAddress().getHostAddress() + ":" + clientHandler.getSocket().getPort() + " has disconnected");
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
    private synchronized void sendMessageToAllClients(ITransferable transferableObject) {
        for (int i = 0; i < this.clients.size(); i++) {
            ClientHandler clientHandler = this.clients.get(i);
            clientHandler.getClientSender().sendMessage(transferableObject);
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
                this.sendMessageToAllClients(object);
            }
        } catch (InterruptedException ie) {
            // Thread interrupted. Stop its execution
            this.dispose();
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
