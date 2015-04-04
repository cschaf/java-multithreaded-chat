package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.MessageType;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerMessage extends TransferableObject {
    private String message;
    private MessageType type;

    public ServerMessage(String message) {
        this.message = message;
        type = MessageType.Info;
    }

    public ServerMessage(String message, MessageType type) {
        this(message);
        this.type = type;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.ServerMessage;
    }

    @Override
    public String toString() {
        String result;
        switch (this.type) {
            case Info:
                result = "Server: " + this.getMessage();
                break;
            case Warning:
                result = "!Server Waring!: " + this.getMessage();
                break;
            case Error:
                result = "!Server Error!: " + this.getMessage();
                break;
            default:
                result = this.getMessage();
        }
        result += " - " + String.format("%1$TT", this.getCreatedAt());
        return result;
    }

    public String getMessage() {
        return message;
    }
}
