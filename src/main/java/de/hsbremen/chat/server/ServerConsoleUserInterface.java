package de.hsbremen.chat.server;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.ClientConnectionConsoleListener;
import de.hsbremen.chat.events.listeners.IClientObjectReceivedListener;
import de.hsbremen.chat.events.listeners.IServerListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerConsoleUserInterface {
    public static void main(String[] args) {
        Server server = new Server(1337);

        server.addServerListener(new IServerListener() {
            @Override
            public void onInfo(EventArgs<ServerMessage> eventArgs) {
                System.out.println(eventArgs.getItem());
            }

            @Override
            public void onError(EventArgs<ServerMessage> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });

        server.addClientObjectReceivedListener(new IClientObjectReceivedListener() {
            @Override
            public void onObjectReceived(EventArgs<ITransferable> eventArgs) {
                switch (eventArgs.getItem().getType()){
                    case Messgage:
                        System.out.println((Message)eventArgs.getItem());
                        break;
                }
            }
        });
        ClientConnectionConsoleListener consoleListener = new ClientConnectionConsoleListener();
        server.addClientConnectionListener(consoleListener);

        server.start();


    }
}
