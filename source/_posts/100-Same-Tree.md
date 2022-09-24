---
title: "100.\_Same Tree"
catalog: true
date: 2022-09-24 12:27:51
subtitle:
header-img:
tags:
---
## Conditions

---

- two binary trees `p` and `q`
- write a function to check if they are the same or not.
    - structurally identical
    - the nodes have the same value

## Solve by hands first

---

we need to check if both of tree are structurally identical first. it means that each node has identical child node at exact same poistion. for instance binary tree `p` only has left node then another binary tree `q` has to have left child node. 

second, we have to check if each node has same value. 

Time complexity is O(n)

![A04630F7-8261-49BD-9313-74502FA348E6.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7e49aa33-bdf2-41ad-b362-67de9f5f321e/A04630F7-8261-49BD-9313-74502FA348E6.jpeg)

## Solution

---

```java
class Solution {
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null) return true;
        if(p == null || q == null) return false;
        if(p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    } 
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5dc130f0-51d5-42e1-92b8-673b4ee15eec/Untitled.png)

## Lesson I learnt

---

1. when p and q both are null, if one of the p and q is null, **then each q and p has to be not null**. so I don’t need to check like below
    
    ```java
    // before
    if(p == null && q == null) return true;
    if(p == null) {
    	if(q != null) return false;
    }
    if(q == null) {
    	if(p != null) return false;
    }
    ```
    
    ```java
    // after
    if(p == null && q == null) return true;
    if(p == null || q == null) return false; 
    // when p is null then q have to be not null
    ```
