package de.hsbremen.chat.client;

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
public class Client {
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 1337;

    BufferedReader in = null;
    PrintWriter out = null;

    public static void main(String[] args) {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // Connect to Server
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Can not establish connection to " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
            e.printStackTrace();
            System.exit(-1);
        }

        // Create and start Sender thread
        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();

        try {
            // Read messages from the server and print them
            String message;
            while ((message=in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException ioe) {
            System.err.println("Connection to server broken.");
            ioe.printStackTrace();
        }
    }
}
