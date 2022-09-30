---
title: "238.\_Product of Array Except Self"
catalog: true
date: 2022-09-30 20:33:02
subtitle:
header-img:
tags:
---
## Conditions

---

- `answer[i]` *is equal to the product of all the elements of* `nums` *except* `nums[i]`
- The product of any prefix or suffix of `nums` is **guaranteed** to fit in a **32-bit** integer.
- You must write an algorithm that runs in `O(n)` time and without using the division operation.

## Solve by hands first

---

it can be simple math problem. create product of all elements of array and divide them in each element. 

but how do it handle 0?

Another option is dynamic programming. each element has to know product of previous element from current index. calculate product of left element and calulate right element again.

## Solution

---

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];
        
        ans[0] = 1;
        for(int i = 1; i < len; i++) {
            ans[i] = ans[i - 1] * nums[i - 1];
        }
        
        int pr = 1;
        for(int i = len - 1; i >= 0; i--) {
            ans[i] *= pr;
            pr *= nums[i];
        }
        
        return ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/65d3250b-c5d0-4ca4-87c7-6c1d8d23b4ef/Untitled.png)

![A99A29B8-77DD-4AFE-BCFB-54C35C0C3D02.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d97af286-acbb-41be-a466-f1dfbe27ba97/A99A29B8-77DD-4AFE-BCFB-54C35C0C3D02.jpeg)

## Lesson I learnt

---

- product = multiplication
