package de.hsbremen.chat.core;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.events.listeners.IErrorListener;
import de.hsbremen.chat.network.ITransferable;

import javax.swing.event.EventListenerList;

/**
 * Created by cschaf on 02.04.2015.
 */
public class ErrorHandler {
    protected EventListenerList listeners = new EventListenerList();

    public void addErrorListender(IErrorListener listener) {
        this.listeners.add(IErrorListener.class, listener);
    }

    public void removeErrorListener(IErrorListener listener) {
        this.listeners.remove(IErrorListener.class, listener);
    }

    public void errorHasOccurred(EventArgs<ITransferable> eventArgs) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] ==IErrorListener.class){
                ((IErrorListener) listeners[i + 1]).onError(eventArgs);
            }
        }
    }
}
