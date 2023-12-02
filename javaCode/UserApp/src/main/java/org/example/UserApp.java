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


    public static void main(String[] args) {
        try {
            System.out.println("connecting to "+ regIP +":"+ regPort);
            channel = ManagedChannelBuilder.forAddress(regIP, regPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            blockingStub = ServiceToResumeGrpc.newBlockingStub(channel);
            noBlockStub = ServiceToResumeGrpc.newStub(channel);
            while (true) {
                switch (Menu()) {
                    case 1:
                        System.out.println("Id:");
                        FileName res = blockingStub.resume(NoParams.newBuilder().build());
                        System.out.println("Sales file name: " + res.getName());
                        break;
                    case 2:
                        System.out.println("Name:");
                        String name = scan.next();
                        System.out.println("Directory to save the file:");
                        String dir = scan.next();
                        System.out.println("Image will be saved as a .jpg");
                        Path file = Paths.get(dir + "/" + name + ".jpg");
                        StreamObserver<FileName> res2 = noBlockStub.getSales(new StreamObserver<FileDataWithName>() {
                            @Override
                            public void onNext(FileDataWithName fileDataWithName) {
                                System.out.println("onNext");
                                try {
                                    System.out.println("Saving file");
                                    Files.write(file, fileDataWithName.getData().getData().toByteArray());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                System.out.println("onError");
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }
                        });
                        res2.onNext(FileName.newBuilder().setName(name).build());
                        res2.onCompleted();
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
            System.out.println(" 1 - Ask for resume file");
            System.out.println(" 2 - Download sales file");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 2) || op == 99));
        return op;
    }
}
