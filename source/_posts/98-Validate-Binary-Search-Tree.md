---
title: "98.\_Validate Binary Search Tree"
catalog: true
date: 2022-10-04 23:08:59
subtitle:
header-img:
tags:
---
## Conditions

---

- Given the `root` of a binary tree
- *if it is a valid binary search tree (BST)*
- -2^31 <= Node.val <= 2^31 - 1

## Solve by hands first

---

we can simply compare left and right node to current node

```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        if(root == null) return true;
        if(root.right != null && root.val >= root.right.val) return false;
        if(root.left != null && root.val <= root.left.val) return false;
        
        if(root.left == null && root.right == null) return true;
        
        return isValidBST(root.left) && isValidBST(root.right);
    }
}
```

The problem is how to remember the previous parent node. **it means we have to track the history of parent node**

![32526D92-787F-48DC-8F8C-D7832A9B0846.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/aeacdbef-d991-40eb-9c4a-e85018e64187/32526D92-787F-48DC-8F8C-D7832A9B0846.jpeg)

we can keep tracking of parent value in two cases

1. move to left node
    - left node is always less than parent node
    - so we update the upper bound to parent node
    - Once set up the upper bound, whenever it move to right node, **it will stay upper bound forever!**
2. move to right node
    - right node is always greater than parent node
    - so we update the lower bound to parent node
    - Once set up the lower bound, whenever it move to left node, **it will stay lower bound forever!**

in a nut shell, the upper and lower bound switch when it move to the other way around (left → right, right → left)

## Solution

---

```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return valid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private boolean valid(TreeNode root, long left, long right) {
        if(root == null) return true;
        if(root.val >= right || root.val <= left) return false;
        
        return valid(root.left, left, root.val) && valid(root.right, root.val, right);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0336171d-a8fb-4c68-a31f-fe4fe4b419bf/Untitled.png)

## Lesson I learnt

---

1. Max int value in java is 2^31-1. but in this case, upper bound can be 2^31-1 and if it’s equals to max int then it will return false. so we have to use Long.MAX_VALUE
    
    ```java
    if(root.val >= right || root.val <= left) return false;
    ```
