# Set 1 – Retail Order Desk: Engineering & Complexity Analysis

---

### Task 1: Order Lookup (Linear Search vs. Binary Search)
* **Time Complexity:** 
  * Linear Search: $O(n)$ worst-case, $O(1)$ best-case.
  * Binary Search: $O(\log n)$ worst-case, $O(1)$ best-case (requires an initial $O(n \log n)$ sort).
* **Space Complexity:** $O(1)$ auxiliary for in-place search traversal.
* **Data Structure Rationale:**
  * Support agents require sub-millisecond retrieval across $50,000$ records. Linear search scans each contiguous record in $O(n)$, scaling linearly with dataset size. Binary search operates on sorted contiguous memory (`ArrayList`), halving search space at each step ($k \approx \log_2(50,000) \approx 16$ comparisons), delivering near-instant response times for high-concurrency systems.

---

### Task 2: Dispatch Ranking (Bubble Sort vs. Merge Sort vs. TimSort)
* **Time Complexity:**
  * Bubble Sort: $O(n^2)$ worst/average, $O(n)$ best.
  * Merge Sort: $O(n \log n)$ worst/average/best.
  * Built-in Sort (`Collections.sort` / TimSort): $O(n \log n)$ worst/average, $O(n)$ best.
* **Space Complexity:**
  * Bubble Sort: $O(1)$ auxiliary (in-place).
  * Merge Sort: $O(n)$ auxiliary (temporary split buffers).
  * Built-in Sort: $O(n)$ auxiliary.
* **Empirical Observation (10,000 records):**
  * Bubble Sort: ~97.69 ms
  * Merge Sort: ~4.03 ms
  * Built-in Sort (TimSort): ~3.60 ms
* **Data Structure Rationale:**
  * Dispatch sorting demands stable, predictable throughput. While Bubble Sort swaps adjacent elements with zero extra memory, its quadratic runtime makes it infeasible for production volume. Merge Sort uses divide-and-conquer to maintain strict $O(n \log n)$ bounds regardless of initial array ordering. TimSort optimizes Merge Sort by finding natural sequential runs in data, offering maximum execution efficiency.

---

### Task 3: Duplicate Order Detection
* **Time Complexity:** $O(n)$ average, $O(n^2)$ worst-case (under pathological hash collision).
* **Space Complexity:** $O(n)$ auxiliary space to store unique composite keys.
* **Data Structure Rationale:**
  * Finance must flag duplicate submissions prior to payment settlement. A brute-force nested pairwise comparison requires $O(n^2)$ time. By hashing a composite key (`customerId + "|" + amount + "|" + minuteTimestamp`), average-case lookup and insertion run in amortized $O(1)$ time, scanning all $50,000$ entries in ~22 ms.

---

### Task 4: Order Modification Undo & Dispatch Routing
* **Time Complexity:** 
  * Push/Pop (Stack) & Enqueue/Dequeue (Queue): $O(1)$ worst-case for Linked List; amortized $O(1)$ for `ArrayDeque`.
* **Space Complexity:** $O(n)$ memory for storage.
* **Empirical Observation (50,000 operations):**
  * Custom Stack (Linked List): ~3.51 ms
  * Built-in Stack (`ArrayDeque`): ~2.64 ms
  * Custom Queue (Linked List): ~2.59 ms
  * Built-in Queue (`ArrayDeque`): ~1.33 ms
* **Data Structure Rationale:**
  * **Stack (LIFO):** Ideal for undoing edits where the most recent modification must revert first.
  * **Queue (FIFO):** Guarantees order dispatching follows exact submission order.
  * **Memory Comparison:** Custom linked structures dynamically allocate nodes without array resizing overhead, but incur pointer memory overhead per node. Built-in `ArrayDeque` uses contiguous memory blocks, offering superior CPU cache locality and lower per-element memory footprint.

---

### Task 5: Minimum Coin/Note Change for Refunds
* **Time Complexity:**
  * Greedy: $O(k \log k + \frac{\text{Amount}}{\text{minCoin}})$ where $k$ is the number of coin denominations.
  * Dynamic Programming: $O(\text{Amount} \times k)$ time.
* **Space Complexity:**
  * Greedy: $O(1)$ auxiliary space.
  * Dynamic Programming: $O(\text{Amount})$ auxiliary space for DP table and path reconstruction.
* **Algorithmic Rationale & Failure Case:**
  * In canonical currency systems (e.g., standard notes $[100, 50, 20, 10, 5, 2, 1]$), the greedy choice property holds: picking the largest possible denomination locally guarantees a global minimum.
  * For non-canonical systems (e.g., denominations $\{1, 3, 4\}$ and refund amount $6$), greedy picks $4$, leaving $2$, resulting in $\{4, 1, 1\}$ ($3$ coins). Dynamic Programming solves subproblems bottom-up using $dp[i] = \min_{c}(dp[i - c] + 1)$, finding the true global optimum $\{3, 3\}$ ($2$ coins).