package de.hsbremen.chat.network.transferableObjects;

import de.hsbremen.chat.network.TransferableType;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientInfo extends TransferableObject {

    private String username = null;
    private String message = null;
    private String ip = null;
    private int port = -1;
    private ClientInfoSendingReason reason;



    public ClientInfo(String username, String ip, int port) {
        this.username = username;
        this.ip = ip;
        this.port = port;
        this.reason = ClientInfoSendingReason.Info;
    }

    public ClientInfo(String username, String ip, int port, ClientInfoSendingReason reason) {
        this(username, ip, port);
        this.reason = reason;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMessage() {
        return this.message;
    }

    public TransferableType getType() {
        return TransferableType.ClientInfo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ClientInfoSendingReason getReason() {
        return reason;
    }
}
