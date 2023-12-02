package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ManagerServer_gRCP {
    public static void main(String[] args) throws Exception {
        ClientService clientService = new ClientService();
        Server server = ServerBuilder.forPort(8500).addService(clientService).build();
        server.start();
        server.awaitTermination();
        server.shutdown();
    }
}