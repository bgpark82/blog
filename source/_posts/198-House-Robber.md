---
title: "198.\_House Robber"
catalog: true
date: 2022-10-02 00:41:19
subtitle:
header-img:
tags:
---
## Conditions

---

- **it will automatically contact the police if two adjacent houses were broken into on the same night**
- return ***the maximum amount of money** you can rob tonight **without alerting the police***

## Solve by hands first

---

it’s dynamic programming + memoization problem. Time complexity is O(n) and space complexity is O(n)

## Solution

---

### top-down

```java
class Solution {
    
    int[] dp;
    
    public int rob(int[] nums) {
        dp = new int[nums.length];
        Arrays.fill(dp, -1);
        
        return dfs(nums, nums.length-1);
    }
    
    private int dfs(int[] nums, int i) { 
        if(i < 0) return 0;
        if(dp[i] != -1) return dp[i];
        
        return dp[i] = Math.max(dfs(nums, i - 2) + nums[i], dfs(nums, i - 1));
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c9d36ba0-fb69-4459-9339-656af4969d12/Untitled.png)

![999401C1-FE13-48F2-9187-866676E614F9.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c93822ee-eed1-4881-8021-a0bfebd15105/999401C1-FE13-48F2-9187-866676E614F9.jpeg)

![F93EAE72-6D07-4C5F-913A-E55CC0C43572.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9b400338-072b-44fd-a807-ec376ccec8e8/F93EAE72-6D07-4C5F-913A-E55CC0C43572.jpeg)

in order to find maximum amount of money at house `i` , we have to compare the cumumlative amount of money at house `i-1` as well. because there we can’t rob the adjacent house so we have to decide which house has more amount money between `i` and `i-1` even tho house at `i`

there are 2 options to rob

1. rob current house `i` 
2. don’t rob current house

first option means that add current money `i` to an cumulative money `i-2`

secodn option is that choose the an cumulative money `i-1` houses

```java
Math.max(dfs(i - 2) + nums[i], dfs(i - 1));
```

### bottom-up

```java
class Solution {
    
    int[] dp;
    
    public int rob(int[] nums) {
        dp = new int[nums.length + 1];
        
        dp[0] = 0;
        dp[1] = nums[0];
        for(int i = 2; i < dp.length; i++) {
            dp[i] = Math.max(dp[i-2] + nums[i-1], dp[i - 1]);
        }
        return dp[dp.length - 1];
    }
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e03378ed-1277-4b21-a795-4b0632ffb016/Untitled.png)

## Lesson I learnt

---

1. dynamic programming can be start from length to 0
2. relationship between sub problem and current problem
    
    ```java
    Math.max(dfs(i-2) + nums[i]
    ```
    
3. use Arrays method to fill with -1
    
    ```java
    Arrays.fill(dp, -1);
    ```
    
4. value of node can be element of memoization
    
    ![F93EAE72-6D07-4C5F-913A-E55CC0C43572.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9b400338-072b-44fd-a807-ec376ccec8e8/F93EAE72-6D07-4C5F-913A-E55CC0C43572.jpeg)
    
5. need extra space for bottom up memoization
    - I thought I was able to reuse `nums` array but it cause the out of bound at `i-2`
    - so I create `dp` with size of nums.length + 1 and set 0 at index 0 for `i-2`
    
    ```java
    int[] dp;
      
    public int rob(int[] nums) {
        dp = new int[nums.length + 1];
        
        dp[0] = 0;
        dp[1] = nums[0];
        for(int i = 2; i < dp.length; i++) {
            dp[i] = Math.max(dp[i-2] + nums[i-1], dp[i - 1]);
        }
        return dp[dp.length - 1];
    }
    ```
