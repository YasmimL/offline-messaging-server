package br.com.ifce.network.rmi;

import br.com.ifce.model.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface OfflineMessagingService extends Remote {

    void createQueue(String queueName) throws RemoteException;

    void send(ChatMessage message) throws RemoteException;

    List<ChatMessage> readAll(String username) throws RemoteException;
}
