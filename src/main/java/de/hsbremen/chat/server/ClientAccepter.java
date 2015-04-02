package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientAccepter extends Thread implements IDisposable{
    ServerSocket serverSocket;
    ServerDispatcher serverDispatcher;
    private boolean disposed;

    public ClientAccepter(ServerSocket serverSocket, ServerDispatcher serverDispatcher){
        this.disposed = false;
        this.serverSocket = serverSocket;
        this.serverDispatcher = serverDispatcher;
    }

    /**
     * Until interrupted, accept clients
     */
    public void run() {
        while (!this.disposed) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                ClientSender clientSender = new ClientSender(clientHandler, serverDispatcher);
                ClientListener clientListener = new ClientListener(clientHandler, serverDispatcher);

                clientHandler.setClientListener(clientListener);
                clientHandler.setClientSender(clientSender);
                clientListener.start();
                clientSender.start();
                serverDispatcher.addClient(clientHandler);
            } catch (IOException e) {
                this.serverDispatcher.getErrorHandler().errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("Could not accepted client", MessageType.Error)));
            }
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
