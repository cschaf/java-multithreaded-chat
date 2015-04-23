package de.hsbremen.chat.client;

import de.hsbremen.chat.core.ClientJListItem;
import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;

import javax.swing.*;

/**
 * Created by cschaf on 17.04.2015.
 */
public class ServerObjectListener implements IServerObjectReceivedListener{
    private Gui gui;
    private DefaultListModel<ClientJListItem>  model;

    public ServerObjectListener(Gui gui){

        this.gui = gui;
        this.model = new DefaultListModel<ClientJListItem>();
    }

    public void onObjectReceived(EventArgs<ITransferable> eventArgs) {

    }

    public void onMessageObjectReceived(EventArgs<Message> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString() + "\n");
    }

    public void onClientInfoObjectReceived(EventArgs<ClientInfo> eventArgs) {
        ClientJListItem item = new ClientJListItem(eventArgs.getItem().getIp(), eventArgs.getItem().getPort(), eventArgs.getItem().getUsername(), null);
        switch (eventArgs.getItem().getReason()){
            case Connect:
                this.model.addElement(item);
                gui.traMessages.append(eventArgs.getItem().getUsername() + "(" + eventArgs.getItem().getPort() + ") has joined" + "\n");
                break;
            case Disconnect:
                for (int i =0; i < this.model.getSize(); i++){
                    String ip = this.model.getElementAt(i).getIp();
                    int port = this.model.getElementAt(i).getPort();
                    if(ip.equals(item.getIp()) && port == item.getPort()){
                        this.model.remove(i);
                    }
                }
                gui.traMessages.append(eventArgs.getItem().getUsername() + "(" + eventArgs.getItem().getPort() + ") has left \n");
                break;
        }
    }

    public void onServerMessageObjectReceived(EventArgs<ServerMessage> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString()+ "\n");
    }

    public void onServerInfoObjectReceived(EventArgs<ServerInfo> eventArgs) {
        this.model.clear();
        for (ClientJListItem user : eventArgs.getItem().getUsers()){
            model.addElement(user);
        }
        gui.listUsers.setModel(model);
    }
}
