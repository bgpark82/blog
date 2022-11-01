---
title: "22.\_Generate Parentheses"
catalog: true
date: 2022-11-01 20:10:35
subtitle:
header-img:
tags:
---
### **Requirements**

---

- write a function to *generate all combinations of well-formed parentheses with n pair of parentheses*

### **Edge cases**

---

- 2 → [(()),()()]
- 3 → [((())),(())(),()(()),()()()]

### **Brute force approach (Discussion + Complexity)**

---

Most of combination problem can be solved either dynamic programming or dfs. given n means pairs of parentheses. so we have n number of open bracket and close bracket each. we can make combination of open and close bracket pair with dfs algorithm. we can start with n open bracket and n close bracket first. Adding open bracket is only possible when open bracket is remaining (open > 0) and ***number of open is less than number of close bracket because if there are more open bracket, there is no way to close!*** The base case is when both open and close bracket is 0. Time complexity is O(2^n) and space complexity is O(1)

![684B2A94-DDD9-4914-B4D7-106982420C50.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0730331e-4d23-4785-b67a-7040c5811b6e/684B2A94-DDD9-4914-B4D7-106982420C50.jpeg)

### **Optimised approach (Discussion + Complexity) …X times**

---

*the interviewer can provide you with some hints to unblock you*

*it’s okay to ask the interviewer for direction.*

### **Coding**

---

```java
List<String> res = new ArrayList();

public List<String> generateParenthesis(int n) {
    dfs("", n, n);
    return res;
}

private void dfs(String s, int open, int close) {
		if(open > close) return;
		if(open > 0) dfs(s + "(", open - 1, close);
		if(close > 0) dfs(s + ")", open, close - 1);
		if(open == 0 && close == 0) {
        res.add(s);
        return;
		}
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cad1d62b-8223-4517-8366-dcb5fa0077da/Untitled.png)

### What I learned

---

1. In bracket problem, number of open brackets is always bigger than number of close brackets

```java
(() // there is no way to make complete bracket
```
