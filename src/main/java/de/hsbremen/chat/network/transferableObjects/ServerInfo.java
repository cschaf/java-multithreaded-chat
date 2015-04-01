package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ServerInfo extends TransferableObject {
    @Override
    public TransferableType getType() {
        return TransferableType.ServerInfo;
    }
}
