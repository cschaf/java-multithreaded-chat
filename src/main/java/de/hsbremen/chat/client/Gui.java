package de.hsbremen.chat.client;

import javax.swing.*;

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(new Gui().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
