package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by cschaf on 28.03.2015.
 * Handles user inputs for stdin
 */
public class Sender extends Thread implements IDisposable {
    private PrintWriter out;
    private boolean disposed;

    public Sender(PrintWriter out) {
        this.disposed = false;
        this.out = out;
    }

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (!isInterrupted() && !this.disposed) {
                String message = in.readLine();
                if(message != null){
                    this.out.println(message);
                    this.out.flush();
                }
            }
        } catch (IOException ioe) {
            this.dispose();
            // Communication is broken
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
