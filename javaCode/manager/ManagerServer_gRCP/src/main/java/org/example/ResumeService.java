package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import manager_server.NoParams;
import org.jetbrains.annotations.NotNull;
import manager_server.*;
import serviceToWriteStubs.*;
import io.grpc.stub.StreamObserver;

public class ResumeService extends ServiceToResumeGrpc.ServiceToResumeImplBase {

    private static String regIP = "localhost";

    private static int regPort = 8501;

    private static ManagedChannel channel;

    private static FileServiceGrpc.FileServiceBlockingStub blockingStub;
    private static FileServiceGrpc.FileServiceStub noBlockStub;

    public ResumeService() {
        channel = ManagedChannelBuilder.forAddress(regIP, regPort)
                .usePlaintext()
                .build();
        blockingStub = FileServiceGrpc.newBlockingStub(channel);
        noBlockStub = FileServiceGrpc.newStub(channel);
    }

    @Override
    public void resume(@NotNull NoParams np, @NotNull StreamObserver<FileDataWithName> name) {
        System.out.println("Resume request");
        FileRequestBytes data = blockingStub.askForResume(serviceToWriteStubs.NoParams.newBuilder().build());
        name.onNext(FileDataWithName.newBuilder().setName(data.getFileName()).setData(data.getFileData()).build());
        name.onCompleted();
    }
}
