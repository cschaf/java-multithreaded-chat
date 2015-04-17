package de.hsbremen.chat.client;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

/**
 * Created by cschaf on 17.04.2015.
 */
public class ServerObjectListener implements IServerObjectReceivedListener{
    private Gui gui;

    public ServerObjectListener(Gui gui){

        this.gui = gui;
    }

    @Override
    public void onObjectReceived(EventArgs<ITransferable> eventArgs) {

    }

    @Override
    public void onMessageObjectReceived(EventArgs<Message> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString() + "\n");
    }

    @Override
    public void onClientInfoObjectReceived(EventArgs<ClientInfo> eventArgs) {

    }

    @Override
    public void onServerMessageObjectReceived(EventArgs<ServerMessage> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString()+ "\n");
    }

    @Override
    public void onServerInfoObjectReceived(EventArgs<ServerInfo> eventArgs) {

    }
}
