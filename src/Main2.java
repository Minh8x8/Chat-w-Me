import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main2 {
    private static final Queue queue = new Queue();
    private static final Stack stack = new Stack();

    private static boolean isQuit = false;
    private static class TransportThread extends Thread {
        public void run() {
            Scanner sc = new Scanner(System.in);
            while (!isQuit) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-----Send Message-----");
                System.out.println("Enter message (or type 'quit' to exit):");
                String message = sc.nextLine();
                if (message.equals("quit")) isQuit = true;
                else {
                    System.out.println("Message length: " + message.length());
                    synchronized (queue) {
                        try {
                            for (char c : message.toCharArray()) {
                                queue.enQueue(c);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            sc.close();
        }
    }

    private static class ProcessThread extends Thread {
        public void run() {
            while (!isQuit) {
                synchronized (queue) {
                    while (!queue.isEmpty()) {
                        stack.push(queue.deQueue());
                    }
                }
                if (!stack.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    System.out.println("-----Received message-----");
                    while (!stack.isEmpty()) {
                        sb.append(stack.pop());
                    }
                    System.out.println("Message: " + sb.reverse().toString());
                    System.out.println("Message length: " + sb.length());

                    // write file to db.txt
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("db/db.txt", true)); // Open file in append mode
                        writer.write(sb.toString()); // Write the message to the file
                        writer.newLine(); // Add a newline character after the message
                        writer.close(); // Close the writer to save changes to the file
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        TransportThread transportThread = new TransportThread();
        ProcessThread processThread = new ProcessThread();

        System.out.println("---------- Chat w Me ----------");

        transportThread.start();
        processThread.start();

        try {
            transportThread.join();
            processThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Exit Chat w Me");
    }
}
