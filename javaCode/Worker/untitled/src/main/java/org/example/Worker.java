package org.example;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import java.util.Scanner;

public class Worker {
    static String IP_BROKER="localhost";
    static Logger logger=new SimpleLoggerFactory().getLogger("RabbitMQ-Consumer");

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP_BROKER); factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            Scanner scan = new Scanner(System.in);

            // Consumer handler to receive messages
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), "UTF-8");
                String routingKey=delivery.getEnvelope().getRoutingKey();
                System.out.println("Message Received:" +consumerTag+":"+ routingKey+":"+recMessage);
            };
            // Consumer handler to receive cancel receiving messages
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("CANCEL Received! "+consumerTag);
            };
            //String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException;
            //String consumeTag=channel.basicConsume(readline("Queue name?"), true, deliverCallback, cancelCallback);
            //System.out.println("Consumer Tag:"+consumeTag);
            // sem autoAck
            DeliverCallback deliverCallbackWithoutAck = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), "UTF-8");
                String routingKey=delivery.getEnvelope().getRoutingKey();
                long deliverTag=delivery.getEnvelope().getDeliveryTag();
                System.out.println(consumerTag+": Message Received:" + routingKey+":"+recMessage);

                //void basicAck(long deliveryTag, boolean multiple) throws IOException;
                //void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;
                if (recMessage.equals("nack"))
                    channel.basicNack(deliverTag, false, true);
                else channel.basicAck(deliverTag,false);
            };
            String consumerTag=channel.basicConsume(readline("Queue name?"), false, deliverCallbackWithoutAck, cancelCallback);
            System.out.println(consumerTag+": waiting for messages or Press any key to finish");
            scan.nextLine();
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