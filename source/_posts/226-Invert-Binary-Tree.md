---
title: "226.\_Invert Binary Tree"
catalog: true
date: 2022-10-18 17:48:22
subtitle:
header-img:
tags:
---
### **Requirements**

---

- invert the binary tree and return its root
- what is inverted means in this question?
    - it means switch left and right child node.

### **Edge cases**

---

[4, 2, 7, 1, 3, 6, 9] → [4, 7, 2, 9, 6, 3, 1]

[] → []

[4] → [4]

[4, 2] → [4, null, 2]

### **Brute force approach (Discussion + Complexity)**

---

It’s hard to use brute force way on Tree data structure

### **Optimised approach (Discussion + Complexity) …X times**

---

We can start with Depth First Search. Switch left and right child nodes in every visit. Put the child node to Stack if they are not null

Time complexity is O(number of node). Space complexity is O(number of child nodes)

[]()

### **Coding**

---

```java
public TreeNode invertTree(TreeNode root) {
    if(root == null) return null;
    Stack<TreeNode> s = new Stack();
    s.add(root);
    while(!s.isEmpty()) {
        TreeNode n = s.pop();
        // switch
        TreeNode l = n.left;
        n.left = n.right;
        n.right = l;
        
        if(n.left != null) s.add(n.left);
        if(n.right != null) s.add(n.right);
    }
    return root;
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/67467859-cf57-4e23-8d44-e31a64696bcb/Untitled.png)

### What I learned

---

1. it doesn’t need to backtrack to change the tree structure.
