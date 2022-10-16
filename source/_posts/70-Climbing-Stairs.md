---
title: "70.\_Climbing Stairs"
catalog: true
date: 2022-10-16 13:34:34
subtitle:
header-img:
tags:
---
### **Requirements**

---

- It takes `n` steps to reach the top.
- Each time you can either climb `1` or `2` steps
- In how many distinct ways can you climb to the top?

### **Edge cases**

---

- 3
    - 1 1 1 = dp[2] + 1
    - 1 2 = dp[1] + 1
    - 2 1 = dp[2] + 1
    - dp[2] = 2
    - dp[1] = 1
- 2 → [1,1][2]

### **Brute force approach (Discussion + Complexity)**

---

*jump into the optimized solution without brute force. In case they get stuck, it backfires*

*you already have a non-optimized solution in place which is always better than nothing*

*in some instances, brute force is in fact the desired solution*

*mention the complexity of the brute force*

*ask the interviewer if you should code this approach*

### **Optimised approach (Discussion + Complexity) …X times**

---

In order to reach nth steps. We can take 1 more steps from n-1 th or n-2 th steps. So, recursino formula would be dp[n] = dp[n-1] + dp[n-2]

### **Coding**

---

Bottom-up

```java
class Solution {
    public int climbStairs(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1; dp[1] = 1;
        for(int i = 2; i <= n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }
}
```

Top-down

```java
class Solution {
		Integer[] dp;
		
		public int climbStairs(int n) {
		    dp = new Integer[n + 1];
		    return dfs(n);
		}
		
		private int dfs(int n) {
		    if(n == 1) return 1;
		    if(n == 2) return 2;
		    if(dp[n] != null) return dp[n];
		    return dp[n] = dfs(n-1) + dfs(n-2);
		}
}
```

### **Dry Run**

---

*It is extremely important to run it through a couple of sample test cases*

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/decb965d-ab3e-4a7c-9cfb-cf53f6a288a0/Untitled.png)

### What I learned

---
