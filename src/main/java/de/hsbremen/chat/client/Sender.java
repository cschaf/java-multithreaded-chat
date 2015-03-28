package de.hsbremen.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by cschaf on 28.03.2015.
 * Handles user inputs for stdin
 */
public class Sender extends Thread {
    private PrintWriter out;

    public Sender(PrintWriter out) {
        this.out = out;
    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run()
    {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (!isInterrupted()) {
                String message = in.readLine();
                this.out.println(message);
                this.out.flush();
            }
        } catch (IOException ioe) {
            // Communication is broken
        }
    }
}
