package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * ClientListener class is purposed to listen for client messages and
 * to forward them to ServerDispatcher.
 */
public class ClientListener extends Thread implements IDisposable{
    private ServerDispatcher serverDispatcher;
    private ClientHandler clientHandler;
    private BufferedReader in;
    private boolean disposed;

    public ClientListener(ClientHandler clientHandler, ServerDispatcher serverDispatcher) throws IOException {
        this.disposed = false;
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
            while (!isInterrupted() && !this.disposed) {
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

    @Override
    public void dispose() {
        try {
            this.disposed = true;
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
