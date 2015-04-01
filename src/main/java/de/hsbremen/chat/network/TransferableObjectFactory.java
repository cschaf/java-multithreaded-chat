package de.hsbremen.chat.network;

import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.net.Socket;

/**
 * Created by cschaf on 29.03.2015.
 */
public class TransferableObjectFactory {
    public static TransferableObject CreateMessage(Socket socket, String username, String message) {
        return new Message(socket.getInetAddress().getHostAddress(), socket.getLocalPort(), username, message);
    }

    public static ITransferable CreateClientInfo(String username) {
        return new ClientInfo(username);
    }

    public static ITransferable CreateServerMessage(String message, MessageType messageType) {
        return new ServerMessage(message, messageType);
    }
}
