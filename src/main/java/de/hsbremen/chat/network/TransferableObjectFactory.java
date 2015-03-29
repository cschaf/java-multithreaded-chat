package de.hsbremen.chat.network;

import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.TransferableObject;

import java.net.Socket;

/**
 * Created by cschaf on 29.03.2015.
 */
public class TransferableObjectFactory {
    public static TransferableObject CreateMessage(Socket socket, String message){
        return new Message(socket.getInetAddress().getHostAddress(), socket.getLocalPort(),System.getProperty("user.name"), message);
    }
}
