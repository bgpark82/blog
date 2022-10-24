---
title: "1624.\_Largest Substring Between Two Equal Characters"
catalog: true
date: 2022-10-25 00:06:58
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the longest substring between two equal number
- length of substring is greater than 2

### **Edge cases**

---

- aa → 0
- aba → 1
- → -1
- abba → 2
- abcdsa → 4

### **Brute force approach (Discussion + Complexity)**

---

I can start with two pointers. create left and right pointer with value 0 and s.length() - 1. but problem of the approach is that we can’t find the same value. if the left and right value is not equals we have to choose move both, left or right.

### **Optimised approach (Discussion + Complexity) …X times**

---

In order to find the same value at certain index, we can cache the value and its index. since it only consists of lowercase English letter, we can create int array with length of 26. store the index at the ascii character of array. Time complexity is O(n) and space complexity is O(1)

```java
Integer[] tmp = new Integer[26];
int max = 0;
for(int i = 0; i < s.length(); i++) {
    int idx = s.charAt(i) - 'a';
    if(tmp[idx] != null) {
        max = Math.max(max, i - tmp[idx]);
    } else {
        tmp[idx] = i;
    }
}
return max - 1;
```

### **Coding**

---

```java
class Solution {
    
    public int maxLengthBetweenEqualCharacters(String s) {
        Integer[] tmp = new Integer[26];
        int max = 0;
        for(int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';
            if(tmp[idx] != null) {
                max = Math.max(max, i - tmp[idx]);
            } else {
                tmp[idx] = i;
            }
        }
        return max - 1;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5d3f499d-8b14-4bf0-b9ef-c7367af2ddd3/Untitled.png)

### What I learned

---

1. Store the index at its ascii character is useful in longest and shortest problem 

```java
Integer[] tmp = new Integer[26];
for(int i = 0; i < s.length(); i++) {
    int idx = s.charAt(i) - 'a';
    tmp[idx] = i; // store index at ascii character
}
```
