package de.hsbremen.chat.client;

import de.hsbremen.chat.core.IDisposable;
import de.hsbremen.chat.network.ITransferable;
import de.hsbremen.chat.network.transferableObjects.ClientInfo;
import de.hsbremen.chat.network.transferableObjects.Message;
import de.hsbremen.chat.network.transferableObjects.ServerInfo;
import de.hsbremen.chat.network.transferableObjects.ServerMessage;
import de.hsbremen.chat.server.Server;

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
                    case ServerMessage:
                        ServerMessage serverMessage = (ServerMessage) receivedObj;
                        System.out.println(serverMessage);
                        break;
                    case ServerInfo:
                        ServerInfo serverInfo = (ServerInfo) receivedObj;
                        if (serverInfo.getUsers().size() >0){
                            System.out.println("--- connected users ---");
                            for (String user : serverInfo.getUsers()){
                                System.out.println(user);
                            }
                            System.out.println("--- end ---");
                        }
                        break;
                    case ClientInfo:
                        ClientInfo clientInfo = (ClientInfo) receivedObj;
                        switch (clientInfo.getReason()){
                            case Connect:
                                System.out.println("--- New User for user list");
                                System.out.println(clientInfo.getUsername() + "(" + clientInfo.getPort() + ")");
                                System.out.println("--- end ---");
                                break;
                            case Disconnect:
                                System.out.println("--- Remove User from user list");
                                System.out.println(clientInfo.getUsername() + "(" + clientInfo.getPort() + ")");
                                System.out.println("--- end ---");
                                break;
                        }
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
