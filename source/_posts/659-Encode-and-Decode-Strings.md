---
title: 659. Encode and Decode Strings
catalog: true
date: 2022-10-19 21:27:00
subtitle:
header-img:
tags:
---
### **Requirements**

---

- encode a list of strings to string
- decode string to a list of strings

### **Edge cases**

---

- input and output has to be the same
- which character can be belong to string like `,`

### **Brute force approach (Discussion + Complexity)**

---

To encode a list of strings to string, concat the strings in the list with the delimiter like `,` , `:` . [”a”,”b”,”c”] turns into a,b,c. Time complexity for encoding process is O(n), space complexity is O(1) If we use StringBuilder

To decode a string to list of strings, split by the delimiter and turn it into list. For instance a,b,c turns into [”a”,”b”,”c”]. Time complexity is O(n)

```java
// encode
StringBuilder sb = new StringBuilder();
for(int i = 0; i < strs.size(); i++) {
	sb.append(strs.get(i));
	if(i != strs.size()-1) sb.append(",");
}
return sb.toString();
```

```java
// decode
return Arrays.asList(str.split(","));
```

### **Optimised approach (Discussion + Complexity) …X times**

---

But the problem of this approach can get us in trouble. what if there is delimeter in the middle of the string in the list. then we will have different answer

Instead we can use the custome delimiter to solve this issue. it’s length of each string with delimiter. For instance [”abc”,”cd”] turns into 4:abc2:cd. we know the length of following substring. so it’s easy to split the string

```java
// encode
StringBuilder sb = new StringBuilder();
for(String str : strs) {
	sb.append(str.length()).append(":").append(str);
}
return sb.toString();
```

```java
// decode
List<String> list = new ArrayList();
int i = 0;
while(i < str.length()) {
    int j = i;
    while(str.charAt(j) != ':') j++;
    int len = Integer.parseInt(str.substring(i, j));
    list.add(str.substring(j + 1, j + 1 + len));
    i = j + 1 + len;
}
return list;

```

### **Coding**

---

```java
public class Solution {
    
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for(String str : strs) {
            sb.append(str.length()).append(":").append(str);
        }
        return sb.toString();
    }

    public List<String> decode(String str) {
        List<String> list = new ArrayList();
        int i = 0;
        while(i < str.length()) {
            int j = i;
            while(str.charAt(j) != ':') j++;
            int len = Integer.parseInt(str.substring(i, j));
            list.add(str.substring(j + 1, j + 1 + len));
            i = j + 1 + len;
        }
        return list;
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/affd0201-84e9-4c4c-b9ee-572641c99f17/Untitled.png)

### What I learned

---

1. Consider the custom delimiter required since the special character can be within a given string

```java
["a$b","b_e","a:b"]
```
