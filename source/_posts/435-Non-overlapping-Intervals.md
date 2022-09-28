---
title: "435.\_Non-overlapping Intervals"
catalog: true
date: 2022-09-28 20:50:15
subtitle:
header-img:
tags:
---
## Conditions

---

- return *the **minimum number** of intervals you need to remove to make the rest of the intervals non-overlapping*

## Solve by hands first

---

There are 3 steps to solve this problem

1. sort array by the start point
2. if current start point is greater than equals to previous end point, it’s not overlapped
3. if it’s not, those intervals are overlapped
    1. increase count
    2. set previous end point to smaller end point from current or previous end point (Greedy)

the key is setting **previous end point to smaller value of current or previous end point**. which is greedy approach. if end point is smaller, there is higher chance to be less overlapped

Time complexity is O(nlogN) which is for sorting array

## Solution

---

```java
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        
        Arrays.sort(intervals, (i, j) -> i[0] - j[0]);
        
        int count = 0, pe = intervals[0][1];
        for(int i = 1; i < intervals.length; i++) {
            int s = intervals[i][0];
            int e = intervals[i][1];
            
            if(s >= pe) pe = e;
            else {
                count++;
                pe = Math.min(pe, e);
            }
        }
        
        return count;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/225f1bd2-8318-4d06-899e-da602c6eae67/Untitled.png)

## Lesson I learnt

---

1. drawing a picture for the complicated problem
2. Greedy is the way that choose the best answer in every sub problem
