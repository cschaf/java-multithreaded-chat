package de.hsbremen.chat.core;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by cschaf on 23.04.2015.
 */
public class ClientJListItem implements Serializable {
    private String ip;
    private int port;
    private String name;
    private Image pic;

    public ClientJListItem(String ip, int port, String name, Image pic) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pic = pic;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.port +  ")";
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Image getPic() {
        return pic;
    }

    public String getName() {
        return this.name;
    }
}
