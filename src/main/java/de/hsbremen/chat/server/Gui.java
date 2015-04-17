package de.hsbremen.chat.server;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cschaf on 31.03.2015.
 */
public class Gui {
    private JLabel lblPort;
    private JTextField tbxPort;
    private JLabel lblIp;
    private JTextField tbxIp;
    private JButton btnStart;
    private JButton btnStop;
    private JTextField tbxMessage;
    private JButton btnSend;
    private JScrollPane scrollPanelUsers;
    private JList listUsers;
    private JPanel pnlMain;
    private JTextArea traMessages;
    private JScrollPane scrollPanelMessages;
    public Gui(){
        this._initComponents();
        this._addComponents();
    }

    private void _addComponents(){
// row 1
        pnlMain.add(lblIp, "left, split 2");
        pnlMain.add(tbxIp, "pushx, growx");
        pnlMain.add(lblPort, "left, split 2");
        pnlMain.add(tbxPort, "sg navBtn");
        pnlMain.add(btnStart, "sg navBtn");
        pnlMain.add(btnStop, "sg navBtn, wrap");

// row 2
        pnlMain.add(scrollPanelMessages, "push, grow, span 6, split");
        pnlMain.add(scrollPanelUsers, "wrap, pushy, growy");
// row 3
        pnlMain.add(tbxMessage, "pushx, growx, split, span 6");
        pnlMain.add(btnSend);

    }

    private void _initComponents(){
        pnlMain = new JPanel(new MigLayout("debug"));
        lblIp = new JLabel("IP:");
        tbxIp = new JTextField("127.0.0.1");
        lblPort = new JLabel("Port:");
        tbxPort = new JTextField("1337");
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        traMessages = new JTextArea(10,10);
        scrollPanelMessages = new JScrollPane(traMessages);
        listUsers = new JList();
        scrollPanelUsers = new JScrollPane(listUsers);
        tbxMessage = new JTextField("");
        btnSend = new JButton("Send");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server Gui");
        frame.setContentPane(new Gui().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d= new Dimension(637, 500);
        frame.setSize(d);
        frame.setPreferredSize(d);
        frame.pack();
        frame.setVisible(true);
    }
}
