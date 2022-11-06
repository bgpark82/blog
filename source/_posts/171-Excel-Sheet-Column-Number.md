---
title: "171.\_Excel Sheet Column Number"
catalog: true
date: 2022-11-06 22:40:09
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return corresponding column number

### **Edge cases**

---

- A -> 26*0 + 1
- Z -> 26*0 + 26
- A A -> 26*1 + 1
- A Z -> 26*1 + 26
- B A -> 26*2 + 1
- B Z -> 26*2 + 26
- Z A -> 26*26 + 1
- Z Z -> 26*26 + 26
- AAA -> 26*26*1 + 26*1 + 1
- AAZ -> 26*26*1 + 26*1 + 26
- ABA -> 26*26*1 + 26*2 + 1
- AZA -> 26*26*1 + 26*26 + 1
- AZZ -> 26*26*1 + 26*26 + 26
- AZZ -> 26*26*2 + 26*26 + 26

We can find the pattern based on above examples

> 1st digit = 26^0
2nd digit = 26^1
3rd digit = 26^2
> 

### **Brute force approach (Discussion + Complexity)**

---

We can find the pattern which is each digits represent 26^digis. For instance AAZ is 26^2*1 + 26^1*1 + 26^0*26. Time complexity is O(n) and space complexity is O(1)

```java
int f = 26, sum = 0, len = ct.length();
for(int i = len-1; i >= 0; i--) {
    int n = ct.charAt(i) - 'A' + 1; // 1
    int d = len - i - 1; // 0
    int s = ((int)Math.pow(f,d)) * n;
    sum += s;
}
return sum;
```

### **Optimised approach (Discussion + Complexity) …X times**

---

it increase 26 in every digits so we don’t need use Math.pow. iterate over index 0 and accumulate 26 in every sum

```java
/**
	BCA
	B = 2
	BC = 2*26 + 3
	BCA = (2*26 + 3)*26 + 1 = 2*26*26 + 3*26 + 1
*/
int sum = 0;
for(char c : s.toUpperCase().toCharArray()) {
		sum *= 26;
		sum += c - 'A' + 1;
}
return sum;
```

### **Coding**

---

```java
class Solution {
    public int titleToNumber(String ct) {
        int sum = 0;
        for(char c : ct.toUpperCase().toCharArray()) {
            sum *= 26;
            sum += c - 'A' + 1;
        }
        return sum;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8ff98cbe-ef0c-4f52-a9ba-d5c935f4f3d9/Untitled.png)

### What I learned

---

1. it helps increase certain number in every digits in a single loop

```java
BCA
B = 2
BC = 2*26 + 3
BCA = 2*26*26 + 3*26 + 1

for(char c : ct) {
    sum *= 26; // muliply 26 in every sum;
    sum += c - 'A' + 1; // add value at the end
}
```
