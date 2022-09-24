---
title: "449.\_Serialize and Deserialize BST"
catalog: true
date: 2022-09-24 11:56:00
subtitle:
header-img:
tags:
---
## Conditions

---

- Design an algorithm to serialize and deserialize a **binary search tree**
- binary search tree can be serialized to a string
- this string can be deserialized to the original tree structure
- **The encoded string should be as compact as possible.**
- • The input tree is **guaranteed** to be a binary search tree.

## Solve by hands first

---

we need to keep the **preorder of BST** while serializing. so if input is [2,1,3] which is in preorder, we can keep this order in the string in output. it makes easier to deserialize. Time complexity of serialization and deserialization is O(n)

## Solution

---

```java
public class Codec {
    
    int idx = 0;

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root == null) return "null";
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        return dfs(data.split(","));
    }
    
    private TreeNode dfs(String[] sl) {
        String s = sl[this.idx];
        
        TreeNode node = null;
        this.idx++;
        
        if(s.equals("null")) return node;
        else node = new TreeNode(Integer.parseInt(s));

        node.left = dfs(sl);
        node.right = dfs(sl);
        return node;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5b1bc672-c028-4731-aab9-b85fe4fe08f4/Untitled.png)

## Lesson I learnt

---

1. preorder tree (parent → left → right)
2. create node in dfs method and return the node
    - I have never thought of **creating TreeNode in the method and return it**. only made a TreeNode outside of method and pass through parameter
    - it makes easy to connect to the each left and right child node from the created node in method
    
    ```java
    private TreeNode dfs(String[] sl) {
        String s = sl[this.idx];
        
        TreeNode node = null;
        this.idx++;
        
        if(s.equals("null")) return node;
        else node = new TreeNode(Integer.parseInt(s));
    
        node.left = dfs(sl);
        node.right = dfs(sl);
        return node;
    }
    ```
    
3. utilize the instance variable in class
    - the pointer has to shift right in every call of dfs method
    
    ```java
    private TreeNode dfs(String[] sl) {
        String s = sl[this.idx];
    }
    ```
