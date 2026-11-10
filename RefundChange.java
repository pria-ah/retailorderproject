import java.util.*;

public class RefundChange {

    // GREEDY APPROACH
    // Sort denominations descending and pick the largest denomination possible at each step
    static List<Integer> minCoinsGreedy(int[] coins, int amount) {
        Integer[] sortedCoins = Arrays.stream(coins).boxed().toArray(Integer[]::new);
        Arrays.sort(sortedCoins, Collections.reverseOrder());

        List<Integer> result = new ArrayList<>();
        int remaining = amount;

        for (int coin : sortedCoins) {
            while (remaining >= coin) {
                result.add(coin);
                remaining -= coin;
            }
        }

        if (remaining != 0) return null; // Cannot make exact change
        return result;
    }

    // DYNAMIC PROGRAMMING APPROACH (Bottom-Up)
    // Guarantees global minimum coin count for ANY valid denomination set
    static List<Integer> minCoinsDP(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        int[] parent = new int[amount + 1];

        Arrays.fill(dp, amount + 1);
        Arrays.fill(parent, -1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    parent[i] = coin;
                }
            }
        }

        if (dp[amount] > amount) return null; // No solution

        // Reconstruct chosen denominations
        List<Integer> result = new ArrayList<>();
        int curr = amount;
        while (curr > 0) {
            int coin = parent[curr];
            result.add(coin);
            curr -= coin;
        }

        return result;
    }

    public static void main(String[] args) {
        // Standard canonical currency set (Greedy succeeds)
        int[] standardCoins = {100, 50, 20, 10, 5, 2, 1};
        int standardAmount = 288;

        System.out.println("=== 1. STANDARD REFUND CHANGE (Canonical System) ===");
        System.out.println("Amount: " + standardAmount);
        System.out.println("Denominations: " + Arrays.toString(standardCoins));

        List<Integer> standardGreedy = minCoinsGreedy(standardCoins, standardAmount);
        System.out.println("Greedy Coins Used (" + standardGreedy.size() + "): " + standardGreedy);

        // Non-canonical system where Greedy FAILS
        // Example: Denominations {1, 3, 4}, Refund Amount = 6
        // Greedy takes: 4, 1, 1 (3 coins)
        // DP finds optimal: 3, 3 (2 coins)
        int[] customCoins = {1, 3, 4};
        int failureAmount = 6;

        System.out.println("\n=== 2. NON-CANONICAL SYSTEM (Greedy Suboptimality Demonstration) ===");
        System.out.println("Refund Target Amount: " + failureAmount);
        System.out.println("Available Denominations: " + Arrays.toString(customCoins));

        List<Integer> greedyResult = minCoinsGreedy(customCoins, failureAmount);
        System.out.println("\n[Greedy Approach]");
        System.out.println("Coins Chosen : " + greedyResult);
        System.out.println("Coin Count   : " + (greedyResult != null ? greedyResult.size() : "No Solution"));
        System.out.println("Verdict      : Suboptimal (Local optimum picks 4 first, leaving remainder 2)");

        List<Integer> dpResult = minCoinsDP(customCoins, failureAmount);
        System.out.println("\n[Dynamic Programming Approach]");
        System.out.println("Coins Chosen : " + dpResult);
        System.out.println("Coin Count   : " + (dpResult != null ? dpResult.size() : "No Solution"));
        System.out.println("Verdict      : Globally Optimal (Evaluates all subproblems dp[i - c] + 1)");

        System.out.println("\n=== COMPLEXITY & RATIONALE ===");
        System.out.println("Greedy: O(k log k + Amount / minCoin) time | O(1) auxiliary space.");
        System.out.println("DP    : O(Amount * k) time               | O(Amount) auxiliary space.");
    }
}