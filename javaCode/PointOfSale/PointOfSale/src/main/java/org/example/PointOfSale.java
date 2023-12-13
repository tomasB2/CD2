package org.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PointOfSale {
    private static String IP_BROKER="localhost";

    private static String exchangeName = null;

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Send message to exchange
            exchangeName = readline("Exchange name");

            for (;;) {
                String messageBody = readline("Qual o texto da mensagem (exit to finish)?");
                if (messageBody.compareTo("exit") == 0) break;
                for (;;) {
                    String topic=readline("Qual o Topic key (exit to finish)?");
                    if (topic.compareTo("exit") == 0) break;
                    channel.basicPublish(exchangeName, topic,null , messageBody.getBytes());
                }

            }
            channel.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }
}