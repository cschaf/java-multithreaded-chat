package de.hsbremen.chat.client;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.network.ITransferable;

import javax.swing.*;

/**
 * Created by cschaf on 17.04.2015.
 */
public class ErrorListener implements IErrorListener{
    private Gui gui;
    private Controller ctrl;

    public ErrorListener(Gui gui, Controller ctrl){

        this.gui = gui;
        this.ctrl = ctrl;
    }
    public void onError(EventArgs<ITransferable> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString() + "\n");
        this.ctrl.logout();
        gui.changeNavbarEnableState(true);
        DefaultListModel<String> model = (DefaultListModel<String>) gui.listUsers.getModel();
        model.clear();
    }
}
