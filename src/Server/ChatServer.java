package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        @Override
        public void run() {
            try {
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