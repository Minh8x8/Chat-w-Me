package server;

import transport.BufferOfMessage;
import process.StackOfMessage;
import db.DB;
import view.ChatView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private ChatView chatView;
    private ServerSocket server;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected = false;
    ReceiveMessageHandler receiveMessageHandler;
    private List<String> messages;
    private StackOfMessage stackOfReceiveMessage = new StackOfMessage();
    private StackOfMessage stackOfSendMessage = new StackOfMessage();

    public ChatServer() {
        try {
            chatView = new ChatView(this);
            chatView.setTitle("Chat w Me - Server");
            chatView.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Start server
            setHistoryTableInView();

            System.out.println("Server started");
            chatView.printMessage("Wait for client connect to chat server");

            server = new ServerSocket(8080);
            client = server.accept();

            System.out.println("Client connected");
            chatView.printMessage("Client connected ");

            isConnected = true;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            messages = new ArrayList<>();

            receiveMessageHandler = new ReceiveMessageHandler();
            receiveMessageHandler.start();
            try {
                receiveMessageHandler.join();
                chatView.printMessage("Client is disconnected");
                isConnected = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (SocketException e){
            System.out.println("Server closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shutdown() {
        try {
            if (isConnected) {
                in.close();
                out.println("Server closed");
                out.close();
                client.close();
            }
            server.close();
            receiveMessageHandler.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void setHistoryTableInView() {
        DB db = new DB();
        db.setDB();
        chatView.setHistoryTable(db.getTableChat());
    }
    public List<String> getMessages(int row_id) {
        DB db = new DB();
        db.setDB();
        return db.getSpecificChatId(row_id);
    }
    public void sendMessage(String message){
        if (isConnected) {
            if (message.equals("L1VuZG8")) {
                out.println(message);
            }
            else {
                BufferOfMessage transport = new BufferOfMessage();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime currentDateTime = LocalDateTime.now();
                String formattedDateTime = currentDateTime.format(formatter);
                message = "Server [" + formattedDateTime + "]: " + message;
                try {
                    stackOfSendMessage.pushMessage(message);
                    transport.addMessage(message);
                    String send = transport.sendMessage();
                    messages.add(send);
                    chatView.printMessage(send);
                    out.println(send);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void saveChat(){
        /* save message to database
        check if the conversation is null or not */
        if (messages == null || messages.isEmpty()) {
            System.out.println();
            System.out.println("Nothing to save");
        }
        else {
            DB db = new DB();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String currentTime = currentDate.format(formatter);
            db.insertToTableChat(currentTime);
            for(String message : messages){
                db.insertMessage(message);
            }
            System.out.println("Save successfully");
        }
    }
    public class ReceiveMessageHandler extends Thread {
        @Override
        public void run() {
            stackOfReceiveMessage = new StackOfMessage();
            String clientMessage;
            Scanner scanner;
            try {
                scanner = new Scanner(client.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (scanner.hasNextLine()) {
                try {
                    clientMessage = scanner.nextLine();
                    // L1VuZG8 just means /Undo encode to base64
                    if (clientMessage.equals("L1VuZG8")) {
                        undoReceiveMessage();
                    } else {
                        stackOfReceiveMessage.pushMessage(clientMessage);
                        messages.add(clientMessage);
                        chatView.printMessage(clientMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void undoReceiveMessage() {
        if (!stackOfReceiveMessage.isEmpty()) {
            String message = stackOfReceiveMessage.popMessage();
            chatView.undoMessage(message);
            // Remove the message for saving in DB
            messages.removeIf(m -> m.equals(message));
        }
    }
    public void undoSendMessage() {
        if (!stackOfSendMessage.isEmpty()) {
            // Undo the message and send a command for undo
            String message = stackOfSendMessage.popMessage();
            chatView.undoMessage(message);
            // Remove the message for saving in DB
            messages.removeIf(m -> m.equals(message));
            sendMessage("L1VuZG8");
        }
    }
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
    }
}