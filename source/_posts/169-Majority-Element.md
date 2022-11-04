---
title: "169.\_Majority Element"
catalog: true
date: 2022-11-05 00:52:18
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the majority element
- majority element is the element that appears more than n/2 times

### **Edge cases**

---

- [3,2,3] → n/2 = 1.5 → 3 appears twice
- [1,2,3,5,1,2,1,1,2,1] → n/2 = 5 → 1 appears five times

### **Brute force approach (Discussion + Complexity)**

---

We need to know the majority factor which is length of array divided by 2. Create the map to store the element as key and the number of appearance as value. find the largest number of value in map. Time complexity is O(n) and space complexity is O(n)

```java
int m = nums.length/2;
Map<Integer, Integer> nm = new HashMap();
for(int n : nums) {
	nm.put(n, nm.getOrDefault(n, 0) + 1);
}
Map.Entry tmp = null;
int max = 0;
for(Map.Entry e : nm.entrySet()) {
	int v = e.getValue();
	if(v < m) continue;
	if(v > max) {
		max = v;
		tmp = e;
	}
}

return e.getKey();

```

We can make our code more readable. We don’t need to wait until the map filled. Whenever the value is greater than majority factor, we can return that value

```java
Map<Integer, Integer> m = new HashMap();
for(int n : nums) {
	m.put(n, m.getOrDefault(n, 0)+1); // increase value
	if(m.get(n) > nums.length/2) return n;
}
return 0;
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/54df2d4f-587d-445e-a1a4-3c5bfa8105d0/Untitled.png)

### **Optimised approach (Discussion + Complexity) …X times**

---

In array, majority element appears in the middle of array. For instance, if we sort the array  [1,2,3,1,1], it will be [1,1,1,2,3]. the element at the middle index which is 4/2 = 2 is 1 which is majority element. Time compexity is O(nlog(n)) and space complexity is O(1). Time complexity and space complexity can be trade off.

```java
Arrays.sort(nums);
return nums[nums.length/2];
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f8c411cf-d3be-421c-be7e-69187c434a48/Untitled.png)

### **Coding**

---

```java
class Solution {
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> m = new HashMap();
        for(int n : nums) {
            m.put(n, m.getOrDefault(n, 0)+1); 
            if(m.get(n) > nums.length/2) return n;
        }
        return 0;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/54df2d4f-587d-445e-a1a4-3c5bfa8105d0/Untitled.png)

### What I learned

---

1. We **can optimize two loop to one loop**

```java
for(int n : nums) {
		m.put(n, m.getOrDefault(n, 0)+1);
}

for(Map.Entry e : m.entrySet()) {
		
}

```

```java
for(int n : nums) {
	m.put(n, m.getOrDefault(n, 0)+1;
	if(m.get(n) > nums.length/2) return n;
}
```
