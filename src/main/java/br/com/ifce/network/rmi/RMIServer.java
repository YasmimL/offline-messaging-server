package br.com.ifce.network.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    private final Registry registry;

    private static final RMIServer INSTANCE = new RMIServer();

    public static RMIServer getInstance() {
        return INSTANCE;
    }

    private RMIServer() {
        try {
            this.registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            this.registry.rebind("offline-messaging-service", new OfflineMessagingServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
