package de.hsbremen.chat.client;

import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;

import javax.swing.*;
import javax.swing.text.StringContent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by cschaf on 17.04.2015.
 */
public class Controller {
    private Client client;
    private Gui gui;
    private IErrorListener errorListener;
    private IServerObjectReceivedListener serverObjectReceivedListener;

    public Controller(Client client, Gui gui) {

        this.client = client;
        this.gui = gui;
        this.registerGuiEvents();
    }

    private void registerClientEvents() {
        if (client == null) return;
        this.errorListener = new ErrorListener(this.gui, this);
        client.addErrorListener(this.errorListener);
        this.serverObjectReceivedListener = new ServerObjectListener(this.gui);
        client.addServerObjectReceivedListener(this.serverObjectReceivedListener);
    }

    private void deregisterClientEvents() {
        if (client == null) return;
        client.removeErrorListener(this.errorListener);
        client.removeServerObjectReceivedListener(this.serverObjectReceivedListener);
    }

    private void registerGuiEvents() {
        if (this.gui == null) return;

        gui.btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.setIp(gui.tbxIp.getText());
                client.setPort(Integer.parseInt(gui.tbxPort.getText()));
                client.setUsername(gui.tbxUsername.getText());
                try {
                    client.connect();
                    registerClientEvents();
                    gui.changeNavbarEnableState(false);
                } catch (Exception e1) {
                    gui.traMessages.append(e1.getMessage() + "\n");
                }

            }
        });

        gui.btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
                gui.changeNavbarEnableState(true);
                gui.traMessages.setText("");
                DefaultListModel<String> model = (DefaultListModel<String>) gui.listUsers.getModel();
                model.clear();
            }
        });

        gui.btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.sendMessage(gui.tbxMessage.getText());
                gui.tbxMessage.setText("");
            }
        });

        gui.tbxMessage.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {

            }


            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    client.sendMessage(gui.tbxMessage.getText());
                    gui.tbxMessage.setText("");
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void logout() {
        client.disconnect();
        deregisterClientEvents();
    }
}
