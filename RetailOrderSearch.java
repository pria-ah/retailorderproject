
import java.io.*;
import java.util.*;

class Order {
    int orderId;
    String customerId;
    double amount;
    String createdAt;

    Order(int orderId, String customerId, double amount, String createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}

public class RetailOrderSearch {

    // LINEAR SEARCH
    static Order linearSearch(ArrayList<Order> orders, int key) {

        for (int i = 0; i < orders.size(); i++) {

            if (orders.get(i).orderId == key) {
                return orders.get(i);
            }
        }

        return null;
    }

    // BINARY SEARCH
    static Order binarySearch(ArrayList<Order> orders, int key) {

        int low = 0;
        int high = orders.size() - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            if (orders.get(mid).orderId == key) {
                return orders.get(mid);
            }

            else if (orders.get(mid).orderId < key) {
                low = mid + 1;
            }

            else {
                high = mid - 1;
            }
        }

        return null;
    }

    public static void main(String[] args) throws IOException {

        ArrayList<Order> orders = new ArrayList<>();

        // READ CSV FILE
        BufferedReader br = new BufferedReader(
            new FileReader("retail_orders_50000.csv")
        );

        String line;

        br.readLine(); // Skip header

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] data = line.split(",");
            if (data.length < 4) {
                continue;
            }

            int orderId = Integer.parseInt(data[0].trim());
            String customerId = data[1].trim();
            double amount = Double.parseDouble(data[2].trim());
            String createdAt = data[3].trim();

            orders.add(new Order(orderId, customerId, amount, createdAt));
        }

        br.close();

        System.out.println("Total Orders Loaded: " + orders.size());

        // GET ORDER ID FROM USER
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Order ID to search: ");
        int key = sc.nextInt();

        // LINEAR SEARCH
        long start1 = System.nanoTime();

        Order result1 = linearSearch(orders, key);

        long end1 = System.nanoTime();

        System.out.println("\n--- LINEAR SEARCH ---");

        if (result1 != null) {
            System.out.println("Order Found!");
            System.out.println("Order ID: " + result1.orderId);
            System.out.println("Customer ID: " + result1.customerId);
            System.out.println("Amount: " + result1.amount);
            System.out.println("Created At: " + result1.createdAt);
        } else {
            System.out.println("Order Not Found!");
        }

        long linearTime = end1 - start1;

        System.out.println("Execution Time: " + linearTime + " ns");

        // SORT ORDERS BY ORDER ID FOR BINARY SEARCH
        ArrayList<Order> sortedOrders = new ArrayList<>(orders);

        Collections.sort(sortedOrders,
            Comparator.comparingInt(o -> o.orderId));

        // BINARY SEARCH
        long start2 = System.nanoTime();

        Order result2 = binarySearch(sortedOrders, key);

        long end2 = System.nanoTime();

        System.out.println("\n--- BINARY SEARCH ---");

        if (result2 != null) {
            System.out.println("Order Found!");
            System.out.println("Order ID: " + result2.orderId);
            System.out.println("Customer ID: " + result2.customerId);
            System.out.println("Amount: " + result2.amount);
            System.out.println("Created At: " + result2.createdAt);
        } else {
            System.out.println("Order Not Found!");
        }

        long binaryTime = end2 - start2;

        System.out.println("Execution Time: " + binaryTime + " ns");

        // COMPARISON
        System.out.println("\n--- PERFORMANCE COMPARISON ---");

        System.out.println("Linear Search: " + linearTime + " ns");
        System.out.println("Binary Search: " + binaryTime + " ns");

        sc.close();
    }
}
