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
    public StreamObserver<FileName> getSales(StreamObserver<FileDataWithName> responseObserver) {
        System.out.println("getSales");
        return new StreamObserver<FileName>() {
            @Override
            public void onNext(FileName fileName) {
                System.out.println("onNext");
                // ir buscar o ficheiro
                responseObserver.onNext(
                        FileDataWithName
                                .newBuilder()
                                .setName(fileName)
                                .setData(
                                        FileData.newBuilder().
                                                setData(ByteString.EMPTY)
                                                .build()
                                ).build()
                );
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
                responseObserver.onError(throwable);
            }
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
                responseObserver.onCompleted();
            }
        };
    }
}
