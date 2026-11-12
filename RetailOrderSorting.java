import java.io.*;
import java.util.*;

public class RetailOrderSorting {

    // Bubble Sort: O(n^2) - Descending order
    static void bubbleSort(ArrayList<Order> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).amount < list.get(j + 1).amount) {
                    Order temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    // Merge Sort: O(n log n) - Descending order
    static void mergeSort(ArrayList<Order> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    static void merge(ArrayList<Order> list, int left, int mid, int right) {
        ArrayList<Order> leftPart = new ArrayList<>(list.subList(left, mid + 1));
        ArrayList<Order> rightPart = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftPart.size() && j < rightPart.size()) {
            if (leftPart.get(i).amount >= rightPart.get(j).amount) {
                list.set(k++, leftPart.get(i++));
            } else {
                list.set(k++, rightPart.get(j++));
            }
        }
        while (i < leftPart.size()) list.set(k++, leftPart.get(i++));
        while (j < rightPart.size()) list.set(k++, rightPart.get(j++));
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("retail_orders_50000.csv"));
        br.readLine();

        String line;
        while ((line = br.readLine()) != null && orders.size() < 10000) {
            String[] p = line.split(",");
            if (p.length >= 4) {
                orders.add(new Order(Integer.parseInt(p[0].trim()), p[1].trim(), Double.parseDouble(p[2].trim()), p[3].trim()));
            }
        }
        br.close();

        // Trace first 3 passes on 5 sample numbers
        System.out.println("--- Tracing 3 Passes on Sample ---");
        double[] sample = {450.0, 120.0, 990.0, 310.0, 750.0};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < sample.length - 1 - i; j++) {
                if (sample[j] < sample[j + 1]) {
                    double t = sample[j];
                    sample[j] = sample[j + 1];
                    sample[j + 1] = t;
                }
            }
            System.out.println("Pass " + (i + 1) + ": " + Arrays.toString(sample));
        }

        // Compare runtimes on 10,000 records
        ArrayList<Order> bubbleList = new ArrayList<>(orders);
        long t1 = System.currentTimeMillis();
        bubbleSort(bubbleList);
        long t2 = System.currentTimeMillis();

        ArrayList<Order> mergeList = new ArrayList<>(orders);
        long t3 = System.currentTimeMillis();
        mergeSort(mergeList, 0, mergeList.size() - 1);
        long t4 = System.currentTimeMillis();

        ArrayList<Order> builtInList = new ArrayList<>(orders);
        long t5 = System.currentTimeMillis();
        builtInList.sort((a, b) -> Double.compare(b.amount, a.amount));
        long t6 = System.currentTimeMillis();

        System.out.println("\n--- Performance Comparison (10,000 Records) ---");
        System.out.println("Bubble Sort: " + (t2 - t1) + " ms");
        System.out.println("Merge Sort : " + (t4 - t3) + " ms");
        System.out.println("Built-in   : " + (t6 - t5) + " ms");
    }
}