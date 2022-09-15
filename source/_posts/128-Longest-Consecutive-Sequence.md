---
title: "128.\_Longest Consecutive Sequence"
catalog: true
date: 2022-09-15 19:46:45
subtitle:
header-img:
tags:
---
## Conditions

---

- You must write an algorithm that runs in `O(n)` time.
- -109 <= nums[i] <= 109

## Solve by hands first

---

we can think of Brute Force way but Its time complexity will be O(m*m) which doesn’t follow condition. Instead I can use PriorityQueue data structure to sort the array. Time complexity on enque is O(logN) which is less than O(N).

## Solution

---

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        if(nums.length == 0) return 0;
        
        PriorityQueue<Integer> pq = new PriorityQueue();
        for(int n : nums) {
            pq.add(n);
        }
        
        int count = 1, max = 1;
        int tmp = pq.poll();
        while(!pq.isEmpty()) {    
            int n = pq.poll(); 
            
            if(n - tmp == 1) max = Math.max(max, ++count); 
            else if(n == tmp) continue;
            else count = 1;
            
            tmp = n;
        }
        
        return max;
        
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/714cdd3b-506a-4482-8f9d-2de81a3732ee/Untitled.png)

## Lesson I learnt

---

1. **Time complexity of enque and deque is O(logN)**
