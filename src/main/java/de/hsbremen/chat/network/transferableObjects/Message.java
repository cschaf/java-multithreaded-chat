package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.TransferableType;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by cschaf on 28.03.2015.
 */
public class Message extends TransferableObject {
    private String message;
    private String username;
    private String senderIp;
    private int port;

    public Message(String senderIp, int port, String username, String message) {
        this.setCreatedAt(new Timestamp(new Date().getTime()));
        this.senderIp = senderIp;
        this.port = port;
        this.message = message;
        this.username = username;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public TransferableType getType() {
        return TransferableType.Messgage;
    }

    @Override
    public String toString() {

        return  this.getUsername() + "(" + this.getPort() + "): " + this.getMessage() + " - " + String.format("%1$TT", this.getCreatedAt());
    }
}
