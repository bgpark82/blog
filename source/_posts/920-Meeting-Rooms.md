---
title: 920. Meeting Rooms
catalog: true
date: 2022-10-19 20:47:00
subtitle:
header-img:
tags:
---
### **Requirements**

---

- with the given array of meeting time intervals consisting of start and end times, determine if the person could attend all meetings

### **Edge cases**

---

- elements are all positive?
- element is in ascending order or order doesn’t matter?
- [] → false
- [[1,2]] → true
- [[1,2],[2,3] → false
- [[1,2],[2,1] → false

### **Brute force approach (Discussion + Complexity)**

---

Starting from intervals[0] and compare intervals[1] … intervals[n]. if the start time of next interval is less than equals to previous interval end time, then return false. otherwise retur true. but it’s only possible when intervals is ordered by start time. For instance [[5,10],[100,200],[20.25]] in this case 20 is less than 200 but person can attend all class

if we order the intervals by the start time, it will look like [[5,10],[20,25][100,200]], in this case we can compare adjacent intervals.

```java
public boolean canAttendMeetings(List<Interval> intervals) {
    Collections.sort(intervals, (a,b) -> a.start - b.start);
    
    for(int i = 1; i < intervals.size(); i++) {
        if(intervals.get(i-1).end > intervals.get(i).start) return false;
    }
    return true;
}
```

Time complexity is O(dual pivot quick sort) + O(length of intervals) which is O(nlog(n)) + O(n) = O(nlog(n))

### **Optimised approach (Discussion + Complexity) …X times**

---

*the interviewer can provide you with some hints to unblock you*

*it’s okay to ask the interviewer for direction.*

### **Coding**

---

```java
public boolean canAttendMeetings(List<Interval> intervals) {
		if(intervals.size() == 0 || intervals == null) return true;

    Collections.sort(intervals, (a,b) -> a.start - b.start);
    
    for(int i = 1; i < intervals.size(); i++) {
        if(intervals.get(i-1).end > intervals.get(i).start) return false;
    }
    return true;
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/48038efb-ce8e-49a7-9a08-10517ab29986/Untitled.png)

### What I learned

---

1. edge case can be a good to decrease the running time
2. compare previous index and current index. start with index 1 and compare `*i*` and `*i-1*`

```java
 for(int i = 1; i < nums.length; i++) {
    if(nums[i-1] > nums[i]) return true;
}
```
