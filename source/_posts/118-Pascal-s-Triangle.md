---
title: "118.\_Pascal's Triangle"
catalog: true
date: 2022-10-31 19:24:38
subtitle:
header-img:
tags:
---
### **Requirements**

---

- return the first numRows of **Pascal's triangle**
- **Pascal's triangle**, each number is the sum of the two numbers directly above it as shown:

### **Edge cases**

---

- 1 → [[1]]
- 2 → [[1],[1,1]]
- 3 → [[1],[1,1],[1,2,1]]
- 4 → [[1],[1,1],[1,2,1],[1,3,4,3,1]]

### **Brute force approach (Discussion + Complexity)**

---

Create the ArrayList to store the each level. Loop through 1 to input number. Each value in iteration represent length of level in pascal triangle.  In every iteration we have to add 1 for the first index and sum of value of i-1, i index in previous level. Time complexity is O(numRows * numRows) and space complexity is O(numRows * numRows)

```java
List<List<Integer>> res = new ArrayList();

for(int i = 1; i <= numRows; i++) {
	List<Integer> list = new ArrayList();
	for(int j = 0; j < i; j++) {
		if(j == 0 || j == i-1) list.add(1);
		else list.add(res.get(i-1).get(j) + res.get(i-1).get(j+1));
	}
	res.add(list);
}
return res;
```

### **Optimised approach (Discussion + Complexity) …X times**

---

*the interviewer can provide you with some hints to unblock you*

*it’s okay to ask the interviewer for direction.*

### **Coding**

---

```java
class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList();
        for(int i = 1; i <= numRows; i++) {
            List<Integer> list = new ArrayList();
            for(int j = 0; j < i; j++) {
                if(j == 0 || j == i-1) list.add(1);
                else list.add(res.get(i-2).get(j-1) + res.get(i-2).get(j));
            }
            res.add(list);
        }
        return res;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c0146532-ca05-48f7-9ae3-fa4970da250f/Untitled.png)

### What I learned

---

1. Brute force approach first is important in array problems
2. determine the meaning of row and column index and relationship between
    - columns and rows

```java
for(int i = 1; i <= numRows; i++) { // i = row of pascal's triangle = width of every row
  for(int j = 0; j < i; j++) { // j = column of pascal's trainable

  }
}
```
