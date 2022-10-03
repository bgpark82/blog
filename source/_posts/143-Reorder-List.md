---
title: "143.\_Reorder List"
catalog: true
date: 2022-10-03 11:30:27
subtitle:
header-img:
tags:
---
## Conditions

---

- You may not modify the values in the list's nodes.
- Only nodes themselves may be changed.

## Solve by hands first

---

1. split half
    
    ![075BA843-42AC-4CFA-B90C-7088A3C5E1F6.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1595618f-6e66-45ae-9031-819b766f5d2c/075BA843-42AC-4CFA-B90C-7088A3C5E1F6.jpeg)
    
2. reverse the second half
    
    ![F296F1B2-F6AE-470A-8202-93C5164ABBB9.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/19adefd7-34c8-4181-852e-25e94d19c81d/F296F1B2-F6AE-470A-8202-93C5164ABBB9.jpeg)
    
3. merge the first and second half
    
    ![D280446B-02F4-4316-9430-2F355EC395C5.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/304a1cc0-94e4-4c31-9adf-c409c30adb84/D280446B-02F4-4316-9430-2F355EC395C5.jpeg)
    

## Solution

---

```java
class Solution {
    public void reorderList(ListNode head) {
        
        if(head == null || head.next == null) return;
        
        ListNode pre = null, slow = head, fast = head, top = head;
        
        while(fast != null && fast.next != null) {
            pre = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // disconnect the half
        pre.next = null;
        
        ListNode end = reverse(slow);
        
        merge(top, end);
    }
    
    private ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head, next = null;
        
        while(curr != null) {
            next = curr.next; // set next pointer
            curr.next = prev; // reference next to prev
            prev = curr; // prev -> curr
            curr = next; // curr -> next
        }
        
        return prev;
    }
    
    private void merge(ListNode head, ListNode tail) {
        
        while(head != null) {
            
            ListNode nh = head.next, nt = tail.next;
            
            head.next = tail;
            if(nh == null) break;
            tail.next = nh;
            
            head = nh;
            tail = nt;
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/10083bc9-3e1d-4033-9fcf-eb5e041dfc20/Untitled.png)

## Lesson I learnt

---

1. utilize the pointer variable!
    
    ```java
    ListNode prev = null, curr = head, next = null;
    ```
    
2. edge case is important
    
    ```java
    1 -> 2 -> null  3 <- 4 <- 5
    
    private void merge(ListNode head, ListNode tail) {
        while(head != null) {
            ListNode nh = head.next, nt = tail.next;
            
            head.next = tail;
            if(nh == null) break; // next head can be null at node 2
            tail.next = nh;
            
            head = nh;
            tail = nt;
        }
    }
    ```
