package de.hsbremen.chat.server;

import de.hsbremen.chat.events.listeners.ClientConnectionConsoleListener;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerConsoleUserInterface {
    public static void main(String[] args) {
        Server server = new Server(1337);
        server.start();
        ClientConnectionConsoleListener consoleListener = new ClientConnectionConsoleListener();
        server.addClientConnectionListener(consoleListener);
    }
}
