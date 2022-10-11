---
title: "3.\_Longest Substring Without Repeating Characters"
catalog: true
date: 2022-10-11 20:15:46
subtitle:
header-img:
tags:
---
## Conditions

---

- find the length of the **longest substring** without repeating characters.
- `s` consists of English letters, digits, symbols and spaces.

## Solve by hands first

---

The goal is to find the maximum length of substring. we can iterate each position and compare with the max length.

The problem is how to find the maximum length. we can use the **sliding window**. Create the left and right pointer. Move the right pointer to the right in every iteration and store the character in string as key and position as value in HashMap. If the right pointer find the character stored in the HashMap then move the left pointer to right from the position of stored in HashMap. **It will keep updating the poistion of the same character to right**. 

![35E4038F-CE7F-4AED-A251-987BE48779AC.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/838202f4-9e6b-4896-b98f-14a8c54037ee/35E4038F-CE7F-4AED-A251-987BE48779AC.jpeg)

The key is that **we have to skip the left pointer to the duplicate position**. For instance “abcb” if the right pointer is last “b” and the left pointer at “a”, we have to skip left pointe to the poisition of stored “b” + 1. but **there is possibility moving left pointer to backward** **if the poisition of the stored value is way behind the current left pointer**.

![3E8CD1AF-BB6A-4C2F-801E-F9B709F67873.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c61832ad-8d0a-48f4-b8a2-12771bdf594e/3E8CD1AF-BB6A-4C2F-801E-F9B709F67873.jpeg)

Time complexity is O(n)

## Solution

---

```java
class Solution {
    
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> m = new HashMap();
        int max = 0;
        for(int l = 0, r = 0; r < s.length(); r++) {
            if(m.containsKey(s.charAt(r))) {
                l = Math.max(l, m.get(s.charAt(r)) + 1);
            }
            m.put(s.charAt(r), r);
            max = Math.max(max, r - l + 1);
        }
        
        return max;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7fd4a9ca-1656-4dce-8803-4208fc5f83ec/Untitled.png)

## Lesson I learnt

---

1. Storing character and position in HashMap and keep updating in every iteration

```java
for(int l = 0, r = 0; r < s.length(); r++) {
    m.puts.charAt(r), r);
    max = Math.max(max, r - l + 1);
}
```

1. Find the maximum left position since it will keep updating the position of duplicated characters (**current left position vs position of duplicated characters**)

```java
l = Math.max(l, m.get(s.charAt(r)) + 1);
```(
