package de.hsbremen.chat.server;

import javax.swing.*;

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(new Gui().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
