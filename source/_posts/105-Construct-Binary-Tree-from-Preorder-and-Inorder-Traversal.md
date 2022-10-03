---
title: "105.\_Construct Binary Tree from Preorder and Inorder Traversal"
catalog: true
date: 2022-10-03 16:21:17
subtitle:
header-img:
tags:
---
## Conditions

---

- return *the binary tree*

## Solve by hands first

---

In preorder traversal, **first element is always be parent node**.

In inorder traversal, **first element is always be left node**.

so, we can split two sequence of array from parent node. 

```java
preorder = [3,9,20,15,7] -> [3 | 9 | 20,15,7]
inorder = [9,3,15,20,7]  -> [9 | 3 | 15,20,7] 
```

Create parent node with the first elmenet of preorder.

Find the index of the parent element in inorder from the first element of preorder. then, **pass left subarray of both preorder and inorder to** 

**left subproblem and also right subarray of both preorder and inorder to right sub problem**

```java
TreeNode parent = new TreeNode(preorder[0]);

parent.left = buildTree([9], [9]);
parent.right = buildTree([20,15,7], [15,20,7]);
```

Time complexity is O(logN)

## Solution

---

```java
class Solution {
    public TreeNode buildTree(int[] pre, int[] in) {
        if(pre.length == 0 || in.length == 0) return null;
        
        int head = pre[0];
        TreeNode root = new TreeNode(head);
        
        int mid = 0;
        for(int i = 0; i < in.length; i++) {
            if(in[i] == head) mid = i;
        }
        
        root.left = buildTree(Arrays.copyOfRange(pre, 1, mid+1), Arrays.copyOfRange(in, 0, mid));
        root.right = buildTree(Arrays.copyOfRange(pre, mid+1, pre.length), Arrays.copyOfRange(in, mid+1, in.length));
        
        return root;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a7234d8b-1ac4-4607-ae43-65687c183477/Untitled.png)

## Lesson I learnt

---

1. return node in tree dynamic programming would help

```java
public TreeNode buildTree(int[] pre, int[] in) {
	if(pre.length == 0 || in.length == 0) return null;
	TreeNode root = new TreeNode(head);
	        
	root.left = buildTree(sub(pre, 1, mid+1), sub(in, 0, mid));
	root.right = buildTree(sub(pre, mid+1, pre.length), sub(in, mid+1, in.length));

	return root;
}	
```
