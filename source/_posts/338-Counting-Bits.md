---
title: "338.\_Counting Bits"
catalog: true
date: 2022-10-20 20:34:25
subtitle:
header-img:
tags:
---
### **Requirements**

---

- `ans[i]` *is the **number of*** `1`***'s** in the binary representation of* `i`
- each element of ans arrays is the number of 1’s in the binary form of i. For instance i = 4 which is 100 in binary representation. So, ans[4] = 1 since there is only one 1 in 100.

### **Edge cases**

---

- 1 → 0 → [0]
- 2 → [1, 2] → [0, 10] → [0, 1]
- 3 → [1, 2, 3] → [0, 10, 11] → [0, 1, 2]
- 4 → [1, 2, 3, 4] → [0, 10, 11, 100] → [0, 1, 2, 1]
- 11 → [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11] → [0, 10, 11, 100, 101, 110, 111, 1000, 1001, 1010] → [0, 1, 2, 1, 2, 2, 3, 1, 2, 2]
- pattern
    - 1 : 1 → 1
    - 2 : 10 11 → 1, 2
    - 4 : 100 101 110 111 → 1, 2, 2, 3
    - 8 : 1000 1001 1010 1011 1100 1101 1110 1111 → 1 2 2 3 2 3 3 4
    - 16 : 10000

### **Brute force approach (Discussion + Complexity)**

---

we can loop through 1 to n. then count the number of i in each iteration. How to count the number of 1’s with number. we can use right shift and & operation. right shift each number and & operation with 1. If the binary is 0, it will return 0, otherwise return 1. Time complexity is O(nlog(n)) (Because shift operation is basically division equation. so n is divided by 2 every loop)

```java
public int[] countBits(int n) {
    int[] ans = new int[n+1];
    for(int i = 0; i <= n; i++) {
        ans[i] = count(i);
    }
    return ans;
}

private int count(int n) {
    int count = 0;
    while(n != 0) {
        if((n & 1) == 1) count++;
        n = n >> 1;
    }
    return count;
}
```

### **Optimised approach (Discussion + Complexity) …X times**

---

To optimize this solution we can find the pattern of bits. It always add 1 on significient bit and bits below have same patterns in preivous bits. Time complexity is O(n) and space complexity is O(n)

```java
0 : 0000 -> 0
--------
1 : 0001 -> 1 + dp[0] -> 1 + dp[1-1]
--------
2 : 0010 -> 1 + dp[0] -> 1 + dp[2-2]
3 : 0011 -> 1 + dp[1] -> 1 + dp[3-2]
--------
4 : 0100 -> 1 + dp[0] -> 1 + dp[4-4]
5 : 0101 -> 1 + dp[1] -> 1 + dp[5-4]
6 : 0110 -> 1 + dp[2] -> 1 + dp[6-4]
7 : 0111 -> 1 + dp[3] -> 1 + dp[7-4]
--------
8 : 1000 -> 1 + dp[0] -> 1 + dp[8-8]
9 : 1001 -> 1 + dp[1] -> 1 + dp[9-8]
--------
dp[n] = 1 + dp[n - significient bit]
```

### **Coding**

---

```java
class Solution {
    public int[] countBits(int n) {
        int[] ans = new int[n+1];
        int sig = 1;
        for(int i = 1; i <= n; i++) {
            if(sig * 2 == i) sig = i;
            ans[i] = 1 + ans[i - sig];
        }
        return ans;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9c54dc06-bffd-4f2c-aa9e-dd44da6489cd/Untitled.png)

### What I learned

---

1. significient bit = 1, 2, 4, 8 ..
2. show the binary representation with 4 bits
3. bits has patterns
4. find the significient bits. it gradually increases twice.

```java
int sig = 1;
for(int i = 1; i <= n; i++) {
	if(sig * 2 == i) sig = i;
}
```
