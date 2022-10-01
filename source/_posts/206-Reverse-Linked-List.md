---
title: "206.\_Reverse Linked List"
catalog: true
date: 2022-10-01 12:46:42
subtitle:
header-img:
tags:
---
## Conditions

---

- singly linked list
- return *the reversed list*
.

## Solve by hands first

---

Simply store the node values in Stack and create the reversed linked list

## Solution

---

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        if(head == null) return null;
        
        Stack<Integer> s = new Stack();
        while(head != null) {
            s.push(head.val);
            head = head.next;
        }
        
        ListNode node = new ListNode(s.pop());
        head = node;
        while(!s.isEmpty()) {
            ListNode cn = new ListNode(s.pop());
            head.next = cn;
            head = cn;
        }
        
        return node;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b2415ecc-1eb0-4bd1-a52b-cf0dc92f4473/Untitled.png)

## Lesson I learnt

---
