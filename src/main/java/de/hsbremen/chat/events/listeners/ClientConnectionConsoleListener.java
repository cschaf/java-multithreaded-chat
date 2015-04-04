package de.hsbremen.chat.events.listeners;

import de.hsbremen.chat.events.EventArgs;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;

/**
 * Created by cschaf on 01.04.2015.
 */
public class ClientConnectionConsoleListener implements IClientConnectionListener {
    @Override
    public void onClientHasConnected(EventArgs<ITransferable> eventArgs) {
        System.out.println(eventArgs.getItem());
    }

    @Override
    public void onClientHasSetName(EventArgs<ITransferable> eventArgs) {
        System.out.println(eventArgs.getItem());
    }

    @Override
    public void onClientHasDisconnected(EventArgs<ITransferable> eventArgs) {
        System.out.println(eventArgs.getItem());
    }

    @Override
    public void onClientHasSignedIn(EventArgs<ITransferable> eventArgs) {
        ClientInfo info = (ClientInfo) eventArgs.getItem();
        System.out.println("--- New user for user list ---");
        System.out.println(info.getUsername() + " (" + info.getIp() + ":" + info.getPort() + ")");
        System.out.println("--- end ---");
    }
}
