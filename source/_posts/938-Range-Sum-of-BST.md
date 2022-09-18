---
title: "938.\_Range Sum of BST"
catalog: true
date: 2022-09-18 23:56:20
subtitle:
header-img:
tags:
---
## Conditions

---

- binary search tree
- 1 <= Node.val <= 105
- 1 <= low <= high <= 105
- All `Node.val` are **unique**.

## Solve by hands first

---

we can use the character of binary search tree. we don’t need to check any nodes less than lower bound and greater than upper bound. if we check the first node and its value is less than lower bound then we need higher value so move to the right node. if we check the first node and its value is greater than upper bound then we need smaller value so move to the left node. if the value of current node is within the range then add the value then check left and right node because value of the child nodes can be without the range as well.  but if valud of child node is null then we can return 0 since its doesn’t need to added. then we can simply pass the added value to parent node.

## Solution

---

```java
class Solution {
    
    public int rangeSumBST(TreeNode root, int low, int high) {
        if(root == null) return 0;
        if(root.val < low) return rangeSumBST(root.right, low, high); // low 보다 작은 노드는 갈 필요가 없다. 그러니 오른쪽 노드만 살펴보면 됨
        if(root.val > high) return rangeSumBST(root.left, low, high); // high 보다 큰 노드는 갈 필요가 없다. 그러니 왼쪽 노드만 살펴보면 됨
        return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
    }
}
```

![1D103191-F17F-4BDF-A9A9-3B2B09D7957D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4ae21bc5-4c3e-4a24-9ed8-9050362dec48/1D103191-F17F-4BDF-A9A9-3B2B09D7957D.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1a81ff54-737c-43a5-9e45-544034839154/Untitled.png)

### Lesson I learnt

---

1. left node is less than its parent node in binary search tree
2. right node is greater than its parent node in birnary search tree
