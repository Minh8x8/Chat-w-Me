package process;

import adt.Stack;

public class StackOfMessage {
    private Stack<Character> message;
    public StackOfMessage(){
        message = new Stack<>();
    }
    public void receiveMessage(String message) {
        for (Character c : message.toCharArray()) {
            this.message.push(c);
        }
    }
    public String getMessage() {
        StringBuilder sb= new StringBuilder();
        while (!message.isEmpty()) {
            sb.append(message.pop());
        }
        return sb.reverse().toString();
    }
}
