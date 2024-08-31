package br.com.ifce.network.activemq;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Consumer {

    private final Session session;

    private final MessageConsumer messageConsumer;

    private final List<String> messages = new ArrayList<>();

    public Consumer(Session session, MessageConsumer messageConsumer) {
        this.session = session;
        this.messageConsumer = messageConsumer;
    }

    public static Consumer newInstance(String queueName) {
        BrokerMediator mediator = BrokerMediator.getInstance();
        Connection connection = mediator.getConnection();
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);

            return new Consumer(session, consumer);
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Consumer readAll() {
        while (true) {
            try {
                Message message = this.messageConsumer.receiveNoWait();
                if (message instanceof TextMessage) {
                    this.messages.add(((TextMessage) message).getText());
                } else {
                    break;
                }
            } catch (JMSException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    public List<String> close() {
        try {
            this.messageConsumer.close();
            this.session.close();
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return this.messages;
    }
}
