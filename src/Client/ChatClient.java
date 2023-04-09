package Client;

import Server.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class ChatClient {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private boolean done;
    private Thread inputHandlerThread;

    public ChatClient() {
        System.out.println("Connecting to server...");
        try {
            client = new Socket("localhost", 8080);
            System.out.println("Connected!");
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // create a thread for handle input
            InputHandler ih = new InputHandler();
            inputHandlerThread = new Thread(ih);
            inputHandlerThread.start();
            String serverMessage;
            while ((serverMessage = in.readLine()) != null){
                if (serverMessage.equals("/quit")) {
                    System.out.println("Server has disconnected");
                    shutdown();
                    break;
                } else {
                    System.out.println("Server: " + serverMessage);
                }
            }
            System.out.println("Stop program");
        } catch (IOException e) {
            System.out.println("Connect failed");
        }
    }

    public void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            client.close();
            inputHandlerThread.interrupt();
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
        ChatClient client = new ChatClient();
    }
}
