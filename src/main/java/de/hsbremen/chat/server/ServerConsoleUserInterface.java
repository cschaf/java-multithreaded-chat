package de.hsbremen.chat.server;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.ClientConnectionConsoleListener;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IServerListener;
import de.hsbremen.chat.network.ITransferable;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerConsoleUserInterface {
    public static void main(String[] args) {
        Server server = new Server(1337);

        server.addServerListener(new IServerListener() {
            @Override
            public void onInfo(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }

            @Override
            public void onError(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });

        server.addClientObjectReceivedListener(new IClientObjectReceivedListener() {
            @Override
            public void onObjectReceived(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });
        ClientConnectionConsoleListener consoleListener = new ClientConnectionConsoleListener();
        server.addClientConnectionListener(consoleListener);

        server.start();
    }
}
