package org.example;


import com.google.protobuf.ByteString;
import io.grpc.ServerBuilder;
import serviceToWriteStubs.*;
import io.grpc.stub.StreamObserver;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Server extends FileServiceGrpc.FileServiceImplBase {

    private static String currentResume = "resume";

    static Channel channel;

    private static String path = "D:\\ISEL\\MEIC\\CD\\trab2\\a\\";
    private static int svcPort = 8501;
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("34.163.78.25"); factory.setPort(5672);

            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(new Server())
                    .build();
            svc.start();
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void writeToFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        String fileName = request.getFileName();
        String fileData = request.getFileData();

        try {
            writeFileContents(fileName, fileData);

            FileResponse response = FileResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            FileResponse response = FileResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorMessage(e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void readFromFile(FileReadRequest request, StreamObserver<ReadFileResponse> responseObserver) {
        String fileName = request.getFileName();

        try {
            String fileText = readFileContents(fileName);

            ReadFileResponse response = ReadFileResponse.newBuilder()
                    .setSuccess(true)
                    .setFileText(fileText)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            ReadFileResponse response = ReadFileResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorMessage(e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void askForResume(NoParams request, StreamObserver<FileRequestBytes> responseObserver) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = currentDateTime.format(formatter);
        currentResume = "resume-" + formattedDateTime;



        try {
            channel.basicPublish("EX", "BROD",null , "null".getBytes());
            Thread.sleep(10000);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            byte[] data = readFileContentsBytes(currentResume);
            FileRequestBytes response = FileRequestBytes.newBuilder()
                    .setFileName(currentResume)
                    .setFileData(ByteString.copyFrom(data))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeResume(FileData request, StreamObserver<NoParams> responseObserver) {
        try {
            writeFileContents(currentResume, request.getFileData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseObserver.onNext(NoParams.newBuilder().build());
        responseObserver.onCompleted();
    }

    private static void writeFileContents(String fileName, String fileData) throws IOException {
        try (FileWriter fileWriter = new FileWriter(path + fileName, true)) {
            fileWriter.write(fileData + "\n");
        }
    }

    private byte[] readFileContentsBytes(String fileName) throws IOException {
        Path filePath = Paths.get(path + fileName);
        return Files.readAllBytes(filePath);
    }

    private String readFileContents(String fileName) throws IOException {
        Path filePath = Paths.get(path + fileName);
        return new String(Files.readAllBytes(filePath));
    }
}