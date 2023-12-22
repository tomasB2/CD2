package org.example;
import serviceToWriteStubs.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;


public class GRP_Comunication {

    private static String workerFile;
    private static ManagedChannel channel;
    private static FileServiceGrpc.FileServiceBlockingStub blockingStub;
    private static FileServiceGrpc.FileServiceStub noBlockStub;

    public GRP_Comunication(String wf) {
        channel = ManagedChannelBuilder.forAddress("localhost", 8501)
                .usePlaintext()
                .build();

        blockingStub = FileServiceGrpc.newBlockingStub(channel);
        noBlockStub = FileServiceGrpc.newStub(channel);
        workerFile = "worker-" + wf;
    }

    public FileResponse writeToFile(String data, String s) {
        return blockingStub.writeToFile(FileRequest.newBuilder()
                .setFileName(workerFile)
                .setFileData(data)
                .build());
    }

    public String readFromFile() {
        return blockingStub.readFromFile(FileReadRequest.newBuilder()
                .setFileName(workerFile)
                .build()).getFileText();
    }

    public void writeToResume() {
        String data = readFromFile();
        blockingStub.writeResume(FileData.newBuilder()
                .setFileData(data)
                .build());
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}