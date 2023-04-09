public class Queue {
    int front = -1;
    int rear = -1;
    int max;
    char[] q;

    public Queue() {
        this.max = 256;
        this.q = new char[max];
    }

    void enQueue(char c) {
        if ((rear + 1) % max == front) {
            throw new IndexOutOfBoundsException("Overflow");
        }
        if (front == -1 & rear == - 1){
            front = 0; rear = 0;
        }
        else if (rear == max - 1 & front != 0){
            rear = 0;
        }
        else {
            rear = (rear + 1) % max;
        }
        q[rear] = c;
    }
    char deQueue() {
        if (front == -1) {
            throw new IndexOutOfBoundsException("Underflow");
        }
        char val = q[front];
        if (front == rear){
            front = -1;
            rear = -1;
        }
        else if (front == max - 1){
            front = 0;
        }
        else {
            front = front + 1;
        }
        return val;
    }
    boolean isEmpty() {
        return front == -1 && rear == -1;
    }
}
