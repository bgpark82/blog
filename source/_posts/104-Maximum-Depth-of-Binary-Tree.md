---
title: "104.\_Maximum Depth of Binary Tree "
catalog: true
date: 2022-10-18 21:07:59
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return maximum depth of binary tree
- maximum depth is the longest path from the root node to farthet leaf node
- maximum number of nodex in the trees is between 0 to 10^4
- range of value of node is between -100 and 100

### **Edge cases**

---

[1, null, 2] → 2

[] → 0

[1] → 1

[3, 9, 20, null, null, 15, 7] → 3

### **Brute force approach (Discussion + Complexity)**

---

It’s hard to use brute force way on Tree data structure

### **Optimised approach (Discussion + Complexity) …X times**

---

We can start with Depth First Search. Visit the left and right child node. If the node is null, return 0. Find the maximum of return value from left and right child node and **plus one as referring to one more steps**.

Time complexity is O(height of binary tree)

```java
public int maxDepth(TreeNode root) {
    if(root == null) return 0;
    return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
}
```

![F97107E0-C8AF-4D10-9F58-67A0714AF800.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4191c462-eb81-4e76-ba0e-ef847e4c0ccb/F97107E0-C8AF-4D10-9F58-67A0714AF800.jpeg)

### **Coding**

---

```java
class Solution {
    
	  public int maxDepth(TreeNode root) {
	      if(root == null) return 0;
	      return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
	  }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/09618b8f-d784-46a8-a817-3c243ab46f0a/Untitled.png)

### What I learned

---
