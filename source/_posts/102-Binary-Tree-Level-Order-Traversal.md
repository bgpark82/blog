---
title: "102.\_Binary Tree Level Order Traversal"
catalog: true
date: 2022-10-03 15:16:57
subtitle:
header-img:
tags:
---
## Conditions

---

- binary tree
- return *the level order traversal of its nodes' values*
- from left to right, level by level

## Solve by hands first

---

we can use BFS algorithm. Time complexity is O(height of node)

The problem is how to I know the node in queue was in the same level. **The answer is that get the size of current queue which is the node in the same level and iterate in length of curren queue times** 

```java
queue = [1] // level 0 -> size 1 -> iterate once
queue = [2,3] // level 1 -> size 2 -> iterate twice
```

```java
while(!q.isEmpty()) {
    int len = q.size();

    for(int i = 0; i < len; i++) {
        TreeNode node = q.poll();        
				q.offer(node.left);
        q.offer(node.right);    
    }            
    ans.addlurn ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6536d54e-02ff-4b64-9ef6-116b55ffe9aa/Untitled.png)

## Lesson I learnt

---

1. Mark the same level in queue
    - Iterate length of current queue size times
    
    ```java
    while(!q.isEmpty()) {
        int len = q.size();
    
        for(int i = 0; i < len; i++) {
            TreeNode node = q.poll();        
    				q.offer(node.left);
            q.offer(node.right);    
        }            
        ans.add(list);
    }
    ```ist);
}
```

## Solution

---

```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList();
        
        if(root == null) return ans;
        
        Queue<TreeNode> q = new ArrayDeque();
        q.offer(root);
        
        while(!q.isEmpty()) {
            int len = q.size();
            
            List<Integer> list = new ArrayList();
            for(int i = 0; i < len; i++) {
                TreeNode node = q.poll();
                list.add(node.val);
                
                if(node.left != null) q.offer(node.left);
                if(node.right != null) q.offer(node.right);    
            }            
            ans.add(list);
        }
        
        ret(
