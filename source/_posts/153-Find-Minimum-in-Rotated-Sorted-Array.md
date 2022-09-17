---
title: "153.\_Find Minimum in Rotated Sorted Array"
catalog: true
date: 2022-09-17 18:47:29
subtitle:
header-img:
tags:
---
## Conditions

---

- array was originally sorted by ascending order
- rotate array between 1 to n times
- array has unique element
- return the minimum element of this array
- algorithm has to run in O(logN)

## Solve by hands first

---

In array problem, if the algorithm has to run in O(logN), we can come up with Binary search initially. If the array supposed to be sorted in ascending order and rotate it to the right then sub arrays from pivot index has minimum value or **just find minimum value with Binary search**.

![C4460C45-0244-485C-A8EA-9D400DCDF0BD.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/90b7b3e8-4ab2-4e1f-b740-cbf933601239/C4460C45-0244-485C-A8EA-9D400DCDF0BD.jpeg)

## Solution

---

```java
class Solution {
    public int findMin(int[] nums) {
        
        int l = 0, r = nums.length - 1;
        
        while(l < r) {
            int m = l + (r - l)/2;
            if(nums[m] > nums[r]) l = m + 1;
            else r = m;
        }
        
        return nums[r];
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3caae118-0fcc-4c6d-b4ee-cbd7b492a97e/Untitled.png)

## Lesson I learnt

---

1. **Choose the left or right index for answer**
2. **One of the left or right index has to be the same as middle index in equals if statement**

```java
while(l < r) {
    int m = l + (r - l)/2;
		if(nums[m] <= nums[r]) r = m;   // value at m and r is euqual, then r move to m
    else l = m + 1;
}
```
