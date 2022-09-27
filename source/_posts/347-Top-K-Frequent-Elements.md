---
title: "347.\_Top K Frequent Elements"
catalog: true
date: 2022-09-27 20:43:30
subtitle:
header-img:
tags:
---
## Conditions

---

- return *the* `k` *most frequent elements*
- return the answer in **any order**
- It is **guaranteed** that the answer is **unique**.
- Your algorithm's time complexity must be better than `O(n log n)`, where n is the array's size.

## Solve by hands first

---

we can start with **Map**. iterate the number array and count the each value. store value as key and count as value in Map. Increase from 1 to input k and find the count of value at every increament. if we can sort by value, first element of map willl be the most frequent element. Time complexity is O(n) and space complexity is O(1)

we can use either **PriorityQueue** or TreeMap to sort by value yet still have to keep the key and value pair. since we use the Map, it’s easy to utilize the Map.Entry which has key and value

problem of using TreeMap is that we can order element by key but has to be unique. in this question, there is the duplicated most frequent element allowed. for instance [1,1,3,3] in this case. 1 and 3 both appears twice. so we can’t keep the both 1, 3 for key 2

## Solution

---

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap();
        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> (b.getValue() - a.getValue()));
        ArrayList<Integer> ans = new ArrayList();
        
        for(int n : nums) {
            m.put(n, m.getOrDefault(n, 0) + 1);
        }
        
        for(Map.Entry<Integer, Integer> e : m.entrySet()) {
            pq.add(e);
        }
        
        while(k-- > 0) {
            ans.add(pq.poll().getKey());
        }
        
        return ans.stream().mapToInt(i -> i).toArray();
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/18e4a223-0b22-4719-ac9d-7f60430fd9ba/Untitled.png)

## Lesson I learnt

---

1. need to have more understanding of TreeMap and PriorityQueue in java
2. PriorityQueue with Entry order by value
    - Map.Entry is class only has key and value as
    - if we want to order PriorityQueue by value in Map.Entry, **we can pass Comparable with lambda form to constructor parameter**
    
    ```java
    PriorityQueue<Map.Entry<Integer, Integer>> pq = 
    		new PriorityQueue<> (b.getValue() - a.getValue()));
    
    ```>((a, b) -
