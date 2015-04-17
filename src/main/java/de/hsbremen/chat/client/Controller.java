package de.hsbremen.chat.client;

import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.events.listeners.IServerObjectReceivedListener;
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
        this.errorListener = new ErrorListener(this.gui);
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
            @Override
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
            @Override
            public void actionPerformed(ActionEvent e) {
                client.disconnect();
                deregisterClientEvents();
                gui.changeNavbarEnableState(true);
                gui.traMessages.setText("");
            }
        });

        gui.btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage(gui.tbxMessage.getText());
                gui.tbxMessage.setText("");
            }
        });

        gui.tbxMessage.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    client.sendMessage(gui.tbxMessage.getText());
                    gui.tbxMessage.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }


}
