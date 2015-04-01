package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientConsoleUserInterface{
    public static void main(String[] args){
        Client client = new Client("localhost", 1337, "Shuffle");
        client.connect();
    }
}
