package org.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;

import java.util.*;

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
                String data = readline("Qual o texto da data (exit to finish)?");
                String codigoProduto = readline("Qual o codigo do produto (exit to finish)?");
                String nomeProduto = readline("Qual o nome do produto (exit to finish)?");
                String quant = readline("Qual a quantidade (exit to finish)?");
                String precoUnitario = readline("Qual o preco unitario (exit to finish)?");
                String iva = readline("Qual o iva (exit to finish)?");
                if (data.compareTo("exit")==0 ||
                        codigoProduto.compareTo("exit")==0 ||
                        nomeProduto.compareTo("exit")==0 ||
                        quant.compareTo("exit")==0 ||
                        precoUnitario.compareTo("exit")==0 ||
                        iva.compareTo("exit")==0) break;
                Product product = new Product(data, Integer.parseInt(codigoProduto), nomeProduto, Integer.parseInt(quant), Double.parseDouble(precoUnitario), Double.parseDouble(iva));
                // convert product to json object
                JSONObject jsonObject = new JSONObject(product);
                for (;;) {
                    String topic=readline("Qual o Topic key (exit to finish)?");
                    if (topic.compareTo("exit") == 0) break;
                    channel.basicPublish(exchangeName, topic,null , jsonObject.toString().getBytes());
                    break;
                }
            System.out.println("Message Sent");
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