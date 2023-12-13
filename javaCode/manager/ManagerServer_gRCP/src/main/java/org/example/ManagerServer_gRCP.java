package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ManagerServer_gRCP {
    public static void main(String[] args) throws Exception {
        ResumeService resumeService = new ResumeService();
        Server server = ServerBuilder.forPort(8500).addService(resumeService).build();
        server.start();
        server.awaitTermination();
        server.shutdown();
    }
}