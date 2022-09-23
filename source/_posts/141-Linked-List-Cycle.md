---
title: "141.\_Linked List Cycle"
catalog: true
date: 2022-09-23 19:44:36
subtitle:
header-img:
tags:
---
## Conditions

---

- determine if the linked list has a cycle in it.
- `pos` is used to denote the index of the node
- `pos` is `-1` or a **valid index** in the linked-list.
- The number of the nodes in the list is in the range `[0, 10^4]`.
- `10^5 <= Node.val <= 10^5`

## Solve by hands first

---

Each node in list has its own unique memory address. **we can store them in HashSet and during the iteration if there is value in HashSet exist then we can return true**. Time complexity is O(n) and space complexity is O(n)

## Solution

---

```java
public class Solution {
    
    public boolean hasCycle(ListNode head) {
        if(head == null) return false;
        
        Set<ListNode> s = new HashSet();
        
        while(head.next != null) {
            if(s.contains(head)) return true;
            s.add(head);
            head = head.next;
        }
        return false;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/21a3df08-f29d-4f0a-ad7f-7fc58ba80a3f/Untitled.png)

is there any way lower than space complexity O(n)?

If we use the **tortoise and hare algorithm then time complexity is O(n) and space complexity is O(1)**

```java
public class Solution {
    
    public boolean hasCycle(ListNode head) {
        
        ListNode slow = head, fast = head;
        
        while(fast != null && fast.next != null) { // fast pointer reach to the last node quicker than slow pointer
            slow = slow.next;
            fast = fast.next.next;
            
            if(slow == fast) return true;
        }
        return false;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5bd3c9f7-ec04-4ead-91bc-9898ac1c6d72/Untitled.png)

fast pointer reach to the last node quicker than slow pointer. **so we can check the loop condition at the fast node**

## Lesson I learnt

---

1. [Floyd's tortoise and hare algorithm](https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_tortoise_and_hare)
    - pointer algorithm that uses only two pointers
    - move through the sequence at different speeds
    - **if slow pointer and fast pointer meet, then it’s cylce!**
    - but what if cycle is long enough??
    - it’s always O(n)
        - every iteration distance between slow and fast pointer decrease 1 at a time!
        - if the entire length of list is n then the maximum gap between two pointer would be n, it ended up meeting two pointers after O(n) time
        - so total time complexity is O(2n) which is equals to O(n)
    - fast node always reach the last node earlier than slow node. so **check the last node with fast pointer**
2. Don’t compare value of node, **compare address of node!**
    
    ```json
    Set<Node> s
    ```
