---
title: "424.\_Longest Repeating Character Replacement"
catalog: true
date: 2022-09-13 18:48:23
subtitle:
header-img:
tags:
---
## Conditions

---

- return the longest same letters after switch with any other letters

## Solve by hands first

---

```java
ABABBA 1

find the possible sequence of letter to change?????
A
AB
ABA -> AAA
ABAB -> x : over two letters
ABABBA -> x : over two letters

B
B A -> BB
B AB -> BBB
B ABB -> BBBB
B ABBA -> x

find the number of letter to switch is equals to k
calculate length of the same letters

```

I think we can start with **dynamic programing**. If you find the patterns it looks like sub problem can return the length of the same. then when it merge the parent problem then check if the letter of root problem is the frequent letter in sub problem. for instance, if the letters in sub problem is BAB then B is frequent letter. it goes back to root problem and its letter is B then increase the length or return 0 if it’s not the same

## Solution

---

we can use the **Sliding Window** instead of Brute Force and its time complexity will be O(n).

```java
class Solution {
    
    // length - frequent <= k
    public int characterReplacement(String s, int k) {
        
        int[] f = new int[26];
        
        int max = 0, mf = 0, l = 0, r = 0;
        
        for(; r < s.length(); r++) {
            f[s.charAt(r) - 'A']++;
            
            int length = r - l + 1; // 0
            mf = Math.max(mf, f[s.charAt(r)-'A']);  
            
            // move left index if frequent is greater than k
            if(length - mf > k) {
                f[s.charAt(l) - 'A']--;
                l++;
            }
            
            max = Math.max(max, r - l + 1);
        }
        
        return max;
    }
}
```

Not to duplicate number of frequent letter. For instance, if the letter at index i = 0, j = 0 is ‘A’, it will point to the same letter but it will count 2 for letter ‘A’. **we can only increase right index to track the number of letters**

Valify the length of less frequent substring is less than k and increase the left index once. **Because it will decrease total length of window which is always less than max value**. So we don’t need to keep increasing and validating the size of window by for loop. 

## Lesson I learnt

---

1. Time complexity of sliding window
2. `l` for left  index and `r` for right index
3. I can only use one of index to check the value in array
