package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by cschaf on 28.03.2015.
 * Sends messages to the client. Messages are stored in a message queue. When
 * the queue is empty, ClientSender falls in sleep until a new message is
 * arrived in the queue. When the queue is not empty, ClientSender sends the
 * messages from the queue to the client socket.
 */
public class ClientSender extends Thread implements IDisposable {
    private Vector<ITransferable> messageQueue;
    private ServerDispatcher serverDispatcher;
    private ClientHandler clientHandler;
    private ObjectOutputStream out;
    private boolean disposed;

    public ClientSender(ClientHandler clientHandler, ServerDispatcher serverDispatcher) throws IOException {
        this.disposed = false;
        this.messageQueue = new Vector<ITransferable>();
        this.clientHandler = clientHandler;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientHandler.getSocket();
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(ITransferable transferableObject) {
        this.messageQueue.add(transferableObject);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized ITransferable getNextMessageFromQueue() throws InterruptedException {
        while (this.messageQueue.size() == 0) {
            wait();
        }
        ITransferable transferableObject = this.messageQueue.get(0);
        this.messageQueue.removeElementAt(0);
        return transferableObject;
    }

    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(ITransferable transferableObject) {
        try {
            this.out.writeObject(transferableObject);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    public void run() {
        try {
            while (!isInterrupted() && !this.disposed) {
                ITransferable message = this.getNextMessageFromQueue();
                this.sendMessageToClient(message);
            }
        } catch (Exception e) {
            this.dispose();
        }

        // Communication is broken. Interrupt both listener and sender threads
        this.clientHandler.getClientListener().interrupt();
        this.serverDispatcher.deleteClient(this.clientHandler);
    }

    @Override
    public void dispose() {
        this.disposed = true;
        try {
            this.out.close();
        } catch (IOException e) {
            this.serverDispatcher.getErrorHandler().errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("ClientSender outputstream could not been closed!", MessageType.Error)));
        }
    }
}
