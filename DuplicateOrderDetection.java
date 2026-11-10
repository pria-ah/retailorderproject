import java.io.*;
import java.util.*;

public class DuplicateOrderDetection {

    static String extractMinute(String timestamp) {
        // Expected format: YYYY-MM-DD HH:MM:SS or ISO format
        // Truncate at the minute mark (first 16 characters: "YYYY-MM-DD HH:MM")
        if (timestamp.length() >= 16) {
            return timestamp.substring(0, 16);
        }
        return timestamp;
    }

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

        System.out.println("Loaded " + orders.size() + " orders for Duplicate Detection.\n");

        // Key: customerId + "|" + amount + "|" + minuteTimestamp
        // Value: List of Order IDs sharing the identical composite key
        Map<String, List<Order>> map = new HashMap<>();
        List<Order> duplicates = new ArrayList<>();

        long start = System.nanoTime();

        for (Order o : orders) {
            String minute = extractMinute(o.createdAt);
            String compositeKey = o.customerId + "|" + o.amount + "|" + minute;

            if (map.containsKey(compositeKey)) {
                duplicates.add(o);
                map.get(compositeKey).add(o);
            } else {
                List<Order> list = new ArrayList<>();
                list.add(o);
                map.put(compositeKey, list);
            }
        }

        long end = System.nanoTime();

        System.out.println("--- DUPLICATE DETECTION RESULTS ---");
        System.out.println("Execution Time: " + ((end - start) / 1_000_000.0) + " ms");
        System.out.println("Total Duplicate Orders Flagged: " + duplicates.size());

        if (!duplicates.isEmpty()) {
            System.out.println("\nSample Duplicates (First 5):");
            int shown = 0;
            for (Order dup : duplicates) {
                if (shown++ >= 5) break;
                String minute = extractMinute(dup.createdAt);
                String key = dup.customerId + "|" + dup.amount + "|" + minute;
                System.out.println("Duplicate Order ID: " + dup.orderId + 
                                   " | Customer: " + dup.customerId + 
                                   " | Amount: " + dup.amount + 
                                   " | Minute: " + minute);
            }
        }

        System.out.println("\n--- COMPLEXITY ANALYSIS ---");
        System.out.println("Time Complexity  : O(n) - Single linear scan with O(1) average hash lookup/insertion.");
        System.out.println("Space Complexity : O(n) - Auxiliary HashMap stores up to n unique keys.");
    }
}