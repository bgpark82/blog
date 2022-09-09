---
title: "33.\_Search in Rotated Sorted Array"
catalog: true
date: 2022-09-09 14:24:29
subtitle:
header-img:
tags:
---

## Conditions

---

1. Time Complexity is O(logN)
2. `*nums*` is sorted in ascending order다
3. `*nums*` is with distinct values

What we can get from conditions is that it is binary search question since it has to take only O(logN) to search the target value in array

## Solve by hands first

---

to use the binary search algorithm, **arrays need to be sorted**. but the given nums is rotated at certain pivot index which means the sub array of nums is in ascneding order. so if we are able to find the pivot index, we could use the binary search algorithm to find the target value. but in the condition, the whole time complexity needs to be O(logN), we also have to use the binary search to find the pivot index value

## Solution

---

```java
class Solution {
    public int search(int[] nums, int target) {
        if(nums.length == 0 || nums == null) return -1;
        
        // 1. find the pivot index with the binary search
        int l = 0;
        int r = nums.length-1;
        
        while(l < r) {
            int m = l + (r - l)/2;
            if(nums[m] > nums[r]) { // 1 > 3  r = 2 -> 0
                l = m + 1;
            } else {
                r = m;
            }
        }
        
        // 2. select the sub array which has target value and reset the left and right index of the sub array
        int s = l;
        l = 0;
        r = nums.length-1;

        if(target >= nums[s] && target <= nums[r]) {
            l = s;
        } else {
            r = s;
        }
        
        // 3. search the target index in sub array
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

        return -1;
    }
}
```

## Lesson I learnt

---

1. setting right index 
2. mid point in even length of array
    - in java, **the actual number will be truncated in *`int`* data type**
    
    ```sql
    [1,2,3,4]
    
    int left = 0;
    int right = 3;
    int mid = left + (right - left) / 2;
            = 0 + (3 - 0)/2
            = 0 + 1.5
            = 1
    ```
    
3. condition in while loop 
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
