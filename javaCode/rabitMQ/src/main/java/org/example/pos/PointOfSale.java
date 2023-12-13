package org.example.pos;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PointOfSale {
    private static final String EXCHANGE_NAME = "Sales_Exchange";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //EXCHANGE_NAME = readline("Exchange name");

            for (;;) {

                    // Routing keys for different topics
                    String routingKeySport = "ALIMENTAR.#";

                    // Messages for different topics
                    String messageSport = "Let's talk about sports!";
                    String messageTech = "New technology trends.";

                    // Publish messages with routing keys
                    channel.basicPublish(EXCHANGE_NAME, routingKeySport, null, messageSport.getBytes());
                    System.out.println(" [x] Sent '" + routingKeySport + "':'" + messageSport + "'");

                    String input = readline("Exit(y/n): ");
                    if (input.equals("y")) {
                        break;
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