---
title: "1048.\_Longest String Chain"
catalog: true
date: 2022-10-26 21:20:43
subtitle:
header-img:
tags:
---
### **Requirements**

---

- words consists of lowercase english letters
- return the length of the longest possible predecessor words chain

### **Edge cases**

---

- [a,b,bc,bcd,da,bcad] → [b,bc,bcd,bcad]

### **Brute force approach (Discussion + Complexity)**

---

We can start with Brute force way first. first things first, sort the array by the length of word in ascending order. because to find the the longest predecessor word chain, the length of word has to be incremented. loop through array and check if there was predecessor in previous loop. in order to find the predecessor we need to store the predecessors in the data structure. I choose the map to store word as key and the length of the longest possible word chain as value. all we need to do is finding a possbile predecessor in each word. we can find the previous length of the longest possible word chain with current word’s combination of predecessor (it’s dynamic programming approach actually) 

```java
Map<String,Integer> dp = new HashMap();
Arrays.sort(words, (a, b) -> a.length() - b.length());
int res = 0;
for(String word : words) {
	int max = 0;
	for(int i = 0; i < word.length(); i++) {
		String prev = word.substring(0, i) + word.substring(i + 1);
		max = Math.max(max, dp.getOrDefault(prev, 0) + 1);
	}
	dp.put(word, max);
	res = Math.max(res, max);
}
return res;
```

### **Optimised approach (Discussion + Complexity) …X times**

---

### **Coding**

---

```java
class Solution {
    public int longestStrChain(String[] words) {
        Map<String,Integer> dp = new HashMap();
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        int res = 0;
        for(String word : words) {
            int max = 0;
            for(int i = 0; i < word.length(); i++) {
                String prev = word.substring(0, i) + word.substring(i + 1);
                max = Math.max(max, dp.getOrDefault(prev, 0) + 1);
            }
            dp.put(word, max);
            res = Math.max(res, max);
        }
        return res;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9b6a625e-4354-44ef-82ee-f8e18ebeed1a/Untitled.png)

### What I learned

---

1. This problem tought me the importance of the brute force first before coming up with optimized solution first. because there is case that no further possible optimization solution. then we can go back to brute force way instead. it helps us to feel ensecure about having at least one right solution
2. If you can store any sub problem/answer and bring it out for current problem, then its called dynamic programing approach

```java
Map<String, Integer> dp = new HashMap<>();

for (String word : words) {
    for (int i = 0; i < word.length(); ++i) {
        best = Math.max(best, dp.getOrDefault(prev, 0) + 1); // find the sub problem/answer from the cache
    }
    dp.put(word, best); // store current sub problem/answer
}
```
