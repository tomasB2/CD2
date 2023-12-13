package org.example;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import manager_server.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class UserApp {

    private static String regIP = "localhost";

    private static int regPort = 8500;

    private static ManagedChannel channel;

    private static ServiceToResumeGrpc.ServiceToResumeBlockingStub blockingStub;
    private static ServiceToResumeGrpc.ServiceToResumeStub noBlockStub;

    private static Scanner scan = new Scanner(System.in);

    private static String dir = "D:\\ISEL\\MEIC\\CD\\trab2\\a";


    public static void main(String[] args) {
        try {
            System.out.println("connecting to "+ regIP +":"+ regPort);
            channel = ManagedChannelBuilder.forAddress(regIP, regPort)
                    .usePlaintext()
                    .build();
            blockingStub = ServiceToResumeGrpc.newBlockingStub(channel);
            noBlockStub = ServiceToResumeGrpc.newStub(channel);
            while (true) {
                switch (Menu()) {
                    case 1:
                        System.out.println("Getting sales resume...");
                        FileDataWithName res = blockingStub.resume(NoParams.newBuilder().build());
                        Files.write(Path.of(dir + "\\" + res.getName() + ".txt"), res.getData().toByteArray());
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Resume");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op == 1) || op == 99));
        return op;
    }
}
