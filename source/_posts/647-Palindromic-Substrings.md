---
title: "647.\_Palindromic Substrings"
catalog: true
date: 2022-09-11 21:32:32
subtitle:
header-img:
tags:
---
## Conditions

---

- palindrome is the string can be read the same backward and forward

## Solve by hands first

---

Since palindrome is the string when it reads the same forward and backward. it can consist of either even or odd number of characters. **So we have to treat it differently depending on size**. For odd number of string, it can start from size one and for even number of string, it can start from size two. **we need to expand the start index to left and last index to right**. 

```java
ex) aabaa

     i j
-------------------------------------------------
odd  0 0 → a  → -1 1 
even 0 1 → aa → -1 2
-------------------------------------------------
odd  1 1 → a  →  0 2 → aab
even 1 2 → ab 
-------------------------------------------------
odd  2 2 → b  →  1 3 → aba → 0 4 → aabaa
even 2 3 → ba →  1 4 → abaa
-------------------------------------------------
odd  3 3 → a  →  2 4 → baa
even 3 4 → ba
-------------------------------------------------
odd  4 4 → a  
```

## Solution

---

```java
class Solution {
    
    int count = 0;
    public int countSubstrings(String s) {    
        for(int i = 0; i < s.length(); i++) {
            expand(s, i, i); // odd
            expand(s, i, i+1); // even
        }
        return count;
    }
    
    private void expand(String s, int i, int j) {
        while(i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            count++; i--; j++;
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/252f3d33-fc19-4922-b198-c5c4801d0e1b/Untitled.png)

## Lesson I learnt

---

1. Another way to solve **Palindrome problem by expanding the start and last index**

```java
expand(s, i, i); // odd
expand(s, i, i+1); // even

private void expand(String s, int i, int j) {
    // the same first and last letter within boundary and expand
    while(i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
        count++; i--; j++;
    }
}
```
