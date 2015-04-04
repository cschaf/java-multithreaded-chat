package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 28.03.2015.
 */
public class Message extends TransferableObject {
    private String message;
    private ClientInfo sender;

    public Message(String message, ITransferable sender) {
        this.message = message;
        this.sender = (ClientInfo) sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.Messgage;
    }

    @Override
    public String toString() {

        return this.sender.getUsername() + "(" + this.sender.getPort() + "): " + this.getMessage() + " - " + String.format("%1$TT", this.getCreatedAt());
    }
}
