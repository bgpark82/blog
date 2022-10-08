---
title: "21.\_Merge Two Sorted Lists"
catalog: true
date: 2022-10-08 13:57:14
subtitle:
header-img:
tags:
---
## Conditions

---

- sorted linked lists `list1` and `list2`
- Return *the head of the merged linked list*
- Both `list1` and `list2` are sorted in **non-decreasing** order.

## Solve by hands first

---

The problem is which node should I pick up. we can solve this issue by passing one of each node to method at a time. if the value of node1 is less than node2, then we move the pointer of node1 to the next node. so we can choose the smaller value as next node of node1. 

![6DB1070C-FB47-4726-AE3C-3EC0FCB3D54A.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8b1d79ad-9eb1-485c-813d-5888e05d7c6c/6DB1070C-FB47-4726-AE3C-3EC0FCB3D54A.jpeg)

when it gets to the last node which will be null then return the non null node. otherwise it will be pointing to null 

![72D9C3E1-FEB2-4F88-9C6F-7C835AAEC175.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/537875f4-e6d4-400b-93dc-80138926e603/72D9C3E1-FEB2-4F88-9C6F-7C835AAEC175.jpeg)

## Solution

---

```java
class Solution {
    public ListNode mergeTwoLists(ListNode n1, ListNode n2) {
        if(n1 == null) return n2;
        if(n2 == null) return n1;
        if(n1.val <= n2.val) {
            n1.next = mergeTwoLists(n1.next, n2);
            return n1;
        } else {
            n2.next = mergeTwoLists(n1, n2.next);
            return n2;
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2e988140-fabd-49ce-9f65-441882cc48f7/Untitled.png)

![0346DEB9-8B32-4EA5-B64D-07819821B006.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4e893057-1502-4d21-b1c2-077d28cceb8d/0346DEB9-8B32-4EA5-B64D-07819821B006.jpeg)

## Lesson I learnt

---

1. I don’t need to move the two node at the same time. move the one node at a time and compare them. setting a right base case is important in this case

```java
public ListNode mergeTwoLists(ListNode n1, ListNode n2) {
        if(n1 == null) return n2;
        if(n2 == null) return n1;
        if(n1.val <= n2.val) {
            n1.next = mergeTwoLists(n1.next, n2);
            return n1;
        } else {
            n2.next = mergeTwoLists(n1, n2.next);
            return n2;
        }
    }
```

1. reference the next node to return value
    - I can reference the return value as next node
    - also consider return node itself

```java
n1.next = mergeTwoLists(n1.next, n2);
return n1;
```
