---
title: "19.\_Remove Nth Node From End of List"
catalog: true
date: 2022-10-08 17:16:30
subtitle:
header-img:
tags:
---
## Conditions

---

- remove the `nth` node from the end of the list

## Solve by hands first

---

I was thinking of using backtracking at first but there is no way to pass [1], 1 case.

We can use the two pointer and dummy node. move the fast node to the right until n is zero. it helps us not to make the reverse linked list. and then make dummy node connecting to head. The slow node will reference dummy node and move slow and fast node together until fast node is null

![D2A30D41-1F0D-4ACD-A9F2-C9E507AE9BB3.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/85ee2cc1-4beb-415f-a7a5-30b5b34b88b0/D2A30D41-1F0D-4ACD-A9F2-C9E507AE9BB3.jpeg)

Time complexity is O(n)

## Solution

---

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        
        ListNode dummy = new ListNode(0, head);
        ListNode slow = dummy, fast = head;
        
        while(n-- > 0) fast = fast.next;
        while(fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        slow.next = slow.next.next;
        return dummy.next;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/872ce9c3-bde6-4f56-b9b5-cfd83acfdf48/Untitled.png)

## Lesson I learnt

---

1. Two pointer is good for Linked list
2. Dummy node can help

```java
Node dummy = new Node(0)
dummy.next = head;

return dummy.next // is equals to head;
```

1. Concat the node with only one node by using pointer

```java
// ~~slow.next = fast.next~~;
slow.next = slow.next.next;
```

1. Backtracking is not a good option for linked list
