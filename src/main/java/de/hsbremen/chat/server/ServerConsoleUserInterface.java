package de.hsbremen.chat.server;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.ClientConnectionConsoleListener;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.events.listeners.IServerListener;
import de.hsbremen.chat.network.ITransferable;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerConsoleUserInterface {
    public static void main(String[] args) {
        Server server = new Server(1337);
        server.addErrorListener(new IErrorListener() {
            public void onError(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });
        server.addServerListener(new IServerListener() {
            public void onInfo(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });

        server.addClientObjectReceivedListener(new IClientObjectReceivedListener() {
            public void onObjectReceived(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });
        ClientConnectionConsoleListener consoleListener = new ClientConnectionConsoleListener();
        server.addClientConnectionListener(consoleListener);

        server.start();
    }
}
