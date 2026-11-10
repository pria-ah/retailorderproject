import java.io.*;
import java.util.*;

// CUSTOM STACK (Linked List implementation)
class CustomStack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }
    private Node<T> top;
    private int size = 0;

    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        T val = top.data;
        top = top.next;
        size--;
        return val;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack is empty");
        return top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int size() { return size; }
}

// CUSTOM QUEUE (Linked List with head & tail)
class CustomQueue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) {
            head = tail;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        T val = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return val;
    }

    public boolean isEmpty() { return head == null; }
    public int size() { return size; }
}

public class OrderDispatchSystem {
    public static void main(String[] args) throws IOException {
        ArrayList<Order> orders = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("retail_orders_50000.csv"));
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] data = line.split(",");
            if (data.length < 4) continue;
            try {
                String idStr = data[0].replaceAll("[^0-9]", "");
                if (idStr.isEmpty()) continue;
                int orderId = Integer.parseInt(idStr);
                String customerId = data[1].trim();
                double amount = Double.parseDouble(data[2].trim());
                String createdAt = data[3].trim();
                orders.add(new Order(orderId, customerId, amount, createdAt));
            } catch (NumberFormatException ignored) {}
        }
        br.close();

        System.out.println("Loaded " + orders.size() + " orders for Stack & Queue benchmarking.\n");

        // 1. STACK BENCHMARK: CustomStack vs Built-in ArrayDeque (Undo last order edits)
        CustomStack<Order> customStack = new CustomStack<>();
        long startCustomStack = System.nanoTime();
        for (Order o : orders) customStack.push(o);
        while (!customStack.isEmpty()) customStack.pop();
        long endCustomStack = System.nanoTime();
        long customStackTime = endCustomStack - startCustomStack;

        ArrayDeque<Order> builtinStack = new ArrayDeque<>();
        long startBuiltinStack = System.nanoTime();
        for (Order o : orders) builtinStack.push(o);
        while (!builtinStack.isEmpty()) builtinStack.pop();
        long endBuiltinStack = System.nanoTime();
        long builtinStackTime = endBuiltinStack - startBuiltinStack;

        // 2. QUEUE BENCHMARK: CustomQueue vs Built-in ArrayDeque (Dispatch FIFO line)
        CustomQueue<Order> customQueue = new CustomQueue<>();
        long startCustomQueue = System.nanoTime();
        for (Order o : orders) customQueue.enqueue(o);
        while (!customQueue.isEmpty()) customQueue.dequeue();
        long endCustomQueue = System.nanoTime();
        long customQueueTime = endCustomQueue - startCustomQueue;

        ArrayDeque<Order> builtinQueue = new ArrayDeque<>();
        long startBuiltinQueue = System.nanoTime();
        for (Order o : orders) builtinQueue.offer(o);
        while (!builtinQueue.isEmpty()) builtinQueue.poll();
        long endBuiltinQueue = System.nanoTime();
        long builtinQueueTime = endBuiltinQueue - startBuiltinQueue;

        System.out.println("--- TASK 4 BENCHMARK RESULTS (50,000 Operations) ---");
        System.out.println("Custom Stack (Linked List)   : " + (customStackTime / 1_000_000.0) + " ms");
        System.out.println("Built-in Stack (ArrayDeque)  : " + (builtinStackTime / 1_000_000.0) + " ms");
        System.out.println("Custom Queue (Linked List)   : " + (customQueueTime / 1_000_000.0) + " ms");
        System.out.println("Built-in Queue (ArrayDeque)  : " + (builtinQueueTime / 1_000_000.0) + " ms");

        System.out.println("\n--- ARCHITECTURAL COMPARISON ---");
        System.out.println("Custom (Linked Nodes): Dynamic memory growth, zero amortized resize overhead, higher per-node pointer memory overhead.");
        System.out.println("Built-in (ArrayDeque): Contiguous array backing, superior cache locality, lower memory footprint, occasional resize copy.");
    }
}
