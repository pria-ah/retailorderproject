import java.io.*;
import java.util.*;

public class RetailOrderSorting {

    static void bubbleSort(ArrayList<Order> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).amount < list.get(j + 1).amount) {
                    Order temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void mergeSort(ArrayList<Order> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    static void merge(ArrayList<Order> list, int left, int mid, int right) {
        ArrayList<Order> leftList = new ArrayList<>(list.subList(left, mid + 1));
        ArrayList<Order> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).amount >= rightList.get(j).amount) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            list.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            list.set(k++, rightList.get(j++));
        }
    }

    static void printSampleTrace(ArrayList<Order> sample) {
        System.out.println("Initial Sample:");
        for (Order o : sample) System.out.print(o.amount + " ");
        System.out.println("\n");

        int n = sample.size();
        for (int i = 0; i < 3 && i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sample.get(j).amount < sample.get(j + 1).amount) {
                    Order temp = sample.get(j);
                    sample.set(j, sample.get(j + 1));
                    sample.set(j + 1, temp);
                }
            }
            System.out.print("Pass " + (i + 1) + ": ");
            for (Order o : sample) System.out.print(o.amount + " ");
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Order> allOrders = new ArrayList<>();

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
                allOrders.add(new Order(orderId, customerId, amount, createdAt));
            } catch (NumberFormatException ignored) {}
        }
        br.close();

        int limit = Math.min(10000, allOrders.size());
        ArrayList<Order> dataset = new ArrayList<>(allOrders.subList(0, limit));
        System.out.println("Loaded " + dataset.size() + " records for Task 2 benchmarking.\n");

        System.out.println("--- FIRST THREE PASSES TRACE (Bubble Sort Sample) ---");
        ArrayList<Order> traceSample = new ArrayList<>(dataset.subList(0, Math.min(6, dataset.size())));
        printSampleTrace(traceSample);

        ArrayList<Order> bubbleList = new ArrayList<>(dataset);
        long startBubble = System.nanoTime();
        bubbleSort(bubbleList);
        long endBubble = System.nanoTime();
        long bubbleTime = endBubble - startBubble;

        ArrayList<Order> mergeList = new ArrayList<>(dataset);
        long startMerge = System.nanoTime();
        mergeSort(mergeList, 0, mergeList.size() - 1);
        long endMerge = System.nanoTime();
        long mergeTime = endMerge - startMerge;

        ArrayList<Order> builtInList = new ArrayList<>(dataset);
        long startBuiltIn = System.nanoTime();
        builtInList.sort((o1, o2) -> Double.compare(o2.amount, o1.amount));
        long endBuiltIn = System.nanoTime();
        long builtInTime = endBuiltIn - startBuiltIn;

        System.out.println("\n--- BENCHMARK RESULTS (10,000 Records, Descending) ---");
        System.out.println("Bubble Sort O(n^2)    : " + (bubbleTime / 1_000_000.0) + " ms");
        System.out.println("Merge Sort O(n log n) : " + (mergeTime / 1_000_000.0) + " ms");
        System.out.println("Built-in Sort (TimSort): " + (builtInTime / 1_000_000.0) + " ms");
    }
}