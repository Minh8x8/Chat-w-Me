package test;

import ADT.Queue;
import ADT.Stack;

public class TestADT {
    public static void main(String[] args) {
        Queue<Character> messageQueue = new Queue<>(); // Queue to store messages
        Queue<Queue<Character>> messageList = new Queue<>(); // Queue to store list of messages

// Add a message to the messageQueue
        messageQueue.enQueue('H');
        messageQueue.enQueue('e');
        messageQueue.enQueue('l');
        messageQueue.enQueue('l');
        messageQueue.enQueue('o');

// Add the messageQueue to the messageList
        messageList.enQueue(messageQueue);

// Clear the messageQueue for the next message
        messageQueue.clear();

// Add another message to the messageQueue
        messageQueue.enQueue('W');
        messageQueue.enQueue('o');
        messageQueue.enQueue('r');
        messageQueue.enQueue('l');
        messageQueue.enQueue('d');

// Add the messageQueue to the messageList
        messageList.enQueue(messageQueue);

// Print out the messageList
        System.out.println(messageList.toString()); // Output: [[H, e, l, l, o], [W, o, r, l, d]]

    }
}
