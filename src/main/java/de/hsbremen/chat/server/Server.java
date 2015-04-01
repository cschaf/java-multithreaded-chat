package de.hsbremen.chat.server;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IClientConnectionListener;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IServerListener;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by cschaf on 28.03.2015.
 * Server is multithreaded chat server. It accepts multiple clients
 * simultaneously and serves them. Clients can send messages to the server.
 * When some client send a message to the server, this message is dispatched
 * to all the clients connected to the server.
 */
public class Server implements IDisposable {
    public int port;
    protected EventListenerList listeners = new EventListenerList();
    private ServerSocket serverSocket = null;
    private ServerDispatcher serverDispatcher = null;
    private ClientAccepter clientAccepter = null;
    private ArrayList<IClientConnectionListener> tempClientConnectionListeners;
    private ArrayList<IClientObjectReceivedListener> tempClientObjectReceivedListeners;

    public Server(int port) {
        this.tempClientObjectReceivedListeners = new ArrayList<IClientObjectReceivedListener>();
        this.tempClientConnectionListeners = new ArrayList<IClientConnectionListener>();
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            printInfo(new EventArgs<ServerMessage>(this, new ServerMessage("Server started on port " + port)));
        } catch (IOException e) {
            errorHasOccurred(new EventArgs<ServerMessage>(this, new ServerMessage("Can not start listening on port " + port, MessageType.Error)));
            System.exit(1);
        }
        // Start ServerDispatcher thread
        this.serverDispatcher = new ServerDispatcher();
        for (IClientConnectionListener listener : this.tempClientConnectionListeners) {
            this.serverDispatcher.addClientConnectionListener(listener);
        }
        for (IClientObjectReceivedListener listener : this.tempClientObjectReceivedListeners) {
            this.serverDispatcher.addClientObjectReceivedListener(listener);
        }
        this.tempClientConnectionListeners.clear();
        this.serverDispatcher.start();
        // Accept and handle client connections
        this.clientAccepter = new ClientAccepter(serverSocket, serverDispatcher);
        this.clientAccepter.start();
    }

    public void stop() {
        this.dispose();
    }

    public void addClientObjectReceivedListener(IClientObjectReceivedListener listener) {
        if (this.serverDispatcher == null) {
            tempClientObjectReceivedListeners.add(listener);
        } else {
            this.serverDispatcher.addClientObjectReceivedListener(listener);
        }
    }

    public void removeClientObjectReceivedListener(IClientObjectReceivedListener listener) {
        this.listeners.remove(IClientObjectReceivedListener.class, listener);
    }

    public void addClientConnectionListener(IClientConnectionListener listener) {
        if (this.serverDispatcher == null) {
            tempClientConnectionListeners.add(listener);
        } else {
            this.serverDispatcher.addClientConnectionListener(listener);
        }
    }

    public void removeClientConnectionListener(IClientConnectionListener listener) {
        this.serverDispatcher.removeClientConnectionListener(listener);
    }

    public void addServerListener(IServerListener listener) {
        this.listeners.add(IServerListener.class, listener);
    }

    public void removeServerListener(IServerListener listener) {
        this.listeners.remove(IServerListener.class, listener);
    }

    private void errorHasOccurred(EventArgs<ServerMessage> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == IServerListener.class) {
                ((IServerListener) listeners[i + 1]).onError(eventArgs);
            }
        }
    }

    private void printInfo(EventArgs<ServerMessage> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == IServerListener.class) {
                ((IServerListener) listeners[i + 1]).onInfo(eventArgs);
            }
        }
    }

    @Override
    public void dispose() {
        try {
            this.clientAccepter.dispose();
            this.serverDispatcher.dispose();
            this.serverSocket.close();
        } catch (IOException e) {
            errorHasOccurred(new EventArgs<ServerMessage>(this, new ServerMessage("Can not dispose Server", MessageType.Error)));
        }
    }
}
