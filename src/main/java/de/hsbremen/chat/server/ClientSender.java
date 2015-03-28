package de.hsbremen.chat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by cschaf on 28.03.2015.
 * Sends messages to the client. Messages are stored in a message queue. When
 * the queue is empty, ClientSender falls in sleep until a new message is
 * arrived in the queue. When the queue is not empty, ClientSender sends the
 * messages from the queue to the client socket.
 */
public class ClientSender extends Thread {
    private Vector messageQueue = new Vector();
    private ServerDispatcher serverDispatcher;
    private ClientHandler clientHandler;
    private PrintWriter out;

    public ClientSender(ClientHandler clientHandler, ServerDispatcher serverDispatcher) throws IOException {
        this.clientHandler = clientHandler;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientHandler.getSocket();
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(String aMessage) {
        this.messageQueue.add(aMessage);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized String getNextMessageFromQueue() throws InterruptedException {
        while (this.messageQueue.size() == 0) {
            wait();
        }
        String message = (String) this.messageQueue.get(0);
        this.messageQueue.removeElementAt(0);
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(String aMessage) {
        this.out.println(aMessage);
        this.out.flush();
    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    public void run() {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
        } catch (Exception e) {
            // Commuication problem
        }

        // Communication is broken. Interrupt both listener and sender threads
        this.clientHandler.getClientListener().interrupt();
        this.serverDispatcher.deleteClient(this.clientHandler);
    }
}