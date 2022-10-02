---
title: "152.\_Maximum Product Subarray"
catalog: true
date: 2022-10-02 19:53:37
subtitle:
header-img:
tags:
---
## Conditions

---

- find a **contiguous non-empty subarray** within the array that has **the largest product**
- and return *the product*
- the answer will fit in a **32-bit** integer.

## Solve by hands first

---

we can start with brute force. but then its time complexity will be O(n*n)

another option can be dynamic programming. Time complexity is O(n)

![379A741F-7B28-445A-A702-04CCF520CAF7.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a32b6443-e355-4ca5-a503-4c1e1dc63e96/379A741F-7B28-445A-A702-04CCF520CAF7.jpeg)

we have to consider two different cases, max and min. since there are positive and negative element in array, product can be positive or negative. 

1. maximum
2. minimum

we can seperate maximum and minimum cases

1. positive
2. negative 
    - if previous calculation was positive
        - after calculation with negative value will be minimum
        - negative value itself will be maximum
    - if previous calculation was negative
        - after calculation with negative value will be maximum
        - negative value itself will be minimum
        

compare current maximum value and previous maximum value

## Solution

---

```java
class Solution {
    public int maxProduct(int[] nums) {
        int ans = Integer.MIN_VALUE;
        for(int num : nums) {
            ans = Math.max(ans, num);
        }
        int max = 1, min = 1;
        for(int n : nums) {
            if(n == 0) {
                max = 1;
                min = 1;
                continue;
            }
            int nmax = max * n;
            int nmin = min * n;
            max = Math.max(Math.max(nmax, nmin), n);
            min = Math.min(Math.min(nmax, nmin), n);
            ans = Math.max(max, ans);
        }
        return ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4605588c-5865-4608-b674-114838d38486/Untitled.png)

## Lesson I learnt

---
