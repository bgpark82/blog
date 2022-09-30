---
title: "322.\_Coin Change"
catalog: true
date: 2022-09-30 19:29:01
subtitle:
header-img:
tags:
---
## Conditions

---

- Return *the fewest number of coins that you need to make up that amount*
- You may assume that you have an infinite number of each kind of coin.

## Solve by hands first

---

most of permutation problem can be handled by Dynamic programming. 

## Solution

---

### bottom up

```java
class Solution {
    

    Integer[] dp;
    
    public int coinChange(int[] coins, int amount) {
        dp = new Integer[amount+1];
        return dfs(coins, amount);
    }
    
    private int dfs(int[] coins, int amount) {
        
        // condition at last node
        if(amount < 0) return -1;
        if(amount == 0) return 0;
        
        // minimum count at amount 
        if(dp[amount] != null) return dp[amount];
        
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < coins.length; i++) {
            int count = dfs(coins, amount - coins[i]);
            if(count >= 0) min = Math.min(c + 1, min);
        }
        
        return dp[amount] = (min == Integer.MAX_VALUE) ? -1 : min;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/188c68b9-9bcc-4641-9f32-02c4b5873da7/Untitled.png)

the meaning of each element in `dp[x]` is that minimum combination when `x` amount left. for instance **dp[5] = 1, which mean when amount left 5 then there are only 1 combination to be amount = 0**. so in this question, what we want to know is number of combination to make amount 0, so we have to consider **all of the possibility from 0 to amount**.

![72846EBD-1718-468F-A90A-5D623DC11E7D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d8a93ed8-e2b8-40b8-9402-5a40775beb1b/72846EBD-1718-468F-A90A-5D623DC11E7D.jpeg)

### top down

```java
class Solution {
    
    int[] dp;
    
    public int coinChange(int[] coins, int amount) {
        dp = new int[amount + 1];
        
        int sum = 0;
        while(++sum <= amount) {
            
            int min = -1;
            for(int coin : coins) {          
                // sum - coin < 0 : can't be 0 at current node
                // dp[1] == -1 : there is no way to make 0 when 1 is left 
                if(sum - coin >= 0 && dp[sum - coin] != -1) {
                    int tmp = dp[sum - coin] + 1;                    
                    min = (min < 0) ? tmp : (tmp < min) ? tmp : min;
                }
            }    
            dp[sum] = min;
        }
        
        return dp[amount];
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4d7ed11b-8ffc-4ffe-a147-ee1543ad58d7/Untitled.png)

![16435737-A6CD-4AD8-8464-FD182778E0CE.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/22798aee-8143-4c89-9a3f-68a1c157a765/16435737-A6CD-4AD8-8464-FD182778E0CE.jpeg)

## Lesson I learnt

---

1. always define sub problem in dynamic programming
2. **decide what to be the value of node**. in this case, remainer is value of node. **The value of node is mostly one of the method parameter**. 
    
    ![96252388-D5CA-4291-82E9-A5380BA54248.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d9972b4c-e1f4-40ff-a714-562a4a1159e4/96252388-D5CA-4291-82E9-A5380BA54248.jpeg)
    
3. The return value of dynamic programming is what we want. but with memoization, it can also be the value at the node in momization array
    
    ```java
    return dp[amount] = (min == Integer.MAX_VALUE) ? -1 : min;
    ```
    
    - since we want to know the minimum number of combination. so return the **minimum number** and **memoization array**
4. In dynamic programming, it’s important to decide **the value of node** and **return value**.
5. utilize default value (ie. dp[0] = -1, dp[last index] = 0)
6. understand elements of memoization array 
    - dp[1] = -1 means there is no way to make 0 when 1 is left
7. in a nutshell
    1. **define the value of node (ie. index)**
    2. **define the return value (ie. sum)**
    3. **define the element of memoization (ie. min value)**
