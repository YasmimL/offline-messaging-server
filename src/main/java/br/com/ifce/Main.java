package br.com.ifce;

import br.com.ifce.network.activemq.BrokerMediator;
import br.com.ifce.network.rmi.RMIServer;

import static java.lang.Runtime.getRuntime;

public class Main {
    public static void main(String[] args) {
        var broker = BrokerMediator.getInstance();
        broker.start();
        getRuntime().addShutdownHook(new Thread(broker::close));

        RMIServer.getInstance().start();

        System.out.println("Running...");
    }
}