---
title: "139.\_Word Break"
catalog: true
date: 2022-10-22 17:40:18
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return true if s can be segmented into one or more dictionary words.
- we can reuse the word in dictionary
- s should consisted of combination of words in dictionary

### **Edge cases**

---

- [abcdefg], [abc, de, fg] → true
- [abcdeabc], [abc, de] → true
- [abcdeabc], [abc, abcd, ea] → false

### **Brute force approach (Discussion + Complexity)**

---

Iterate through each index and check the word in dictinary. Initially setup the first index of dp as true. it means we can compare the word in dictionary to the substring from index which was true to length of word. if the current index is not true, it means we don’t need to make substring out of word. it makes us easily skip to the next index. when the index reaches to the last index (s.length()) and its dp value is true. we found the segements string out of word. Time complexity is O(length of string * length of wordDict) and space complexity is O(length of string)

```java
boolean[] dp = new boolean[s.length() + 1];
dp[0] = true; 
for(int i = 0; i <= s.length(); i++) {
    for(String w : wordDict) {
        if(dp[i] && i + w.length() <= s.length() && s.substring(i, i + w.length()).equals(w)) {
            dp[i + w.length()] = true;
        }
    }
}
return dp[s.length()];
```

in many solution, it breaks the loop if they meet the if statement. but we don’t actually need to do it. 

![ACBC727B-C97A-4424-89CE-487FD86C4663.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/92476fdf-d45e-4b62-b57b-e2955b340e4d/ACBC727B-C97A-4424-89CE-487FD86C4663.jpeg)

### **Optimised approach (Discussion + Complexity) …X times**

---

*the interviewer can provide you with some hints to unblock you*

*it’s okay to ask the interviewer for direction.*

### **Coding**

---

```java
public class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; 
        for(int i = 0; i <= s.length(); i++) {
            for(String w : wordDict) {
                if(dp[i] && i + w.length() <= s.length() && s.substring(i, i + w.length()).equals(w)) {
                    dp[i + w.length()] = true;
                }
            }
        }
        return dp[s.length()];
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f120d056-355d-4c6a-b1ad-40e15df878c2/Untitled.png)

### What I learned

---

1. **dp array can be mark flag** whether substring start with the current index can be valid or not.
2. try not to make tree for dynamic programming. just use the memoization
