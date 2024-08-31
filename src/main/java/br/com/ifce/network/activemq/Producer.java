package br.com.ifce.network.activemq;

import javax.jms.*;

public class Producer {

    private final Session session;

    private final MessageProducer messageProducer;

    public Producer(Session session, MessageProducer messageProducer) {
        this.session = session;
        this.messageProducer = messageProducer;
    }

    public static Producer newInstance(String queueName) {
        BrokerMediator mediator = BrokerMediator.getInstance();
        Connection connection = mediator.getConnection();
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            return new Producer(session, producer);
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Producer send(String text) {
        try {
            TextMessage message = this.session.createTextMessage(text);
            this.messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return this;
    }

    public void close() {
        try {
            this.messageProducer.close();
            this.session.close();
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
