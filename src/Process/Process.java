package Process;

import ADT.Queue;
import ADT.Stack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Process {
    public static final Stack<Queue<Character>> stackOfMessages = new Stack<>();
    public static Queue<Character> message = new Queue<>();

    public void receiveMessage(Queue<Character> message) {
        stackOfMessages.push(message);
    }
    public void printMessage() {
        if (!stackOfMessages.isEmpty()) {
            String[] files = new String[stackOfMessages.size()];
            while (!stackOfMessages.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                message = stackOfMessages.pop();
                while (!message.isEmpty()) {
                    sb.append(message.deQueue());
                }
                files[stackOfMessages.size()] = sb.toString();
            }
            for (String file : files) {
                System.out.print(file);
                System.out.println(" (length: " + file.length() + ")");
            }
            saveFile(files);
        }
    }

    public void saveFile(String[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Minh\\OneDrive\\Desktop\\ChatwMe\\src\\db.txt", true))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
