public class Stack {
    int top = -1;
    char[] s;

    public Stack() {
        s = new char[256];
    }
    void push(char c) {
        if (top == 256-1) throw new IndexOutOfBoundsException("Overflow");
        else top = top + 1;
        s[top] = c;
    }
    char pop() {
        if (top == -1) throw new IndexOutOfBoundsException("Underflow");
        return s[top--];
    }
    boolean isEmpty() {
        return top == -1;
    }
    int size() {
        return top;
    }
}
