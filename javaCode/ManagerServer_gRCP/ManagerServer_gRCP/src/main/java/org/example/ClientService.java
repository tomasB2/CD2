package org.example;

import com.google.protobuf.ByteString;
import org.jetbrains.annotations.NotNull;
import manager_server.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class ClientService extends ServiceToResumeGrpc.ServiceToResumeImplBase {

    @Override
    public void resume(@NotNull NoParams np, @NotNull StreamObserver<FileName> name) {
        System.out.println("Resume");
        name.onNext(FileName.newBuilder().setName("Name1").build());
        name.onCompleted();
    }

    @Override
    public void getSales(@NotNull FileName request, StreamObserver<FileDataWithName> responseObserver) {
        System.out.println("getSales");
        List<byte[]> bytes = new ArrayList<>();
        // load data from file
        responseObserver.onNext(
                FileDataWithName
                .newBuilder()
                .setName(request)
                .setData(
                        FileData.newBuilder().
                                setData(ByteString.EMPTY)
                                .build()
                ).build()
        );
        responseObserver.onCompleted();
    }
}
