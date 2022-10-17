---
title: "125.\_Valid Palindrome"
catalog: true
date: 2022-10-17 23:46:56
subtitle:
header-img:
tags:
---
### **Requirements**

---

- after converting all uppercase letters into lowercase letters
- removing all non-alphanumeric characters
- Alphanumeric characters include letters and numbers
- return `true` *if it is a **palindrome**, or* `false` *otherwise*

### **Edge cases**

---

a b2b A → ab2ba

2 → 2

a → a

Aba → aba

! @ ! 2 → 2

- space
- letter
- numeric

### **Brute force approach (Discussion + Complexity)**

---

Check each element if it’s letter or numeric. if the character is letter and uppercase, convert it to lowercase. 

After iteration complete, check if the string is palindrome or not

Time complexity is O(n)

### **Optimised approach (Discussion + Complexity) …X times**

---

We can just compare from very left and right characters. if each of the character is not letter or digits then move the pointer to left or right. otherwise compare the chrarcter of left and right index, if those are not the same character then return false, otherwise move the pointer to left and right.

Time complexity is O(n)

### **Coding**

---

```java
class Solution {
    public boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while(l <= r) {
            if(!Character.isLetterOrDigit(s.charAt(l))) l++; 
            else if(!Character.isLetterOrDigit(s.charAt(r))) r--;
            else {
                if(Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
                l++; r--;
            }
        }
        return true;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ad2a96b5-ad39-4fb4-9e66-dba769650eda/Untitled.png)

### What I learned

---

1. Compare from left and right index to solve the Palindrome problem
