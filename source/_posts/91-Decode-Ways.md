---
title: "91.\_Decode Ways"
catalog: true
date: 2022-10-21 22:10:58
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the number of ways to decode
- answer fits in 32 bit integer

### **Edge cases**

---

- 12 → (1, 2), (12) → 2
- 101 → (10, 1) → 1
- 01 → 0
- 226 → (2, 2, 6), (22, 6) (2, 26)

### **Brute force approach (Discussion + Complexity)**

---

We have to find all possible ways to decode. it can be combination of the numbers in array. dfs can be a good way to start. we visit the first index and visit next index because single digits can be encoded (1 ~ 9 which encoded as A ~ I). There is another possibility on two digits which ranges between 10 to 26. let’s say if the next index value is 1, we could check out next index and if the next index value is between 0 to 9. if the next index value is 2, we could check out enxt index and if the next index valu is between 0 to 6 then we can add the 1 on current possible way of decode. Base case is when the current index is equals to the length of string. because it hits the last character in string. in this case we can simply return 1. it means we found the one way to decode (since we hit at the end of string)

The problem is number 0. if the current value is 0, there is no way we can decode except previous index was 1 or 2. 

node is index and return value is ways to decode. Time complexity is O(2^(length of input string)) (Since it is binary tree with left child (idx+1) and right child (idx+2)) and space complexity is O(1)

```java
public int numDecodings(String s) {
	return dfs(s, 0);	
}

private int dfs(String s, int idx) {
	if(idx == s.length()) return 1;

	char c = s.charAt(idx);
	if(c == '0') return 0;         // previous index already skip when next value is 0 (only when previous index value was 1 or 2)
	int way = dfs(s, idx + 1);
	if(idx + 1 < s.length() && (c == '1' || (c == '2' && s.charAt(idx + 1) <= '6'))) {
		way += dfs(s, idx + 2);
	}
	return way;
}
```

![80052E58-0727-46EA-931F-E19C41B84D34.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cebad307-3c23-4143-b139-e2965a94dcbe/80052E58-0727-46EA-931F-E19C41B84D34.jpeg)

### **Optimised approach (Discussion + Complexity) …X times**

---

Let’s optimize above solution with memoization. What we are looking for is way to decode which is `way` variable in the code. we can simply switch the variable with integer array. Time complexity is O(n) and space complexity is O(n)

```java
Integer[] dp;
public int numDecodings(String s) {
	dp = new Integer[s.length()];
	return dfs(s, 0);	
}

private int dfs(String s, int idx) {
	if(idx == s.length()) return 1;
	if(dp[idx] != null) return dp[idx];
	
	char c = s.charAt(idx);
	if(c == '0') return 0;         // previous index already skip when next value is 0 (only when previous index value was 1 or 2)
	dp[idx] = dfs(s, idx + 1);
	if(idx + 1 < s.length() && (c == '1' || (c == '2' && s.charAt(idx + 1) <= '6'))) {
		dp[idx] += dfs(s, idx + 2);
	}
	return dp[idx];
}
```

![2B732F45-6ED2-49FC-AFA3-230DBDF5104E.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/caf6b618-6234-4ed1-b6c1-a800db88786c/2B732F45-6ED2-49FC-AFA3-230DBDF5104E.jpeg)

### **Coding**

---

```java
class Solution {

    Integer[] dp;
    public int numDecodings(String s) {
        dp = new Integer[s.length()];
        return dfs(s, 0);	
    }

    private int dfs(String s, int idx) {
        if(idx == s.length()) return 1;
        if(dp[idx] != null) return dp[idx];

        char c = s.charAt(idx);
        if(c == '0') return 0; // previous index already skip when next value is 0 (only when previous index value was 1 or 2)
        
        dp[idx] = dfs(s, idx + 1);
        if(idx + 1 < s.length() && (c == '1' || (c == '2' && s.charAt(idx + 1) <= '6'))) {
            dp[idx] += dfs(s, idx + 2);
        }
        return dp[idx];
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/72dc8806-c7af-4960-b0b0-47d426ad8e9d/Untitled.png)

### What I learned

---

1. **index tree is more intutive than value tree in dynamic programming**

![2E071145-C92A-4F4E-98F4-202A7552C487.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e94b726b-33d6-4e4f-acf7-aaa24689d88a/2E071145-C92A-4F4E-98F4-202A7552C487.jpeg)

![CBFEBF63-59B9-4AFB-B658-A06170A1AB1B.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8b28c4bc-99ac-4c32-b9f9-9a239f25755c/CBFEBF63-59B9-4AFB-B658-A06170A1AB1B.jpeg)

1. most of base case in dynamic programming is `n.length()` which is null 

```java
private int dfs(String s, int i) {
    if(i == s.length()) return 1; // quickly return to prev node
    ...
    return r;
}
```

1. Each line of the if statement can affect other node

```java
private int dfs(String s, int i) {
    if(i == s.length()) return 1; // don't need care until i reach to the length
    
    char c = s.charAt(i);
    if(c == '0') return 0; // can be point in time        
    
    int r = dfs(s, i + 1); 
    if(i + 1 < s.length() && (c == '1' || c == '2' && s.charAt(i + 1) < '7')) { // can't be higher than length anyway
        r += dfs(s, i + 2);
    }

    return r;
}
```

1. We can skip indices not iterate one index at a time

```java
if(c == '0') return 0; // 2. so this can be skipped in below condition!

int r = dfs(s, i + 1); 
if(i + 1 < s.length() && (c == '1' || c == '2' && s.charAt(i + 1) < '7')) { // can't be higher than length anyway
    r += dfs(s, i + 2); // 1. we can skip the 0 value index
}
```
