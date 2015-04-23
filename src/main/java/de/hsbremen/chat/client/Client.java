package de.hsbremen.chat.client;

import de.hsbremen.chat.core.ErrorHandler;
import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.*;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.TransferableObjectFactory;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
    private String username = "Guest";
    private ErrorHandler errorHandler = null;
    protected EventListenerList listeners = new EventListenerList();
    private ArrayList<IServerObjectReceivedListener> tempServerObjectReceivedListeners;

    public Client(String serverIp, int serverPort, String username) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.username = username;
        this.errorHandler = new ErrorHandler();
        this.tempServerObjectReceivedListeners = new ArrayList<IServerObjectReceivedListener>();
    }
    public Client() {
        this.errorHandler = new ErrorHandler();
        this.tempServerObjectReceivedListeners = new ArrayList<IServerObjectReceivedListener>();
    }

    public void addErrorListener(IErrorListener listener) {
        this.errorHandler.addErrorListender(listener);
    }

    public void removeErrorListener(IErrorListener listener) {
        this.errorHandler.removeErrorListener(listener);
    }

    public void addServerObjectReceivedListener(IServerObjectReceivedListener listener) {
        if (this.listener == null) {
            tempServerObjectReceivedListeners.add(listener);
        } else {
            this.listener.addServerObjectReceivedListener(listener);
        }
    }

    public void removeServerObjectReceivedListener(IServerObjectReceivedListener listener) {
        this.listener.removeServerObjectReceivedListener(listener);
    }
    /**
     * Connect to Server
     */
    public void connect() throws Exception {
        try {
            socket = new Socket(serverIp, serverPort);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            errorHandler.errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("Can not establish connection to " + serverIp + ":" + serverPort, MessageType.Error)));
            throw new Exception("Can not establish connection to " + serverIp + ":" + serverPort);
        }

        // Create and start Sender thread
        this.sender = new Sender(socket, out);
        this.sender.sendLogin(this.username);
        this.sender.start();

        this.listener = new Listener(in, this.errorHandler);
        for (IServerObjectReceivedListener tempListener : this.tempServerObjectReceivedListeners) {
            this.listener.addServerObjectReceivedListener(tempListener);
        }
        this.tempServerObjectReceivedListeners.clear();
        this.listener.start();
    }

    public void disconnect() {
        this.dispose();
    }

    public void sendMessage(String message){
        this.sender.sendMessage(this.username, message);
    }

    public void dispose() {
        try {
            if (this.listener != null) {
                this.listener.dispose();
            }
            if (this.sender != null) {
                this.sender.dispose();
            }
            if (this.out != null) {
                this.out.close();
            }
            if (this.in != null) {
                this.in.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            errorHandler.errorHasOccurred(new EventArgs<ITransferable>(this, TransferableObjectFactory.CreateServerMessage("Could not dispose clientobject", MessageType.Error)));
        }
    }

    public void setIp(String ip) {
        this.serverIp = ip;
    }

    public void setPort(int port) {
        this.serverPort = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
