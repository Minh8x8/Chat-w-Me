package Server;

import db.DB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket server;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private boolean done;

    public ChatServer() {
        try {
            done = false;
            // Start server
            System.out.println("Server started");
            server = new ServerSocket(8080);
            client = server.accept();
            System.out.println("Client connected");
            System.out.println("/help for more information");
            // create
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // create a thread for handle input
            InputHandler ih = new InputHandler();
            Thread t = new Thread(ih);
            t.start();
            String clientMessage;
            while ((clientMessage = in.readLine()) != null){
                if (clientMessage.equals("/quit")) {
                    done = true;
                    break;
                } else {
                    System.out.println(clientMessage);
                }
            }
            shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }
    public void shutdown() {
        done = true;
        try {
            in.close();
            out.println("/quit");
            out.close();
            client.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class InputHandler implements Runnable {
        public void help() {
            System.out.println("-----List of commands-----");
            System.out.println("/history : show the chat history");
            System.out.println("/show [chat id] : show the chat history of the specified chat id");
            System.out.println("/quit : stop the program");
            System.out.println("--------------------------");
        }

        public void chatHistory(DB db) {
            System.out.println("-----Chat History-----");
            db.setDB();
            List<String[]> chatList = db.getTableChat();
            System.out.printf("%-5s%s", "id", "time\n");
            for (String[] chat : chatList) {
                System.out.printf("%-5s%s\n", chat[0], chat[1]);
            }
            System.out.println("----------------------");
        }

        public void showIDChatHistory(DB db, int id) {
            db.setDB();
            boolean isContainMessage = false;
            List<String[]> detailTable = db.getTableDetail();
            List<String> messages = new ArrayList<>();
            for (String[] message : detailTable) {
                if (Integer.parseInt(message[1]) == id) {
                    messages.add(message[2]);
                    isContainMessage = true;
                }
            }
            if (!isContainMessage) {
                System.out.println("Invalid message id");
            } else {
                System.out.println("----- Message Id ["+id+"] History -----");
                for (String m : messages) {
                    System.out.println(m);
                }
                System.out.println("---------------------------------------");
            }
        }

        @Override
        public void run() {
            try {
                DB db = new DB();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime currentDateTime = LocalDateTime.now();
                String formattedDateTime;
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    if (inReader.ready()) {
                        formattedDateTime = currentDateTime.format(formatter);
                        String message = inReader.readLine();
                        if (message.equals("/quit")) {
                            out.println("/quit");
                            inReader.close();
                            shutdown();
                            System.out.println("Stop program");
                        } else if (message.equals("/history")) {
                            chatHistory(db);
                        } else if (message.equals("/help")) {
                            help();
                        } else if (message.startsWith("/show")) {
                            String id = message.split("\\s+")[1];
                            try {
                                int chatID = Integer.parseInt(id);
                                showIDChatHistory(db, chatID);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input: Please input a number");
                            }
                        } else {
                            out.println("Server [" + formattedDateTime + "]: " + message);
                        }
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
    }
}