package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

import java.util.EventListener;

/**
 * Created by cschaf on 01.04.2015.
 */
public interface IServerObjectReceivedListener extends EventListener{
    void onObjectReceived(EventArgs<ITransferable> eventArgs);
    void onMessageObjectReceived(EventArgs<Message> eventArgs);
    void onClientInfoObjectReceived(EventArgs<ClientInfo> eventArgs);
    void onServerMessageObjectReceived(EventArgs<ServerMessage> eventArgs);
    void onServerInfoObjectReceived(EventArgs<ServerInfo> eventArgs);
}
