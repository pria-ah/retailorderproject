import java.io.*;
import java.util.*;

public class DuplicateOrderDetection {
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

        // HashSet for checking seen keys
        HashSet<String> seen = new HashSet<>();
        ArrayList<Order> duplicates = new ArrayList<>();

        for (Order o : orders) {
            // First 16 characters represent "YYYY-MM-DD HH:mm"
            String minute = o.createdAt.length() >= 16 ? o.createdAt.substring(0, 16) : o.createdAt;
            String key = o.customerId + "|" + o.amount + "|" + minute;

            if (seen.contains(key)) {
                duplicates.add(o);
            } else {
                seen.add(key);
            }
        }

        System.out.println("Total Orders Scanned: " + orders.size());
        System.out.println("Duplicate Orders Found: " + duplicates.size());
        System.out.println("Time Complexity : O(n)");
        System.out.println("Space Complexity: O(n)");
    }
}