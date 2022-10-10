---
title: "1.\_Two Sum"
catalog: true
date: 2022-10-10 23:20:53
subtitle:
header-img:
tags:
---
## Conditions

---

- return *indices of the two numbers such that they add up to `target`*
- you may not use the *same* element twice.
- You can return the answer in any order

## Solve by hands first

---

we can use HashMap to match the value and target - value. Map contains element as key and index as value. Time complexity is O(n) and space complexity is O(n)

```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> m = new HashMap();
    for(int i = 0; i < nums.length; i++) {
        m.put(nums[i], i);
    }
    for(int i = 0; i < nums.length; i++) {
        if(m.containsKey(target - nums[i])) {
            return new int[]{m.get(target - nums[i]), i};
        }
    }
    return null;
}
```

Problem of this solution is there is chance to use the same element twice. for instance if nums is [3, 2, 4] and target is 6, it will use the 3 twice which meet the constraint. 

## Solution

---

```java
class Solution {
    
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap();
        for(int i = 0; i < nums.length; i++) {
            if(m.containsKey(target - nums[i])) {
                return new int[]{m.get(target - nums[i]), i};
            }
            m.put(nums[i], i);
        }
        return null;
		}
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/386ea77a-7fa5-41cd-9de4-94d880758a2b/Untitled.png)

## Lesson I learnt

---

1. HashMap is useful when it comes to match two values

```java
nums   2 7 11 15
target 9
----------------
key 2 7 11 15
val 0 1 2  3
----------------
9 - 2 = 7
9 - 7 = 2

```
