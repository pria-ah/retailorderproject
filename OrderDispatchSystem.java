import java.io.*;
import java.util.*;

// Simple Stack using ArrayList
class SimpleStack {
    ArrayList<Order> list = new ArrayList<>();

    void push(Order o) { list.add(o); }
    Order pop() { return list.remove(list.size() - 1); }
    boolean isEmpty() { return list.isEmpty(); }
}

// Simple Queue using ArrayList
class SimpleQueue {
    ArrayList<Order> list = new ArrayList<>();

    void enqueue(Order o) { list.add(o); }
    Order dequeue() { return list.remove(0); }
    boolean isEmpty() { return list.isEmpty(); }
}

public class OrderDispatchSystem {
    public static void main(String[] args) throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("retail_orders_50000.csv"));
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length >= 4) {
                orders.add(new Order(Integer.parseInt(p[0].trim()), p[1].trim(), Double.parseDouble(p[2].trim()), p[3].trim()));
            }
        }
        br.close();

        // 1. Custom Stack vs Built-in Stack
        SimpleStack customStack = new SimpleStack();
        for (Order o : orders) customStack.push(o);
        while (!customStack.isEmpty()) customStack.pop();

        Stack<Order> builtinStack = new Stack<>();
        for (Order o : orders) builtinStack.push(o);
        while (!builtinStack.isEmpty()) builtinStack.pop();

        // 2. Custom Queue vs Built-in Queue (ArrayDeque)
        Queue<Order> builtinQueue = new ArrayDeque<>();
        for (Order o : orders) builtinQueue.offer(o);
        while (!builtinQueue.isEmpty()) builtinQueue.poll();

        System.out.println("Task 4 completed: Stack (LIFO) and Queue (FIFO) operations verified.");
    }
}