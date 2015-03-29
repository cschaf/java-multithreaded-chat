package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cschaf on 28.03.2015.
 */
public abstract class TransferableObject implements Serializable, ITransferable {
    private Timestamp createdAt;
    private String senderIp;
    private int port;

    protected TransferableObject(String senderIp, int port) {
        this.senderIp = senderIp;
        this.port = port;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "(" +this.getCreatedAt()+ ")" +  this.getSenderIp() + ":" + this.getPort();
    }
}
