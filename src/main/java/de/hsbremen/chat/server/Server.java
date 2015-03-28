package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * Server is multithreaded chat server. It accepts multiple clients
 * simultaneously and serves them. Clients can send messages to the server.
 * When some client send a message to the server, this message is dispatched
 * to all the clients connected to the server.
 */
public class Server implements IDisposable{
    public static final int LISTENING_PORT = 1337;

    public static void main(String[] args) throws IOException {
        // Open server socket for listening
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(LISTENING_PORT);
            System.out.println("Server started on port " + LISTENING_PORT);
        } catch (IOException e) {
            System.err.println("Can not start listening on port " + LISTENING_PORT);
            e.printStackTrace();
            System.exit(1);
        }
        // Start ServerDispatcher thread
        ServerDispatcher serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();
        // Accept and handle client connections
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client has benn connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                ClientListener clientListener = new ClientListener(clientHandler, serverDispatcher);
                ClientSender clientSender = new ClientSender(clientHandler, serverDispatcher);
                clientHandler.setClientListener(clientListener);
                clientHandler.setClientSender(clientSender);
                clientListener.start();
                clientSender.start();
                serverDispatcher.addClient(clientHandler);
            } catch (IOException ioe) {
                serverDispatcher.dispose();
                serverSocket.close();
                ioe.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
    }
}
