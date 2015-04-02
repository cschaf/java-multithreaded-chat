package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by cschaf on 28.03.2015.
 * ClientListener class is purposed to listen for client messages and
 * to forward them to ServerDispatcher.
 */
public class ClientListener extends Thread implements IDisposable {
    private ServerDispatcher serverDispatcher;
    private ClientHandler clientHandler;
    private ObjectInputStream in;
    private boolean disposed;

    public ClientListener(ClientHandler clientHandler, ServerDispatcher serverDispatcher) throws IOException {
        this.disposed = false;
        this.clientHandler = clientHandler;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientHandler.getSocket();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run() {
        try {
            while (!isInterrupted() && !this.disposed) {
                ITransferable receivedObject = null;
                try {
                    receivedObject = (ITransferable) this.in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (receivedObject == null) {
                    break;
                }
                switch (receivedObject.getType()) {
                    case ClientInfo:
                        ClientInfo info = (ClientInfo) receivedObject;
                        if (clientHandler.getUsername() != info.getUsername()) {
                            clientHandler.setUsername(info.getUsername());
                        }
                        break;
                    default:
                        this.serverDispatcher.dispatchMessage(receivedObject);
                }

            }
        } catch (IOException e) {
            this.serverDispatcher.getErrorHandler().errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("Problem reading from socket (communication is broken)", MessageType.Error)));
            this.dispose();
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
