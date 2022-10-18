---
title: "121.\_Best Time to Buy and Sell Stock"
catalog: true
date: 2022-10-18 20:49:31
subtitle:
header-img:
tags:
---
### **Requirements**

---

- prices[i] is the price of a given stock on the ith day
- maximize the profit by **choosing a single day to buy one stock** and **choosing a different day in the future to sell that stock**
- if you can’t not achieve any profit return 0
- given days are between 1 and 10^5 days
- price is between 0 to 10^4
- order matters in this case which means buy the stock at the minimum and cell it at maximum
- **elements are distinct in array?**

### **Edge cases**

---

- [1, 2, 3] → 2 = 3 - 1
- [3, 2, 1] → 0
- [] → 0

### **Brute force approach (Discussion + Complexity)**

---

Starting from left index at index 0 and compare with the right index which is next index of left index. if the value of left index is smaller than equals to value of right index. Find the take out of both value and store as max. After whole iteration, return max value

Time complexity is O(lenth of array * length of array) which is O(n^2). Space complexity is O(1)

```java
int max = 0;
for(int i = 0; i < nums.length; i++) {
	for(int j = i+1; j < nums.length; j++) {
		if(nums[i] <= nums[j]) {
			max = Math.max(max, nums[j] - nums[i]);
		}
	}
}
return max;

```

### **Optimised approach (Discussion + Complexity) …X times**

---

We can optimize the time complexity to O(n) with **Two Pointer**. We need two pointer from index 0 and the last index. Compare the value of those value of indices, If left value is greater than right value, then move left pointer to right. if left value is less than equals to right value, store it as max and move the right pointer to left. it keeps on going until left index is greater than right index. if we can’t meet any condition, then it will return 0 as max

```java
int max = 0, l = 0, r = prices.length-1;
while(l <= r) {
	if(prices[l] > prices[r]) l++;
	else {
		max = Math.max(max, prices[r] - prices[l]);
		r--;
	}
}
return max;
```

Problem of the approach is that in case of [2, 1, 4], since 4 is greater than 2, so right index move to left but left index has to move to get maximum profit. so, instead of putting the right index from the last index, we can simply start off left and right index from 0 index.

Move to right index one at a time. If left pointer value is less than right pointer value which makes positive profit, compare the previous maximum and current profit and choose the larger value. otherwise move the left pointer to the right pointer because **right pointer is currently less than left pointer which pointing to the minimum value**. Time complexity is O(length of input array)

```java
int max = 0, l = 0, r = 0;
while(r < prices.length) {
	if(prices[l] < prices[r]) max = Math.max(max, prices[r] - prices[l]);
	else l = r;
	r++;
}
return max;
```

![206A8703-105F-4E47-890C-07CEB6CE7FE3.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/23ffcb0b-fe1d-416f-8a05-5930fdf2d7db/206A8703-105F-4E47-890C-07CEB6CE7FE3.jpeg)

### **Coding**

---

```java
class Solution {
    public int maxProfit(int[] prices) {
        int max = 0, l = 0, r = 0;
        while(r <th) {
            if(prices[l] < prices[r]) max = Math.max(max, prices[r] - prices[l]);
            else l = r;
            r++;
        }
        return max;
    }
}
``` 

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7be58ed8-3a62-4e83-9244-741848f1cc06/Untitled.png)

### What I learned

---

1. [Kadane's Algorithm](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39038/Kadane's-Algorithm-Since-no-one-has-mentioned-about-this-so-far-%3A)-(In-case-if-interviewer-twists-the-input))
    - it’s the algorithm comparing the current max value and global max value

```java
public int maxProfit(int[] prices) {
    int maxCur = 0, maxSoFar = 0;
    for(int i = 1; i < prices.length; i++) {
        maxCur = Math.max(0, maxCur += prices[i] - prices[i-1]);
        maxSoFar = Math.max(maxCur, maxSoFar);
    }
    return maxSoFar;
}
```

1. Two pointer can be from 0 to last index or start from 0 at the same time
    - **left = 0, right = n.length**
    - **left = 0, right = 0**
2. Two pointer is a good solution for Array problem
