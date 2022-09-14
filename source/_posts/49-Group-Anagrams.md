---
title: "49.\_Group Anagrams"
catalog: true
date: 2022-09-14 22:30:43
subtitle:
header-img:
tags:
---
## Conditions

---

- **Anagram** is the word can formed by rearranging the letters of another. typically using all the letters once (cinema → iceman)
- answer can be any order

## Solve by hands first

---

we can start with Brute Force. We can go through the strings one by one but **each string has to be store somewhere to compare to other string.** we need to choose the right data structure. we have to find the **anagram which has the same length of letter with the different order**. so **if anagram is sorted, it turn into the same word**. Map is a good option in this case. its time complexity is O(1). so total timecomplexity will be O(m*n)

## Solution

---

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        
        Map<String, List<String>> m = new HashMap();
        
        for(int i = 0; i < strs.length; i++) {
            char[] c = strs[i].toCharArray(); 
            Arrays.sort(c);
            String s = String.valueOf(c); 
            
            if(!m.containsKey(s)) m.put(s, new ArrayList());
            m.get(s).add(strs[i]);
        }
        
        return new ArrayList(m.values());
    }
}
```

## Lesson I learnt

---

1. **Anagram** has the same size of characters but different order → **sort**
2. **Map** can be a good option for **anagram** problem
