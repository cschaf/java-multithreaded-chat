package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.io.*;
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
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 1337;

    public static void main(String[] args) throws IOException {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Socket socket = null;
        try {
            // Connect to Server
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to server " + SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Can not establish connection to " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            System.exit(-1);
        }

        // Create and start Sender thread
        Sender sender = new Sender(socket, out);
        sender.setDaemon(true);
        sender.start();

        try {
            // Read messages from the server and print them
            ITransferable receivedObj;
            while ((receivedObj = (ITransferable) in.readObject()) != null) {
                switch (receivedObj.getType()){
                    case Messgage:
                        Message message = (Message)receivedObj;
                        System.out.println(message);
                        break;
                }
            }
        } catch (IOException e) {
            out.close();
            in.close();
            socket.close();
            System.err.println("Connection to server has been broken.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {

    }
}
