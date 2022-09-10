---
title: "39.\_Combination Sum"
catalog: true
date: 2022-09-10 15:35:37
subtitle:
header-img:
tags:
---

## Conditions

---

- distinct integer array `candidate`
- return list of all unique combination of `candidates` numbers sum to target in any order
- The same number can be chosen from `candidates` an unlimited number of times
- Gauaranteed that the number of unique combinatios is less than 150

## Solve by hands first

---

We can start from Brute Force. Time complexity will be O(nm). If we use DFS algorithm, time complexity can be O(logN). **The combination question can be handled by DFS algorithm**.

how to store the history of the array values when sum is equals to target in DFS?

- create ArrayList and pass to method parameter
- add the value into list and remove it if it doesn’t meet condition by Backtracking

```java
for (int j = i; j < candi.length; j++) {
    // add value in list and take out current value
    sum += candi[j];
    list.add(candi[j]);

    dfs(j, target, candi, sum, list);
    
    // remove value in list and take out current value
    list.remove(list.size()-1);
    sum -= candi[j];
}
```

## Solution

---

```java
class Solution {
    
    List<List<Integer>> ans = new ArrayList();
    
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        for(int i = 0; i < candidates.length; i++) {
            dfs(i, target, candidates, 0, new ArrayList());
        }
        return ans;
    }
     
    void dfs(int i, int target, int[] candi, int sum, List<Integer> list) {
        
        if(target < sum) {
            return;
        }
        
        if(target == sum) {
            if(!ans.contains(list)) {
                ans.add(new ArrayList(list));    
            }
            return;
        }
        
        for (int j = i; j < candi.length; j++) {
            // add value in list and take out current value
            sum += candi[j];
            list.add(candi[j]);

            dfs(j, target, candi, sum, list);
            
            // remove value in list and take out current value
            list.remove(list.size()-1);
            sum -= candi[j];
        }
    } 
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/954e4c9f-17c6-4c35-bbf5-9afcf8873f11/Untitled.png)

**is there any way to make it faster?**

## Lesson I learnt

---

1. **Combination can be handle by DFS (or Backtracking If you needed)**
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/829439ef-0239-43db-baab-f3172d354d7c/Untitled.png)
    
2. **Backtracking helps to initialize the value before and after**
    
    ```java
    for (int j = i; j < candi.length; j++) {
        // add value in list and take out current value
        sum += candi[j];
        list.add(candi[j]);
    
        dfs(j, target, candi, sum, list);
        
        // remove value in list and take out current value
        list.remove(list.size()-1);
        sum -= candi[j];
    }
    ```
