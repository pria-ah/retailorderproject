import java.util.*;

public class RefundChange {

    // 1. Greedy Approach
    static void minCoinsGreedy(int[] coins, int amount) {
        ArrayList<Integer> used = new ArrayList<>();
        int remaining = amount;

        for (int i = coins.length - 1; i >= 0; i--) {
            while (remaining >= coins[i]) {
                used.add(coins[i]);
                remaining -= coins[i];
            }
        }

        System.out.println("Greedy result for " + amount + ": " + used + " (Count: " + used.size() + ")");
    }

    // 2. Dynamic Programming Approach
    static void minCoinsDP(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        System.out.println("DP optimal coin count for " + amount + ": " + dp[amount]);
    }

    public static void main(String[] args) {
        // Standard system where Greedy works
        int[] standardCoins = {1, 2, 5, 10, 20, 50, 100};
        System.out.println("--- Standard Currency (Greedy Works) ---");
        minCoinsGreedy(standardCoins, 288);

        // Custom set where Greedy fails: Coins {1, 3, 4}, Amount = 6
        // Greedy gives: 4, 1, 1 (3 coins)
        // DP gives: 3, 3 (2 coins)
        int[] customCoins = {1, 3, 4};
        System.out.println("\n--- Denomination where Greedy Fails ---");
        minCoinsGreedy(customCoins, 6);
        minCoinsDP(customCoins, 6);
    }
}