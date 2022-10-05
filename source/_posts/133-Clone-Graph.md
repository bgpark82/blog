---
title: "133.\_Clone Graph"
catalog: true
date: 2022-10-05 20:49:31
subtitle:
header-img:
tags:
---
## Conditions

---

- **[connected](https://en.wikipedia.org/wiki/Connectivity_(graph_theory)#Connected_graph)** undirected graph.
- Return a **[deep copy](https://en.wikipedia.org/wiki/Object_copying#Deep_copy)** (clone) of the graph.

## Solve by hands first

---

we can use dfs with map. it will traverse all the way to the last node in graph and travel back to the first node. Time complexity is O(n). 

![DD01F724-B3D7-40D6-B669-0B6B9DD7B10D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c025d93f-7565-439e-b936-6ce5a100b005/DD01F724-B3D7-40D6-B669-0B6B9DD7B10D.jpeg)

## Solution

---

```java
class Solution {
    
    Map<Node, Node> oldToNew;
    
    public Node cloneGraph(Node node) {
        if(node == null) return null;
        oldToNew = new HashMap();
        return dfs(node);
    }
    
    private Node dfs(Node node) {
        if(oldToNew.containsKey(node)) {
            return oldToNew.get(node);
        }
        
        Node n = new Node(node.val);
        oldToNew.put(node, n);
        for(Node nb : node.neighbors) {
            n.neighbors.add(dfs(nb));
        }
        return n;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5032e015-32ab-4a4f-a5f2-50063ada3264/Untitled.png)

## Lesson I learnt

---

1. **HashMap can be a good solution for Graph problem**
    - purpose of using map is to **disconnect reference of nodes**
    - the code below will exceed time limit
    - because it will create new node and  reference all of other nodes infinitely

```java
public Node cloneGraph(Node node) {
    return dfs(node);
}

private Node dfs(Node node) {        
    Node n = new Node(node.val);
    for(Node nb : node.neighbors) {
        n.neighbors.add(dfs(nb));
    }
    return n;
}
```

1. **dfs in Graph will traverse all the way to last node**. only way dereference node is using map
