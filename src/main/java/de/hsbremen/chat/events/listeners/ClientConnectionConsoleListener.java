package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientConnectionConsoleListener implements IClientConnectionListener {
    @Override
    public void onClientHasConnected(EventArgs<ServerMessage> eventArgs) {
        System.out.println(eventArgs.getItem());
    }

    @Override
    public void onClientHasDisconnected(EventArgs<ServerMessage> eventArgs) {
        System.out.println(eventArgs.getItem());
    }

    @Override
    public void onError(EventArgs<ServerMessage> eventArgs) {
        System.out.println(eventArgs.getItem());
    }
}
