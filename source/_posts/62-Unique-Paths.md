---
title: "62.\_Unique Paths"
catalog: true
date: 2022-10-06 23:24:34
subtitle:
header-img:
tags:
---
## Conditions

---

- The robot tries to move to the **bottom-right corner**
- The robot can only move either down or right at any point in time.
- return *the number of possible unique paths that the robot can take to reach the bottom-right corner*
- so that the answer will be less than or equal to `2 * 10^9`

## Solve by hands first

---

we can use dynamic programming with memoization. we can implement both bottom-up and top-down approach. 

The point is first rows and columns is always filled with 1. because it only can go to right or bottom. **it is base case.** The general term of dynamic programming is `*dp[i][j] = dp[i-1][j] + dp[i][j-1]*`

## Solution

---

### bottom up

```java
class Solution {
    
    public int uniquePaths(int m, int n) (int m, int n) {
        dp = new Integer[m+1][n+1];
        return dfs(m,n);
    }
    
    private int dfs(int m, int n) {
        if(m == 1 && n == 1) return 1;
        if(m == 1 || n == 1) return 1;
        if(dp[m][n] != null) return dp[m][n];
        
        return dp[m][n] = dfs(m-1, n) + dfs(m, n-1);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9b6b8db8-883e-423e-b2e8-a89e00235ea9/Untitled.png)

![97D7AA1E-D074-44F0-93F0-0F3D8BDF628D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/911fadb1-473a-4682-a6bd-26c34851e7ea/97D7AA1E-D074-44F0-93F0-0F3D8BDF628D.jpeg)

## Lesson I learnt

---

1. Similar problems
    - [91. Decode Ways](https://leetcode.com/problems/decode-ways)
    - [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
    - [509. Fibonacci Number](https://leetcode.com/problems/fibonacci-number/)
2. it is very important to define the base case

```java
private int dfs(int m, int n) {
		// base case
    if(m == 1 && n == 1) return 1;
    if(m == 1 || n == 1) return 1;
    
    return dp[m][n] = dfs(m-1, n) + dfs(m, n-1);
}
```
        int[][] dp = new int[m+1][n+1];
        
        for(int i = 0; i <= m; i++) dp[i][0] = 1;
        for(int i = 0; i <= n; i++) dp[0][i] = 1;
       
        for(int i = 1; i < m; i++) {
            for(int j = 1; j < n; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        
        return dp[m-1][n-1];
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0c48bc8d-800a-4250-80e0-2b9d9a2570b8/Untitled.png)

![333F363A-7719-4EC4-B252-49E36731A98C.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bf6268b6-7926-4a33-8dab-317c38e4ef12/333F363A-7719-4EC4-B252-49E36731A98C.jpeg)

### top down

```java
class Solution {
    
    Integer[][] dp;
    
    public int uniquePaths{
