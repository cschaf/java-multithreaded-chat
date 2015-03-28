package de.hsbremen.chat.server;

import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * ClientHandler class contains information about a client, connected to the server.
 */
public class ClientHandler {
    private Socket socket = null;
    private ClientSender clientSender = null;
    private ClientListener clientListener = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientSender getClientSender() {

        return clientSender;
    }

    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public ClientListener getClientListener() {
        return clientListener;
    }

    public void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
    }
}
