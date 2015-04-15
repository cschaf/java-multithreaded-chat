package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientConsoleUserInterface{
    public static void main(String[] args){
        Client client = new Client("localhost", 1337, "Shuffle");
        client.addErrorListener(new IErrorListener() {
            @Override
            public void onError(EventArgs<ITransferable> eventArgs) {
                System.out.println(eventArgs.getItem());
            }
        });
        client.addServerObjectReceivedListener(new IServerObjectReceivedListener() {
            @Override
            public void onObjectReceived(EventArgs<ITransferable> eventArgs) {

            }

            @Override
            public void onMessageObjectReceived(EventArgs<Message> eventArgs) {
                System.out.println(eventArgs.getItem());
            }

            @Override
            public void onClientInfoObjectReceived(EventArgs<ClientInfo> eventArgs) {
                switch (eventArgs.getItem().getReason()){
                    case Connect:
                        System.out.println("--- New User for user list");
                        System.out.println(eventArgs.getItem().getUsername() + "(" + eventArgs.getItem().getPort() + ")");
                        System.out.println("--- end ---");
                        break;
                    case Disconnect:
                        System.out.println("--- Remove User from user list");
                        System.out.println(eventArgs.getItem().getUsername() + "(" + eventArgs.getItem().getPort() + ")");
                        System.out.println("--- end ---");
                        break;
                }
            }

            @Override
            public void onServerMessageObjectReceived(EventArgs<ServerMessage> eventArgs) {
                System.out.println(eventArgs.getItem());
            }

            @Override
            public void onServerInfoObjectReceived(EventArgs<ServerInfo> eventArgs) {
                System.out.println("--- connected users ---");
                for (String user : eventArgs.getItem().getUsers()){
                    System.out.println(user);
                }
                System.out.println("--- end ---");
            }
        });
        client.connect();
    }
}
