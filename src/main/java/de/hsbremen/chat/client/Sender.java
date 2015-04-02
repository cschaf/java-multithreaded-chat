package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
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
    private String username;

    public Sender(Socket socket, ObjectOutputStream out, String username) {
        this.username = username;
        this.disposed = false;
        this.socket = socket;
        this.out = out;
    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            this.out.writeObject(TransferableObjectFactory.CreateClientInfo(this.username, "set username"));
            this.out.flush();
            while (!isInterrupted() && !this.disposed) {
                String message = in.readLine();
                if(message != null){
                    this.out.writeObject(TransferableObjectFactory.CreateMessage(socket, this.username, message));
                    this.out.flush();
                }
            }
        } catch (IOException ioe) {
            this.dispose();
            // Communication is broken
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
