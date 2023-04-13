package process;

import adt.Stack;

public class StackOfMessage {
    private Stack<String> message;
    public StackOfMessage(){
        message = new Stack<>();
    }
    public void pushMessage(String message) {
        this.message.push(message);
    }
    public String popMessage() {
        return message.pop();
    }
    public boolean isEmpty() {
        return message.isEmpty();
    }
}
