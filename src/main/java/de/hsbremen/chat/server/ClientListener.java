package de.hsbremen.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 */
public class ClientListener extends Thread {
    private ServerDispatcher serverDispatcher;
    private ClientHandler clientHandler;
    private BufferedReader in;

    public ClientListener(ClientHandler clientHandler, ServerDispatcher serverDispatcher) throws IOException {
        this.clientHandler = clientHandler;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientHandler.getSocket();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run() {
        try {
            while (!isInterrupted()) {
                String message = this.in.readLine();
                if (message == null) {
                    break;
                }
                this.serverDispatcher.dispatchMessage(this.clientHandler, message);
            }
        } catch (IOException e) {
            // Problem reading from socket (communication is broken)
        }

        // Communication is broken. Interrupt both listener and sender threads
        this.clientHandler.getClientSender().interrupt();
        this.serverDispatcher.deleteClient(this.clientHandler);
    }
}
