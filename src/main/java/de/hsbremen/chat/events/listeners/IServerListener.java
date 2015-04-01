package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.util.EventListener;

/**
 * Created by cschaf on 01.04.2015.
 */
public interface IServerListener extends EventListener, IErrorListener{
    void onInfo(EventArgs<ServerMessage> eventArgs);
}
