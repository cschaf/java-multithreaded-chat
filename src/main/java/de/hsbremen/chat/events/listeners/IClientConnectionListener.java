package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

import java.util.EventListener;

/**
 * Created by cschaf on 30.03.2015.
 */
public interface IClientConnectionListener extends EventListener, IErrorListener {
    void onClientHasConnected(EventArgs<ServerMessage> eventArgs);

    void onClientHasDisconnected(EventArgs<ServerMessage> eventArgs);
}
