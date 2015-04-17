package de.hsbremen.chat.client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cschaf on 17.04.2015.
 */
public class Main {
    public static void main(String[] args) {
        Gui gui = new Gui();
        Client  client = new Client();
        Controller controller = new Controller(client, gui);
    }
}
