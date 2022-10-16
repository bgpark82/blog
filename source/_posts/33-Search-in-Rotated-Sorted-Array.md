---
title: "33.\_Search in Rotated Sorted Array"
catalog: true
date: 2022-09-09 14:24:29
subtitle:
header-img:
tags:
---
### **Requirements**

---

- integer array `nums` sorted in ascending order (with **distinct** values).
- **possibly rotated** at an unknown pivot index `k`
- return *the index of* `target` *if it is in* `nums`*, or* `-1` *if it is not in* `nums`
- You must write an algorithm with `O(log n)` runtime complexity.

### **Edge cases**

---

- [4,5,6,7,0,1,2], 0 → 4 (exist)
- [4,5,6,7,0,1,2], 3 → -1 (non-exist)
- [0], 1 → -1 (non-exist)

### **Brute force approach (Discussion + Complexity)**

---

we can’t use the brute force way because the quired runtime complexity is O(logn). binary search can be a good start for this problem since the array is already sorted.

### **Optimised approach (Discussion + Complexity) …X times**

---

we can start with finding pivot index. Because left and right sub arrays are already sorted from the pivot. it makes easy to find the target value by using the binary search. so total time complexity is O(logn) + O(logn) + O(logn) = O(logn)

### **Coding**

---

```java
class Solution {
    public int search(int[] nums, int target) {
        
        // nums = [4,5,6,7,0,1,2], target = 0
        //         l     m     r            
        // nums = [6,7,0,1,2,4,5], target = 0
        //         l     m     r            
        //         l m r
        //             l
        int l = 0, r = nums.length - 1;
        while(l < r) {
            int m = (l + r) / 2;
            if(nums[m] > nums[r]) l = m + 1;
            else r = m;
        }
        int p = l;

        // left sub array
        l = 0; r = p - 1;
        while(l <= r) {
            int m = (l + r) / 2;
            if(nums[m] == target) return m;
            else if(nums[m] > target) r = m - 1;
            else l = m + 1;
        }

        // right sub array
        l = p; r = nums.length - 1;
        while(l <= r) {
            int m = (l + r) / 2;
            if(nums[m] == target) return m;
            else if(nums[m] > target) r = m - 1;
            else l = m + 1;
        }
        
        return -1;
        
    }
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0147f6f2-df70-465f-b244-d9df82f34edf/Untitled.png)

### What I learned

---

1. Don’t forget the equals comparison in a while loop to find the target

```java
while(l <= r)
```

1. mid point in even length of array
    - in java, **the actual number will be truncated in *`int`* data type**

```sql
[1,2,3,4]

int left = 0;
int right = 3;
int mid = left + (right - left) / 2;
        = 0 + (3 - 0)/2
        = 0 + 1.5
        = 1
int mid = (left + right) / 2;
```

1. condition in while loop 
    - In binary search, condition in while loop has to be `*left ≤ right*`, **if size of the array is one, then it happen to compare the same index**
    - **The left and right index updated with +1 and -1** in order to meet the *`left ≤ right`* condition. otherwise it will stuck in while loop at certain point in time. for instance left = 1, right = 1
    - ex) array = [1], left = 0, right = 0 → when left < right in while condition, it can’t go into the while loop since it does not meet the condition

```java
while(l <= r) {
    int m = l + (r - l)/2;
    if(nums[m] == target) {
        return m;
    } else if (nums[m] < target) {
        l = m + 1;
    } else {
        r = m - 1;
    }
}
```
