package transport;

import adt.Queue;

public class BufferOfMessage {
    private Queue<Character> message;
    public BufferOfMessage(){
        message = new Queue<>();
    }
    public void addMessage(String message){
        for (Character c : message.toCharArray()) {
            this.message.enQueue(c);
        }
    }
    public String sendMessage(){
        StringBuilder sb = new StringBuilder();
        while (!this.message.isEmpty()) {
            sb.append(this.message.deQueue());
        }
        return sb.toString();
    }
}
