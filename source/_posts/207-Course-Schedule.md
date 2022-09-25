---
title: "207.\_Course Schedule"
catalog: true
date: 2022-09-25 15:43:46
subtitle:
header-img:
tags:
---
## Conditions

---

- Return `true` if you can finish all courses. Otherwise, return `false`
- All the pairs prerequisites[i] are **unique**.

## Solve by hands first

---

we should check the prerequisites are in the course first. if it’s existed, then we can take a course. false case is when it has cycle in between prerequisities course and the course wants to take. if there is cycle then return false otherwise return true. **how do I express the cycle with algorithm**? Graph! we can use DFS algorithm to search the Graph

Problem is how we can detect the cycle in graph? 

## Solution

---

```java
class Solution {
    
    Map<Integer, List<Integer>> m;
    Set<Integer> visited;
    
    public boolean canFinish(int nc, int[][] prerequisites) {
        
        m = new HashMap();
        visited = new HashSet();
        
        for(int i = 0; i < nc; i++) {
            m.put(i, new ArrayList());
        }
        
        for(int[] pre : prerequisites) {
            List<Integer> l = m.get(pre[0]);
            l.add(pre[1]);
            m.put(pre[0], l);
        }
        
        for(int i = 0; i < nc; i++) {
            if(!dfs(i)) return false;
        }
        
        return true;
    }
    
    private boolean dfs(int k) {
        if(visited.contains(k)) return false;
        if(m.get(k) == null || m.get(k).isEmpty()) return true;
        
        visited.add(k);
        for(int l : m.get(k)) {
            if(!dfs(l)) return false;
        }
        visited.remove(k);
        m.put(k, new ArrayList());
        return true;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8da69891-2aca-4e68-b46b-2410a32d5406/Untitled.png)

1. **Problem was how to detect loop in graph**. 
    - solution is **Set**
    - every iteration**, insert the current node into Set and after checking all of the children node then remove the current node from Set**.
    - **when revisit the node, if it’s in the Set which mean it’s cycle**
2. **Another problem is how to check if the node has no cycle**
    - we can remove the childrent nodes after visiting all the childrent nodes
    - **if there is emtpy child node which mean the node won’t be able to make loop in Graph**

![31F9E96C-D597-4B0D-9EB7-2B3BF30995AF.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7558abdc-3248-4299-8f5d-79b10bf67f2a/31F9E96C-D597-4B0D-9EB7-2B3BF30995AF.jpeg)

![9679588C-CCBB-46E0-AC71-92CB4B3F1E90.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c4d854ce-457f-4842-87a6-5bebfcb0c136/9679588C-CCBB-46E0-AC71-92CB4B3F1E90.jpeg)

## Lesson I learnt

---

1. BFS, DFS (Backtracking)  is the way of searching in Tree, Graph
2. Graph or Tree can be Map with node as key and list of child nodes as value! it doesn’t actually need to be 
3. to detect the loop in graph, we can use **Set.**
    - **add the node in Set** when entry the node
    - **remove the node in Set** after visiting childrent nodes
