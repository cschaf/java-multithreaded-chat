package de.hsbremen.chat.client;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.network.ITransferable;

/**
 * Created by cschaf on 17.04.2015.
 */
public class ErrorListener implements IErrorListener{
    private Gui gui;

    public ErrorListener(Gui gui){

        this.gui = gui;
    }
    @Override
    public void onError(EventArgs<ITransferable> eventArgs) {
        gui.traMessages.append(eventArgs.getItem().toString()+ "\n");
    }
}
