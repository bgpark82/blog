---
title: "11.\_Container With Most Water"
catalog: true
date: 2022-10-11 18:51:17
subtitle:
header-img:
tags:
---
## Conditions

---

- Find two lines contains the most water
- Return *the maximum amount of water a container can store*
.

## Solve by hands first

---

The goal is to find the two lines containing the most water. **Formular to calculate the max water is max height * max width**. so **we can start from max width which is index 0 to length - 1**. then calculate the water. move the left or right index from the smaller height line to heigher. Time complexity is O(n)

## Solution

---

```java
class Solution {
    
    public int maxArea(int[] height) {
        int l = 0, r = height.length - 1, max = 0;
        while(l < r) {
            max = Math.max(Math.min(height[l], height[r]) * (r - l), max);
            if(height[l] < height[r]) l++;
            else r--;
        }
        return max;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9bce7f1d-8c66-4182-b3d8-e1a9cfdb9884/Untitled.png)

## Lesson I learnt

---
