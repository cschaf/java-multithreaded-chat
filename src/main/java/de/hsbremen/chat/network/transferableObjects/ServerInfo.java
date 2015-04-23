package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.core.ClientJListItem;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

import java.util.ArrayList;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerInfo extends TransferableObject {

    private ArrayList<ClientJListItem> users;

    public ServerInfo(ArrayList<ClientJListItem> users) {
        this.users = users;
    }

    public TransferableType getType() {
        return TransferableType.ServerInfo;
    }

    public ArrayList<ClientJListItem> getUsers() {
        return users;
    }
}
