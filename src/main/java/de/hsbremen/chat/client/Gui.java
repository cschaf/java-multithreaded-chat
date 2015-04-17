package de.hsbremen.chat.client;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cschaf on 31.03.2015.
 */
public class Gui {
    private JPanel pnlMain;
    private JLabel lblUsername;
    private JTextField tbxUsername;
    private JButton btnConnect;
    private JButton btnDisconnect;
    private JScrollPane scrollPanelMessages;
    private JTextArea traMessages;
    private JTextField tbxMessage;
    private JButton btnSend;
    private JScrollPane scrollPanelUsers;
    private JList listUsers;


    public Gui(){
        this._initComponents();
        this._addComponents();
    }

    private void _addComponents(){
// row 1
        pnlMain.add(lblUsername, "left, split 2, span 4");
        pnlMain.add(tbxUsername, "pushx, growx");
        pnlMain.add(btnConnect, "sg navBtn");
        pnlMain.add(btnDisconnect, "sg navBtn, wrap");
// row 2
        pnlMain.add(scrollPanelMessages, "push, grow, span 6, split");
        pnlMain.add(scrollPanelUsers, "wrap, pushy, growy");
// row 3
        pnlMain.add(tbxMessage, "pushx, growx, split, span 6");
        pnlMain.add(btnSend);

    }

    private void _initComponents(){
        pnlMain = new JPanel(new MigLayout("debug"));
        lblUsername = new JLabel("Username:");
        tbxUsername = new JTextField("");
        btnConnect = new JButton("Connect");
        btnDisconnect = new JButton("Disconnect");
        traMessages = new JTextArea(10,10);
        scrollPanelMessages = new JScrollPane(traMessages);
        listUsers = new JList();
        scrollPanelUsers = new JScrollPane(listUsers);
        tbxMessage = new JTextField("");
        btnSend = new JButton("Send");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Client Gui");
        frame.setContentPane(new Gui().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(637, 500);
        frame.setPreferredSize(new Dimension(637, 500));
        frame.pack();
        frame.setVisible(true);
    }
}
