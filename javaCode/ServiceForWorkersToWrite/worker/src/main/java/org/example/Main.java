package org.example;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static String GenerateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private static final String fileName = GenerateUUID() + ".txt";

    private static final WriteToFiles writeService = new WriteToFiles();

    private static void menu() {
        System.out.println("1. Send message");
        System.out.println("2. Resume");
        System.out.println("3. Exit");
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            while (true) {
                menu();

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.println("Enter message");
                    String message = scanner.nextLine();
                    // send message to server
                    writeService.writeToFile(fileName, message);
                }

                else if (choice == 2) {
                    String resume = writeService.readFromFile(fileName);
                    writeService.writeToFile(fileName, resume);
                }

                else if (choice == 3) {
                    break;
                }
            }
        } finally {
            writeService.shutdown();
        }
    }
}