---
title: "230.\_Kth Smallest Element in a BST"
catalog: true
date: 2022-10-02 11:07:43
subtitle:
header-img:
tags:
---
## Conditions

---

- binary search tree
- return *the* `kth` *smallest value*

## Solve by hands first

---

If we traverse in inorder (left → root → right) in binary search tree, it always find the smallest value first. Time complexity is O(n)

## Solution

---

```java
class Solution {
    
    int c = 0, ans = 0;
    
    public int kthSmallest(TreeNode root, int k) {
        c = k;
        io(root);
        return ans;
    }
    
    private void io(TreeNode root) {
        if(root.left != null) io(root.left);
        if(--c == 0) {
            ans = root.val;
            return;
        }
        if(root.right != null) io(root.right);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/371dbcff-7b9e-493b-9e05-a0279fd1a414/Untitled.png)

## Lesson I learnt

---

1. inorder : left → root → right
2. inorder with code
    
    ```java
    private void io(Node root, int k) {
    	io(root.left, k);  // visit left node
    	k++;               // just handle value at current node
    	io(root.right, k); // visit right node
    }
    ```
    
3. **decrease target instead of increasing to target**
