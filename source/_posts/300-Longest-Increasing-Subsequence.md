---
title: "300.\_Longest Increasing Subsequence"
catalog: true
date: 2022-10-20 22:08:47
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the length of the longest strictly increasing subsequence without changing order

### **Edge cases**

---

- [] → 0
- [1] → 1
- [1,1,1,1,1] → 1
- [10, 1, 3, 7, 5, 15, 100, 20] → [1, 3, 7, 15, 20] → 5

### **Brute force approach (Discussion + Complexity)**

---

we can use dynamic programming. Iterate throught every element in array. in the recursion, find the max length of increasing subsequence within remaining subarray. Once we find the max length then add 1 for current value. Base line is when the current index is the last index, then return 1 which means the last index value is part of the increasing subsequence. Time complexity is O(n^n) and space complexity is O(1)

```java
public int lengthOfLIS(int[] nums) {
		int max = 0;
		for(int i = 0; i < nums.length; i++) {
			max = Math.max(max, dfs(nums, i));
		}
		return max;
}

private int dfs(int[] nums, int idx) {
    if(idx >= nums.length - 1) return 1;

    int max = 0;
    for(int i = idx+1; i < nums.length; i++) {
        if(nums[idx] >= nums[i]) continue;
        max = Math.max(max, dfs(nums, i));
    }
    return max + 1;
}
```

![6CFFFF82-C60B-4AAF-B7B2-925B7E9BA821.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c35b1406-6646-495e-b894-1b0dd34348e9/6CFFFF82-C60B-4AAF-B7B2-925B7E9BA821.jpeg)

### **Optimised approach (Discussion + Complexity) …X times**

---

We can optimize above solution with memoization. What we looking for is the maximum length at index 0. So we can store the maximum length at every index (which can be node in recursion). Time complexity is O(n^2)

```java
Integer[] dp;

public int lengthOfLIS(int[] nums) {
		dp = new Integer[nums.length];
		int max = 0;
		for(int i = 0; i < nums.length; i++) {
			max = Math.max(max, dfs(nums, i));
		}
		return max;
}

private int dfs(int[] nums, int idx) {
    if(idx >= nums.length - 1) return 1;
		if(dp[idx] != null) return dp[idx];

    int max = 0;
    for(int i = idx+1; i < nums.length; i++) {
        if(nums[idx] >= nums[i]) continue;
        max = Math.max(max, dfs(nums, i));
    }
    return dp[idx] = max + 1;
}
```

![2F761690-DFCF-4DC2-B1F7-71C4DA7D2002.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c8ae39a7-d8fa-4895-80ea-81c7c14cc78a/2F761690-DFCF-4DC2-B1F7-71C4DA7D2002.jpeg)

We can also use top down approach. But it’s not easy to come up with the idea because elements in the dp array should as least have length of 1. because every element can be potentially belong to subsequence. So, we have to set 1 in dp array from the beginning. then we can start from top to bottom. from the last index decrease index to 0. and in the inner loop, iterate from outer index to last index to see previous index has larger subsequence or not. **If maxium length in any previous index can jump to current index, max length of previous index + 1 is the maximum length of increasing subsequence at current index**. So, **we can simple replace the max length at current index to max length at any previous index + 1**. Time complexity is O(n^2) (a bit complicated explaination I will soon clear it up!)

```java
int[] dp = new int[nums.length];
Arrays.fill(dp,1);
for(int i = nums.length-1; i >= 0; i--) {
	for(int j = i+1; j < nums.length-1; j++) {
		if(nums[i] >= nums[j]) continue;
		nums[i] = Math.max(nums[i], nums[j]+1);
	}
}
Arrays.sort(dp);
return dp[nums.length-1];
```

![84BD3CE9-BC72-499C-931E-18B06AEC1A54.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ca128783-4d01-49ee-8d3c-5b4b5e4d93cb/84BD3CE9-BC72-499C-931E-18B06AEC1A54.jpeg)

### **Coding**

---

```java
Integer[] dp;

public int lengthOfLIS(int[] nums) {
		dp = new Integer[nums.length];
		int max = 0;
		for(int i = 0; i < nums.length; i++) {
			max = Math.max(max, dfs(nums, i));
		}
		return max;
}

private int dfs(int[] nums, int idx) {
    if(idx >= nums.length - 1) return 1;
		if(dp[idx] != null) return dp[idx];

    int max = 0;
    for(int i = idx+1; i < nums.length; i++) {
        if(nums[idx] >= nums[i]) continue;
        max = Math.max(max, dfs(nums, i));
    }
    return dp[idx] = max + 1;
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/008e57f7-4349-4c81-9412-49cbe54f3404/Untitled.png)

### What I learned

---

1. dynamic programing program can be greedy by any chance
2. There is [O(nlog(n)) approach](https://leetcode.com/problems/longest-increasing-subsequence/discuss/1326308/C%2B%2BPython-DP-Binary-Search-BIT-Solutions-Picture-explain-O(NlogN)) as well 
3. Important to know what represent node and return in dp 
4. In top down dynamic programing, it start from the last index to 0. check with the next value and if it’s greater than current element then compare the length of current longest subsequence at current index and the length of previous longest subsequence. the point is **compare the cached value at current index to next index and updated the cached array at current index**

```java
for(int i = nums.length-1; i >= 0; i--) {
    for(int j = i+1; j < nums.length; j++) {
        if(nums[i] >= nums[j]) continue;

				// cached array at current index vs next index + 1 and update cached value at current index to whichever max value
        df[i] = Math.max(df[i], df[j]+1); 
    }
}
```
