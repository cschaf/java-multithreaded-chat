package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by cschaf on 01.04.2015.
 */
public class Listener extends Thread implements IDisposable {
    private ObjectInputStream in = null;
    private boolean disposed;
    public Listener(ObjectInputStream in){
        this.disposed = false;
        this.in = in;
    }

    public void run(){
        try {
            // Read messages from the server and print them
            ITransferable receivedObj;
            while (!this.disposed && (receivedObj = (ITransferable) in.readObject()) != null) {
                switch (receivedObj.getType()){
                    case Messgage:
                        Message message = (Message)receivedObj;
                        System.out.println(message);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Connection to server has been broken.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }
}
