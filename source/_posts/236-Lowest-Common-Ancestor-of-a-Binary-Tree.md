---
title: "236.\_Lowest Common Ancestor of a Binary Tree"
catalog: true
date: 2022-10-01 11:54:55
subtitle:
header-img:
tags:
---
## Conditions

---

- binary tree
- find the lowest common ancestor (LCA) of two given nodes in the tree.
- he lowest common ancestor is defined between two nodes `p` and `q` as the lowest node in `T` that has both `p` and `q` as descendants

## Solve by hands first

---

Unlike lowest common ancestor of binary search tree question, we have to visit both of left and right childrent anyway. if we find the node then return otherwise should compare visited left and right node. there are two caes can be the lowest common ancestors

1. both of child nodes are not null
2. one of child is not null

![1F161E1D-86B1-4D4F-A879-F48C16FE3DB0.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6a3e6f65-4d61-4253-968f-d4e2700b59a4/1F161E1D-86B1-4D4F-A879-F48C16FE3DB0.jpeg)

## Solution

---

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        
        if(root == null) return null;
        if(root == p || root == q) return root;
        
        TreeNode ln = lowestCommonAncestor(root.left, p, q);
        TreeNode rn = lowestCommonAncestor(root.right, p, q);

        if(ln != null && rn != null) return root;
        return ln != null ? ln : rn;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/348363f5-19b4-44a8-9dc9-2c95fee12fc6/Untitled.png)

## Lesson I learnt

---

1. read the code carefully
2. compare with reference of node
    
    ```java
    root == p
    ```
    
3. three part of tree traversal. **every things happens in the current node!**
    1. enter the current node
    2. visit child nodes
    3. come back from child node
    
    ```java
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    		
    		// 1. enter the current root      
        if(root == null) return null;
        if(root == p || root == q) return root;
        
    		// 2. visit child nodes
        TreeNode ln = lowestCommonAncestor(root.left, p, q);
        TreeNode rn = lowestCommonAncestor(root.right, p, q);
    
    		// 3. come back from child nodes
        if(ln != null && rn != null) return root;
        return ln != null ? ln : rn;
    }
    ```
