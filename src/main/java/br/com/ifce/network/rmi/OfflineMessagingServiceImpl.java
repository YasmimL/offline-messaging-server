package br.com.ifce.network.rmi;

import br.com.ifce.model.ChatMessage;
import br.com.ifce.network.activemq.BrokerMediator;
import br.com.ifce.network.activemq.Consumer;
import br.com.ifce.network.activemq.Producer;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.List;

public class OfflineMessagingServiceImpl extends UnicastRemoteObject implements OfflineMessagingService {
    protected OfflineMessagingServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void createQueue(String queueName) {
        BrokerMediator mediator = BrokerMediator.getInstance();
        Connection connection = mediator.getConnection();
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.createQueue(queueName);
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ChatMessage message) {
        Producer.newInstance(message.to())
            .send(message.from() + ":" + message.to() + ":" + message.message())
            .close();
    }

    @Override
    public List<ChatMessage> readAll(String username) {
        final var messages = Consumer.newInstance(username)
            .readAll()
            .close();
        return messages.stream()
            .map(message -> {
                final var result = message.split(":", 3);
                return new ChatMessage(result[0], result[1], result[2], LocalTime.now());
            })
            .toList();
    }
}
