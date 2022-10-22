---
title: "53.\_Maximum Subarray"
catalog: true
date: 2022-10-22 16:14:28
subtitle:
header-img:
tags:
---
### **Requirements**

---

- find the contiguous subarray (at least contain 1 value) which has the largest sum
- return the sum
- num is between -10^4 and 10^4 which means contain negative value and 0

### **Edge cases**

---

- [1, 0, 2, -1, 3, 5, -3]
    - [1, 0, 2, -1, 3, 5, -3] → 7
    - [1, 0, 2, -1, 3, 5] → 10

### **Brute force approach (Discussion + Complexity)**

---

We can start with Brute force way. Iterate each index and calculate the sum of contiguous subarrays. find the maximum value out of that. Time complexity is O(n^2) and space complexity is O(1)

```java
int max = nums[0];
for(int i = 0; i < nums.length; i++) {
    int sum = nums[i];
    max = Math.max(max, sum);
    for(int j = i+1; j < nums.length; j++) {
        sum += nums[j];
        max = Math.max(max, sum);
    }
}
return max;
```

### **Optimised approach (Discussion + Complexity) …X times**

---

We can optimize above solution to O(n) with sliding window. The tip is the contiguous subarray contains the largest sum and the range of the value in array contains negative value. it means we don’t need any negative value to make the largest sum. So, when sum of the contiguous subarray becomes negative which means can’t be the largest sum. we drop the current subarray and reset the contiguous subarray starting from current index. Time complexity is O(n) and space complexity is O(1);

```java
int max = nums[0];
int sum = 0;
for(int n : nums) {
	if(sum < 0) sum = 0;
	sum += n;
	max = Math.max(max, sum);
}
return max;

```

![C18E9EEF-7299-45BE-9BBD-D3774F20B083.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cf2aafb1-1772-4a95-9a6e-c808e24899b7/C18E9EEF-7299-45BE-9BBD-D3774F20B083.jpeg)

### **Coding**

---

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int sum = 0;
        for(int n : nums) {
            if(sum < 0) sum = 0;
            sum += n;
            max = Math.max(max, sum);
        }
        return max;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/80c55d1c-b08f-4a3e-b74e-fbb5c18de642/Untitled.png)

### What I learned

---

1. Sliding window is a good solution in array problem
2. Find the core logic or patterns in the problem. carefully read the problem and find the key. in this problem, the key was the **contiguous** **subarray** and **largest sum**.
