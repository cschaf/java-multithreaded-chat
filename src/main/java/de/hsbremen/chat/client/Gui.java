package de.hsbremen.chat.client;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cschaf on 31.03.2015.
 */
public class Gui extends JFrame{
    public JPanel pnlMain;
    private JLabel lblUsername;
    public JTextField tbxUsername;
    private JLabel lblPort;
    public JTextField tbxPort;
    private JLabel lblIp;
    public JTextField tbxIp;
    public JButton btnConnect;
    public JButton btnDisconnect;
    private JScrollPane scrollPanelMessages;
    public JTextArea traMessages;
    public JTextField tbxMessage;
    public JButton btnSend;
    private JScrollPane scrollPanelUsers;
    public JList listUsers;


    public Gui(){
        super("Client Gui");
        this._initComponents();
        this._addComponents();

        setContentPane(this.pnlMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(637, 500);
        setPreferredSize(new Dimension(637, 500));
        pack();
        setVisible(true);
    }

    private void _addComponents(){
// row 1
        pnlMain.add(lblUsername, "left, split 2, span 4");
        pnlMain.add(tbxUsername, "pushx, growx");
        pnlMain.add(lblIp, "split");
        pnlMain.add(tbxIp, "pushx, growx");
        pnlMain.add(lblPort, "split");
        pnlMain.add(tbxPort, "pushx, growx");
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
        pnlMain = new JPanel(new MigLayout());
        lblUsername = new JLabel("Username:");
        tbxUsername = new JTextField("Guest");
        lblIp = new JLabel("IP:");
        tbxIp = new JTextField("localhost");
        lblPort = new JLabel("Port:");
        tbxPort = new JTextField("1337");
        btnConnect = new JButton("Connect");
        btnDisconnect = new JButton("Disconnect");
        traMessages = new JTextArea(10,10);
        traMessages.setEditable(false);
        //traMessages.setLineWrap(true);
        scrollPanelMessages = new JScrollPane(traMessages);
        listUsers = new JList();
        scrollPanelUsers = new JScrollPane(listUsers);
        tbxMessage = new JTextField("");
        btnSend = new JButton("Send");

        btnDisconnect.setEnabled(false);
        tbxMessage.setEnabled(false);
        btnSend.setEnabled(false);
    }

    public void changeNavbarEnableState(boolean state){
        tbxUsername.setEnabled(state);
        tbxIp.setEnabled(state);
        tbxPort.setEnabled(state);
        btnConnect.setEnabled(state);
        btnDisconnect.setEnabled(!state);
        btnSend.setEnabled(!state);
        tbxMessage.setEnabled(!state);
    }
}
