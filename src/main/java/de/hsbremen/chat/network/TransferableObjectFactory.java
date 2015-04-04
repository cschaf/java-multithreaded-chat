package de.hsbremen.chat.network;

import de.hsbremen.chat.network.transferableObjects.*;

import java.util.ArrayList;

/**
 * Created by cschaf on 29.03.2015.
 */
public class TransferableObjectFactory {
    public static ITransferable CreateMessage(String message, ITransferable sender) {
        return new Message(message, sender);
    }

    public static ITransferable CreateClientInfo(String username, String ip, int port) {
        return new ClientInfo(username, ip, port);
    }

    public static ITransferable CreateClientInfo(String username, String ip, int port, ClientInfoSendingReason reason) {
        return new ClientInfo(username, ip, port, reason);
    }

    public static ITransferable CreateServerMessage(String message, MessageType messageType) {
        return new ServerMessage(message, messageType);
    }
    public static ITransferable CreateServerInfo(ArrayList<String> users) {
        return new ServerInfo(users);
    }
}
