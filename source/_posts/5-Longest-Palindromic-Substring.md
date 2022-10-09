---
title: "5.\_Longest Palindromic Substring"
catalog: true
date: 2022-10-09 15:54:32
subtitle:
header-img:
tags:
---
## Conditions

---

- return *the longest palindromic substring* in `s`

## Solve by hands first

---

There are various way of solving Palindrome problem. in this case, we can search through each letters and expanding each letter from the middle. for instance, there is word “abc”, to check if it’s Palindrome, starting from b and expand left and right letter at a time. then it will be “abc”. if left and right letter is the same, then we can call it Palindrome

But there are two cases, even and odd size of palindrome. so we have to check both cases

## Solution

---

```java
class Solution {
    public String longestPalindrome(String s) {
        
        String ans = "";
        for(int i = 0; i < s.length(); i++) {
            
            // odd string
            int l = i, r = i;
            while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                if(ans.length() < r - l + 1) {
                    ans = s.substring(l, r+1);
                } 
                l--;
                r++;
            }
            
            // even string
            l = i; r = i+1;
            while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                if(ans.length() < r - l + 1) {
                    ans = s.substring(l, r+1);
                } 
                l--;
                r++;
            }
        }
        
        return ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ea3660bc-2fa5-4846-942e-dc1e35360eff/Untitled.png)

## Lesson I learnt

---

1. Palindrome solution

```java
// odd string
int l = i, r = i;
while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
    l--;
    r++;
}
```

```java

// even string
l = i; r = i+1;
while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
    l--;
    r++;
}
```
