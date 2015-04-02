package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientInfo extends TransferableObject {

    private String username = null;
    private String message = null;

    public ClientInfo(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return this.username;
    }
    public String getMessage() {
        return this.message;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.ClientInfo;
    }
}
