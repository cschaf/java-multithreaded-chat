package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * Client connects to Server and prints all the messages
 * received from the server. It also allows the user to send messages to the
 * server. Client thread reads messages and print them to the standard
 * output. Sender thread reads messages from the standard input and sends them
 * to the server.
 */
public class Client implements IDisposable {
    private String serverIp = "localhost";
    private int serverPort = 1337;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private Socket socket = null;
    private Sender sender = null;
    private Listener listener = null;

    public Client(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    /**
     * Connect to Server
     */
    public void connect() {
        try {
            socket = new Socket(serverIp, serverPort);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to server " + serverIp + ":" + serverPort);
        } catch (IOException e) {
            this.dispose();
            System.err.println("Can not establish connection to " + serverIp + ":" + serverPort);
            System.exit(-1);
        }

        // Create and start Sender thread
        this.sender = new Sender(socket, out);
        this.sender.setDaemon(true);
        this.sender.start();

        this.listener = new Listener(in);
        this.listener.start();
    }

    public void disconnect() {
        this.dispose();
    }

    @Override
    public void dispose() {
        try {
            this.listener.dispose();
            this.sender.dispose();
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
