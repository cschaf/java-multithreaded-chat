package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

import java.util.ArrayList;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerInfo extends TransferableObject {

    private ArrayList<String> users;

    public ServerInfo(ArrayList<String> users) {
        this.users = users;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.ServerInfo;
    }

    public ArrayList<String> getUsers() {
        return users;
    }
}
