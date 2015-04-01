package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cschaf on 28.03.2015.
 */
public abstract class TransferableObject implements Serializable, ITransferable {
    private Timestamp createdAt;

    protected TransferableObject() {
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "created at: " + this.getCreatedAt();
    }
}
