import java.io.*;
import java.util.*;

class Order {
    int id;
    String customerId;
    double amount;
    String createdAt;

    Order(int id, String customerId, double amount, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}

public class RetailOrderSearch {

    static Order linearSearch(ArrayList<Order> list, int targetId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == targetId) {
                return list.get(i);
            }
        }
        return null;
    }

    static Order binarySearch(ArrayList<Order> list, int targetId) {
        int low = 0, high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (list.get(mid).id == targetId) {
                return list.get(mid);
            } else if (list.get(mid).id < targetId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("retail_orders_50000.csv"));
        br.readLine(); // skip header

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                int id = Integer.parseInt(parts[0].trim());
                double amount = Double.parseDouble(parts[2].trim());
                orders.add(new Order(id, parts[1].trim(), amount, parts[3].trim()));
            }
        }
        br.close();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Order ID to search: ");
        int key = sc.nextInt();

        // 1. Linear Search
        long t1 = System.nanoTime();
        Order r1 = linearSearch(orders, key);
        long t2 = System.nanoTime();

        // Sort before Binary Search
        orders.sort(Comparator.comparingInt(o -> o.id));

        // 2. Binary Search
        long t3 = System.nanoTime();
        Order r2 = binarySearch(orders, key);
        long t4 = System.nanoTime();

        System.out.println("\nLinear Search result: " + (r1 != null ? "Found ID " + r1.id : "Not Found"));
        System.out.println("Linear Time: " + (t2 - t1) + " ns");

        System.out.println("Binary Search result: " + (r2 != null ? "Found ID " + r2.id : "Not Found"));
        System.out.println("Binary Time: " + (t4 - t3) + " ns");
        sc.close();
    }
}