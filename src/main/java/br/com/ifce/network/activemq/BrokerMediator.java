package br.com.ifce.network.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static org.apache.activemq.ActiveMQConnection.*;

public class BrokerMediator {

    private final Connection connection;

    private static final BrokerMediator INSTANCE = new BrokerMediator();

    public static BrokerMediator getInstance() {
        return INSTANCE;
    }

    private BrokerMediator() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_URL);
            this.connection = connectionFactory.createConnection(DEFAULT_USER, DEFAULT_PASSWORD);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            this.connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
