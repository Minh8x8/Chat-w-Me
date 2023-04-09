package test;

import Transport.Transport;
import Process.Process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ChatwMe {
    private static final Transport transport = new Transport();
    private static final Process process = new Process();
    private static final Scanner scanner = new Scanner(System.in);
    public static void printMenu(){
        System.out.println("-----Chat w Me-----");
        System.out.println("1. Send a message");
        System.out.println("2. Print messages received");
        System.out.println("3. Exit");
        System.out.println("4. Clear screen");
        System.out.println("5. Show all messages from db");
    }
    public static void sendAMessage(){
        System.out.println("Enter message");
        String message = scanner.nextLine();
        try {
            transport.saveMessageToQueue(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Message's length: " + message.length());
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void printMessagesReceived(){
        while (!transport.isMessageEmpty()) {
            process.receiveMessage(transport.sendMessage());
        }
        process.printMessage();
    }
    public static void readFile(){
        BufferedReader reader;
        try {
            // Open the file for reading
            reader = new BufferedReader(new FileReader("C:\\Users\\Minh\\OneDrive\\Desktop\\ChatwMe\\src\\db.txt"));
            String line = reader.readLine();
            while (line != null) {
                // Process each line of the file
                System.out.println(line + " (length: " + line.length() + ")");
                line = reader.readLine();
            }
            // Close the file
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int command = -1;
        while (true) {
            try {
                printMenu();
                command = Integer.parseInt(scanner.nextLine());;
                if (command == 3) break;
                switch (command) {
                    case 1:
                        sendAMessage();
                        break;
                    case 2:
                        printMessagesReceived();
                        break;
                    case 4:
                        clearScreen();
                        break;
                    case 5:
                        readFile();
                        break;
                    default:
                        System.out.println("Invalid command, please try again");
                        break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
