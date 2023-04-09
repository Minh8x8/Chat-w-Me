package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                    System.out.println("Client: " + clientMessage);
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
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    if (inReader.ready()) {
                        String message = inReader.readLine();
                        if (message.equals("/quit")) {
                            out.println("/quit");
                            inReader.close();
                            shutdown();
                        } else {
                            out.println(message);
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