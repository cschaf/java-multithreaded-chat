package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;

import java.util.EventListener;

/**
 * Created by cschaf on 01.04.2015.
 */
public interface IErrorListener extends EventListener{
    void onError(EventArgs<ITransferable> eventArgs);
}
