package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.ClientInfoSendingReason;
import de.hsbremen.chat.network.transferableObjects.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * Handles user inputs for stdin
 */
public class Sender extends Thread implements IDisposable {
    private ObjectOutputStream out;
    private Socket socket;
    private boolean disposed;

    public Sender(Socket socket, ObjectOutputStream out) {
        this.disposed = false;
        this.socket = socket;
        this.out = out;
        this.setName("Client-Senderthread");
    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run() {
        try {
            while (!isInterrupted() && !this.disposed) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String username, String message){
        ITransferable sender = TransferableObjectFactory.CreateClientInfo(username, socket.getInetAddress().getHostAddress(), socket.getLocalPort());
        try {
            this.out.writeObject(TransferableObjectFactory.CreateMessage(message, sender));
            this.out.flush();
        } catch (IOException e) {
            this.dispose();
        }

    }
    public void sendLogin(String username) {
        try {
            this.out.writeObject(TransferableObjectFactory.CreateClientInfo(username,socket.getInetAddress().getHostAddress(), socket.getLocalPort(), ClientInfoSendingReason.Connect));
            this.out.flush();
        } catch (IOException e) {
            this.dispose();
        }

    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
