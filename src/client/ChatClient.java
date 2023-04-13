package client;

import db.DB;
import process.StackOfMessage;
import server.ChatServer;
import transport.BufferOfMessage;
import view.ClientChatView;
import view.LoadingChatView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatClient {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected = false;
    ReceiveMessageHandler receiveMessageHandler;
    private ClientChatView chatView;
    private boolean done;
    private StackOfMessage stackOfReceiveMessage = new StackOfMessage();
    private StackOfMessage stackOfSendMessage = new StackOfMessage();

    public ChatClient() {
        chatView = new ClientChatView(this);
        chatView.setTitle("Chat w Me - Client");
        System.out.println("Connecting to server...");
        try {
            setHistoryTableInView();
            isConnected = false;
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            LoadingChatView loadingChatView = new LoadingChatView();
            loadingChatView.setVisible(true);
            while (!isConnected) {
                try {
                    client = new Socket("localhost", 8080);
                    if (client.isConnected()) {
                        isConnected = true;
                    }
                } catch (SocketException e) {
                    System.out.println("Try connecting");
                    Thread.sleep(1000);
                    //e.printStackTrace();
                }
            }
            System.out.println("Connected!");
            chatView.printMessage("Connected!");
            loadingChatView.dispose();
            chatView.setVisible(true);
            chatView.isConnected = true;
            isConnected = true;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            receiveMessageHandler = new ReceiveMessageHandler();
            receiveMessageHandler.start();
            try {
                receiveMessageHandler.join();
                chatView.printMessage("Server is closed");
                isConnected = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        done = true;
        try {
            if (isConnected) {
                in.close();
                out.close();
                client.close();
                receiveMessageHandler.interrupt();
            }
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
                message = "Client [" + formattedDateTime + "]: " + message;
                try {
                    stackOfSendMessage.pushMessage(message);
                    transport.addMessage(message);
                    String send = transport.sendMessage();
                    chatView.printMessage(send);
                    out.println(send);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                    } else{
                        stackOfReceiveMessage.pushMessage(clientMessage);
                        chatView.printMessage(clientMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void undoReceiveMessage(){
        if (!stackOfReceiveMessage.isEmpty()) {
            String message = stackOfReceiveMessage.popMessage();
            chatView.undoMessage(message);
        }
    }

    public void undoSendMessage() {
        if (!stackOfSendMessage.isEmpty()) {
            // Undo the message and send a command for undo
            String message = stackOfSendMessage.popMessage();
            chatView.undoMessage(message);
            sendMessage("L1VuZG8");
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
    }
}
