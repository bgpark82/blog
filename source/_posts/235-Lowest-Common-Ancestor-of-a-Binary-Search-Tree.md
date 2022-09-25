---
title: "235.\_Lowest Common Ancestor of a Binary Search Tree"
catalog: true
date: 2022-09-25 22:52:27
subtitle:
header-img:
tags:
---
## Conditions

---

- Given a binary search tree (BST)
- find the lowest common ancestor (LCA) node of two given nodes
- The lowest common ancestor is defined between two nodes `p` and `q` as the lowest node in `T` that has both `p` and `q` as descendants
- we allow **a node to be a descendant of itself**
- All `Node.val` are **unique**.
- p != q

## Solve by hands first

---

both min, max value can be less or greater than current node’s value. then how do we split to left and right child node;

## Solution

---

```java
class Solution {
    
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        return dfs(root, Math.min(p.val, q.val), Math.max(p.val, q.val));
    }
    
    private TreeNode dfs(TreeNode root, int min, int max) {
        if(root == null) return null;
        if(root.val < min && root.val < max) return dfs(root.right,  min,  max);
        else if(root.val > min && root.val > max) return dfs(root.left,  min,  max);
        else return root;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9c29bf31-5abc-46b4-ba09-49ad38c69e6f/Untitled.png)

```java
class Solution {
    
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while(true) {
            if(root.val < p.val && root.val < q.val) root = root.right;
            else if(root.val > p.val && root.val > q.val) root = root.left;
            else return root;
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b1448a13-4070-48a4-b6e1-0cb7a6657fd1/Untitled.png)

**if the value of node is greater than minimun and less than maximum, the node is the lowest common ancester**. otherwise it has to travel left or right until find the common ancester

**it’s the question that you know the character/pattern of BST or not**. 

## Lesson I learnt

---

1. change input example with the given inputs
2. I need to fully solve by hand first and the implement code by it! (it’s very important since I can review my thought afterall)
3. use the `else if` and `else` statement activly when handle edge case
    
    ```java
    if(root.val < p.val && root.val < q.val) root = root.right; // root.val is less than both min, max
    else if(root.val > p.val && root.val  q.val) root = root.left; // root.val is greater than both min, max 
    else return root;
    ```
    
4. **when split occur at certain node. that node will be the lowest common ancestor**
    - for instance with given 2, 8. parent node 6 is greater than 2 and less than 8 which means it has to be splited.
    - so whatever value is greater than minimun and less than maximum can be the lowest common ancester
    - it doesn’t really matters how many child node it has to travel after
    
    ```java
         6   
       /   \
      2     8
             \ 
              10 
    ```
    
    ```java
    2 < 6 < 8
    2 < 6 < 10
    ```>
