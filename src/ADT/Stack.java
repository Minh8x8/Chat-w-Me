package ADT;

public class Stack<E> {
    int top = -1;
    Object[] s;

    public Stack() {
        s = new Object[250];
    }
    public void push(E c) {
        if (top == 256-1) throw new IndexOutOfBoundsException("Overflow");
        else top = top + 1;
        s[top] = c;
    }
    public E pop() {
        if (top == -1) throw new IndexOutOfBoundsException("Underflow");
        return (E) s[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top+1;
    }
}

