---
title: "136.\_Single Number"
catalog: true
date: 2022-10-23 23:39:38
subtitle:
header-img:
tags:
---
### **Requirements**

---

- find the single element in array
- linear time complexity and constant extra space
- element is between -3*10^4 and 3*10^4
- can’t use sorting

### **Edge cases**

---

- [2,2,1] → 1
- [1] → 1
- [1,2,3,1,2,4] → 4

### **Brute force approach (Discussion + Complexity)**

---

even though it has to be linear time complexity but I want to start with brute force way first. we can sort the array and use two pointer to find the target value. if the length of array is 1, return the value at index 0 since there is empty input array. sort the array in whichever order. create two pointer left and right. left start with 0 and right start with 1. if the value at index left and right is equals, increase 2 index and move on. because if we just move 1 index at a time, value at left and right index will be different and accidently return wrong answer. otherwise return current value. if there is nothing returned and return the last value of array since it can’t reach to it. Time complexity is O(nlog(n)) for dual pivot quick sort to sort the array and space complexity is O(1)

```java
if(nums.length == 1) return nums[0];

Arrays.sort(nums);

int l = 0, r = 1;
while(r < nums.length) {
    if(nums[l] == nums[r]) {
        l+=2; 
        r+=2;
        continue;
    }
    return nums[l];
}
return nums[nums.length-1];
```

### **Optimised approach (Discussion + Complexity) …X times**

---

I can use XOR operator to optimize above solution by O(n). If we do XOR operation with the same value it will return 0. XOR operation with 0 and whatever number, it will return whatever number. we gonna use this behavior. So, there is only 1 single element, it means if we do XOR operation, the single element will be returned. For instance, [2,2,1] turn into 2 ^ 2 = 0 and 0 ^ 1 = 1. Time complexity is O(n) and space complexity is O(1)

```java
int ans = 0;
for(int n : nums) ans ^= n;
return ans;
```

### **Coding**

---

```java
class Solution {
    public int singleNumber(int[] nums) {
        int ans = 0;
        for(int n : nums) ans ^= n;
        return ans;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/da817482-331e-4647-8e4c-0f61237ea263/Untitled.png)

### What I learned

---

1. bit manipulation can be good solution for array problem
    - XOR : the same value will return 0
