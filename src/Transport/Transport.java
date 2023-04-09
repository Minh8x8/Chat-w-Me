package Transport;

import ADT.Queue;

public class Transport {
    private static final Queue<Queue<Character>> queueOfMessages = new Queue<>();

    public void saveMessageToQueue(String message){
        Queue<Character> queue = new Queue<>();
        for (char c : message.toCharArray()){
            queue.enQueue(c);
        }
        queueOfMessages.enQueue(queue);
    }

    public Queue<Character> sendMessage(){
        return queueOfMessages.deQueue();
    }

    public boolean isMessageEmpty() { return queueOfMessages.isEmpty(); }
}
