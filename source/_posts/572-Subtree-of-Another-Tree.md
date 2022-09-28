---
title: "572.\_Subtree of Another Tree"
catalog: true
date: 2022-09-28 10:17:44
subtitle:
header-img:
tags:
---
## Conditions

---

- two **binary trees** `root` and `subRoot`
- return `true` if there is a subtree of `root` with the same structure and node values of `subRoot`

## Solve by hands first

---

we can start with DFS first. one we find the value of subRoot in root Tree, then move to the same direction of child node for both of subRoot and root tree. once complete traverse subRoot tree then return true if root tree has the same subtree otherwise false

One of the constraint that I couldn’t notice is tree does not have child node in terms of the same structure. so it can be easier to return condition at DFS algorithm

![FE9CC412-5EEC-45B2-9259-794EAD021E8D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a33c168f-daaf-4e31-b112-466585656805/FE9CC412-5EEC-45B2-9259-794EAD021E8D.jpeg)

## Solution

---

```java
class Solution {
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if(root == null) return false;
 
        if(isSame(root, subRoot)) return true;
        
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }
    
    private boolean isSame(TreeNode root, TreeNode subRoot) {
        if(root == null && subRoot == null) return true;
        if(root == null || subRoot == null) return false;
        
        if(root.val != subRoot.val) return false;

        return isSame(root.left, subRoot.left) && isSame(root.right, subRoot.right);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1a763a04-c2e4-4b79-a4a2-d2475f472a65/Untitled.png)

There are two ways of traversal

1. traverse binary tree until last node (left or right)
2. traverse the same structured and value node until both of node are null (left or right)

first case is that we need to traverse left and right node until the last node. in the meantime we have to check if the value of root and subRoot is the same or not. if it is true, then we start traverse to see if both of tree have the same structure and value

```java
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    if(root == null) return false; // return when it's last node
    
    return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
}
```

second case is that we can back on to normal binary tree traversal when both of value is not the same. it means that it keep traversing the root tree to the last node. so it’s important to know that we can return to binary tree traveresal when it’s not the same value.

then if the value of both trees has the same value then return true or if one of the node has child node then return false since it doesn’t have same structure. 

when spilt to left and right node, we used `&&` which mean all of the child node has to be the same. if there is false from one of the descentant, then return false 

```java
private boolean isSame(TreeNode root, TreeNode subRoot) {
    if(root == null && subRoot == null) return true; // if both last node is null, it could be same structure
    if(root == null || subRoot == null) return false; // if one of the node has child node, it doesn't have same structure
    
    if(root.val != subRoot.val) return false; // if it's not the same value we can go back to normal traverse which is isSubtree method

    return isSame(root.left, subRoot.left) && isSame(root.right, subRoot.right); 
}
```

## Lesson I learnt

---

1. DFS has another DFS stream 
2. important to set return point in DFS
