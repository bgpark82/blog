---
title: "78.\_Subsets"
catalog: true
date: 2022-11-06 15:08:26
subtitle:
header-img:
tags:
---
### **Requirements**

---

- nums is integer array of unique elements
- return all possible subsets without duplicated subset in any order

### **Edge cases**

---

- [1,2,3] → [[],[1],[2],[3],[1,2],[2,3],[1,3],[1,2,3]]
- [1,2] → [[],[1],[2],[1,2]]

### **Brute force approach (Discussion + Complexity)**

---

We can start with backtracking. we have to store the list depending on its size (1 ~ length of nums). it can be base case in backtracking. if the size of sub array is equal to given size, then add subarray to answer array. 

One of the requirement is that no duplication allowed (Combination). we can loop from index 0 to n-1 and to meet this condition, always increase the index to the next node. we don’t need to worry about the size because **it only can add subarray when its size equals to given size, other size of subarray is ignored**. 

In the backtracking, add the value in subarray and remove it after backtracking operation.

Time complexity is O(n*n^(n-1)) and space complexity is O(n)

```java
List<List<Integer>> res = new ArrayList();
int n, s;

public List<List<Integer>> subsets(int[] nums) {
    for(s = 0; s <= nums.length; s++) {
        backtracking(nums, 0, new ArrayList());
    }
    return res;
}

private void backtracking(int[] nums, int idx, List<Integer> sub) {
    if(sub.size() == k) { // other size of subarray is ignored
        res.add(new ArrayList(sub));
        return;
    }
    
    for(int i = idx; i < nums.length; i++) {
        sub.add(nums[i]);
        backtracking(nums, i+1, sub);
        sub.remove(sub.size()-1);
    }
}
```

![79830C02-6EF9-42C0-B07A-AAB45E3EAACE.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4d4f9f3d-4a5e-45c5-9a39-d52e77d7e81e/79830C02-6EF9-42C0-B07A-AAB45E3EAACE.jpeg)

### **Optimised approach (Discussion + Complexity) …X times**

---

### **Coding**

---

```java
class Solution {
    List<List<Integer>> res = new ArrayList();
    int n, s;

    public List<List<Integer>> subsets(int[] nums) {
        for(s = 0; s <= nums.length; s++) {
            backtracking(nums, 0, new ArrayList());
        }
        return res;
    }

    private void backtracking(int[] nums, int idx, List<Integer> sub) {
        if(sub.size() == k) {
            res.add(new ArrayList(sub));
            return;
        }
        
        for(int i = idx; i < nums.length; i++) {
            sub.add(nums[i]);
            backtracking(nums, i+1, sub);
            sub.remove(sub.size()-1);
        }
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6735aa39-9290-4a02-a49e-58272619e032/Untitled.png)

### What I learned

---

1. remove list in bactracking. remove the last element in array

```java
sub.remove(sub.size()-1);
```

1. In combination, we can increase the size of subarray in every iteration

```java
for(int size = 0; size <= nums.length; size++) {
		backtracking(nums, 0, size);
}
```

1. Enable to filter out any subarray with base case. 

```java
if(sub.size() == k) { // filter any subarray size is not equal to k.
    res.add(new ArrayList(sub));
    return;
}
// only can move down
//   1. subarray size is not k
//   2. idx is greater than nums.length
for(int i = idx; i < nums.length; i++) { // 0 ~ n-1
    sub.add(nums[i]);                    // it will add and remove anyway
    backtracking(nums, i+1, sub);
    sub.remove(sub.size()-1);
}
```
