
import java.io.*;
import java.util.*;

class SimpleStack {
    ArrayList<Order> list = new ArrayList<>();

    void push(Order o) {
        list.add(o);
    }

    Order pop() {
        return list.remove(list.size() - 1);
    }

    boolean isEmpty() {
        return list.isEmpty();
    }
}

class SimpleQueue {
    ArrayList<Order> list = new ArrayList<>();

    void enqueue(Order o) {
        list.add(o);
    }

    Order dequeue() {
        return list.remove(0);
    }

    boolean isEmpty() {
        return list.isEmpty();
    }
}

public class OrderDispatchSystem {

    public static void main(String[] args) throws Exception {

        ArrayList<Order> orders = new ArrayList<>();

        BufferedReader br = new BufferedReader(
            new FileReader("retail_orders_50000.csv"));

        br.readLine();
        String line;

        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");

            if (p.length >= 4) {
                orders.add(new Order(
                    Integer.parseInt(p[0].trim()),
                    p[1].trim(),
                    Double.parseDouble(p[2].trim()),
                    p[3].trim()
                ));
            }
        }

        br.close();

        // Custom Stack
        SimpleStack customStack = new SimpleStack();

        for (Order o : orders)
            customStack.push(o);

        while (!customStack.isEmpty())
            customStack.pop();

        // Custom Queue
        SimpleQueue customQueue = new SimpleQueue();

        for (Order o : orders)
            customQueue.enqueue(o);

        while (!customQueue.isEmpty())
            customQueue.dequeue();

        // Built-in Stack
        Stack<Order> builtinStack = new Stack<>();

        for (Order o : orders)
            builtinStack.push(o);

        while (!builtinStack.isEmpty())
            builtinStack.pop();

        // Built-in Queue
        Queue<Order> builtinQueue = new ArrayDeque<>();

        for (Order o : orders)
            builtinQueue.offer(o);

        while (!builtinQueue.isEmpty())
            builtinQueue.poll();

        System.out.println("Custom Stack: Completed");
        System.out.println("Custom Queue: Completed");
        System.out.println("Built-in Stack: Completed");
        System.out.println("Built-in Queue: Completed");

        System.out.println("Task 4 completed successfully.");
    }
}
