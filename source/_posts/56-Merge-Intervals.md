---
title: "56.\_Merge Intervals"
catalog: true
date: 2022-10-07 23:32:18
subtitle:
header-img:
tags:
---
## Conditions

---

- merge all overlapping intervals
- return *an array of the non-overlapping intervals that cover all the intervals in the input*

## Solve by hands first

---

we can sort the array by the start. it makes easy to find the overlappig intervals. we compare the first and next interval, if start of next interval is less than equals to first interval, then we can merge those two intervals. otherwise add the merged interval to list and update the start and end point.

The problem is when to merge 

```java
int s = intervals[0][0], e = intervals[0][1];
for(int[] interval : intervals) {
    int ns = interval[0];
    int ne = interval[1];
    
		// next start point is less than equals to first end point
		// then choose whichever larger end from first or next interval
    if(ns <= e) {
        e = Math.max(e,ne);
    } 
}
```

![9F363ABE-7C34-4109-89C5-DF2BFE5DD8F1.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/62e02e15-07cd-4802-b91b-104c9443c7ff/9F363ABE-7C34-4109-89C5-DF2BFE5DD8F1.jpeg)

Another problem is when it will added in the list. add start and end when it’s not overlapped. it always leaves the last interval so we need to add it after finishing loop

```java
int s = intervals[0][0], e = intervals[0][1];
for(int[] interval : intervals) {
    int ns = interval[0];
    int ne = interval[1];
    
    if(ns <= e) {
        e = Math.max(e,ne);
    } else {
        ans.add(new int[]{s, e});
        s = ns;
        e = ne;
    }
}
ans.add(new int[]{s, e});
```

Time complexity is O(length of intervals)

## Solution

---

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        List<int[]> ans = new ArrayList();
        
        int s = intervals[0][0], e = intervals[0][1];
        for(int[] interval : intervals) {
            int ns = interval[0];
            int ne = interval[1];
            
            if(ns <= e) {
                e = Math.max(e,ne);
            } else {
                ans.add(new int[]{s, e});
                s = ns;
                e = ne;
            }
        }
        ans.add(new int[]{s, e});

        return ans.toArray(new int[ans.size()][]);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9a404349-936f-4907-b237-b9719588f132/Untitled.png)

## Lesson I learnt

---

1. List to array
    
    ```java
    list.toArray(new int[list.size()][]); 
    ```
