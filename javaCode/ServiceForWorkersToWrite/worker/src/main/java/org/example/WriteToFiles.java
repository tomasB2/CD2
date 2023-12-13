package org.example;

import serviceToWriteStubs.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class WriteToFiles {

    private static ManagedChannel channel;

    private static FileServiceGrpc.FileServiceBlockingStub blockingStub;
    private static FileServiceGrpc.FileServiceStub noBlockStub;

    public WriteToFiles() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        blockingStub = FileServiceGrpc.newBlockingStub(channel);
        noBlockStub = FileServiceGrpc.newStub(channel);
    }

    public FileResponse writeToFile(String fileName, String data) {
        return blockingStub.writeToFile(FileRequest.newBuilder()
                .setFileName(fileName)
                .setFileData(data)
                .build());
    }

    public String readFromFile(String fileName) {
        return blockingStub.readFromFile(FileReadRequest.newBuilder()
                .setFileName(fileName)
                .build()).getFileText();
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
