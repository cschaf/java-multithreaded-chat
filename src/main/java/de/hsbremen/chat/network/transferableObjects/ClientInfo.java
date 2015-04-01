package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientInfo extends TransferableObject {

    private String username;

    public ClientInfo(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.ClientInfo;
    }
}
