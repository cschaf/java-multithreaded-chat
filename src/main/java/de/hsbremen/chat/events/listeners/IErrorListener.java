package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

/**
 * Created by cschaf on 01.04.2015.
 */
public interface IErrorListener {
    void onError(EventArgs<ServerMessage> eventArgs);
}
