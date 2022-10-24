---
title: "1903.\_Largest Odd Number in String"
catalog: true
date: 2022-10-24 23:04:09
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the largest odd number from the non empty substring of input num

### **Edge cases**

---

- 57 → [57, 7] → 57
- 4206 → “”
- 3570 → 357

### **Brute force approach (Discussion + Complexity)**

---

To determine odd number, check the last digit is odd (1,3,5,7,9). we wants to find the larest odd number so start from the whole number string and shift the lowest digit to the left. Time complexity is O(n) and space complexity is O(1)

```java
class Solution {
    public String largestOddNumber(String num) {
        for(int i = num.length() - 1; i >= 0; i--) {
            if(num.charAt(i) % 2 == 1) return num.substring(0, i+1);
        }
        return "";
    }
}
```

### **Optimised approach (Discussion + Complexity) …X times**

---

### **Coding**

---

```java
class Solution {
    public String largestOddNumber(String num) {
        for(int i = num.length() - 1; i >= 0; i--) {
            if(num.charAt(i) % 2 == 1) return num.substring(0, i+1);
        }
        return "";
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4d577f1b-f44d-42e1-a7e1-f4b86ccefd4e/Untitled.png)

### What I learned

---

1. This is longest and shortest substring problem. so it is important to determin the start and end pointer. in this case, **we start from 0, last index to 0, 0**
2. I thought we need two pointer but
