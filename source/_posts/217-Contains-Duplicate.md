---
title: "217.\_Contains Duplicate"
catalog: true
date: 2022-10-18 18:25:17
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return true if any value appears at least twice in the array
- return false if every element is distinct
- length of nums array is greater eqauls to 1 and less than equals to 10^5
- value of array is greater than equals to -10^9 and less than equals to 10^9

### **Edge cases**

---

- [1,2,3,4] → false
- [1,2,2,4] → true
- [] → true

### **Brute force approach (Discussion + Complexity)**

---

I can simply using Set data structure and compare the length of input array. if the length of set is equals length of input array, it returns false since it’s distinct 

```java
Set<Integer> s = new HashSet();
for(int n : nums) s.add(n);
return nums.length == s.size() ? false : true;
```

Time complexity is O(n) since it needs to create the Set. Space complexity is O(n) in order to store the size of input array

### **Optimised approach (Discussion + Complexity) …X times**

---

There is way we can optimize the space complexity using sort. It definitely has trade off. Sort the array first and compare to next value. if the value is equals to next value then return true. otherwise return false.

Time complexity is O(nlogn) which is dual pivot quick sort in java Arrays.sort method. and space complexity is O(1)

```java
Arrays.sort(nums);
int f = nums[0];
for(int i = 1; i < nums.length; i++) {
    if(f == nums[i]) return true;
    f = nums[i];
}
return false;
```

There is way we can optimize the solution. we don’t need to store all of arrays in HashMap, instead compare every element in node and value in set. if we find the same value in set, we can immediately return true. otherewise return false. it helps us to save time depends on how much we can find the duplicated value. but total Time complexity can be the same as previous solution

```java
Set<Integer> s = new HashSet();
for(int i = 0; i < nums.length; i++) {
    if(s.contains(nums[i])) return true;
    s.add(nums[i]);
}
return false;
```

### **Coding**

---

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> s = new HashSet();
        for(int i = 0; i < nums.length; i++) {
            if(s.contains(nums[i])) return true;
            s.add(nums[i]);
        }
        return false;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/935f4cfa-bd73-4aea-8dc4-b2661b069238/Untitled.png)

### What I learned

---

1. Time complexity and space complexity of `*Arrays.sort()*` in java
    - Time complexity : O(nlog(n))
    - Spack complexity : O(1)
2. Time complexity and space complexity of `*HashSet*` in java
    - Time complexity on add : O(1)
