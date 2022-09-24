---
title: "191.\_Number of 1 Bits"
catalog: true
date: 2022-09-24 15:09:17
subtitle:
header-img:
tags:
---
## Conditions

---

- Write a function that takes an unsigned integer
- returns the number of '1' bits

## Solve by hands first

---

we can use one of bit manipulation operator. we can shift input bits to right and compare with AND operator. if result is 1 then we can count them

| a | b | a & b (AND) | a | b (OR) | a ^ b (XOR) |
| --- | --- | --- | --- | --- |
| 0 | 0 | 0 | 0 | 1 |
| 0 | 1 | 0 | 1 | 0 |
| 1 | 0 | 0 | 1 | 0 |
| 1 | 1 | 1 | 1 | 1 |

## Solution

---

```java
public class Solution {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
      int count = 0;
        while(n != 0) {
            if((n & 1) == 1) count++;
            n = n >>> 1;
        }
        return count;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31e66524-1c64-4167-b3d4-e033f26dda44/Untitled.png)

## Lesson I learnt

---

- there is **no unsigned integer** type in Java
- In Java, the compiler represents the signed integers using [2's complement notation](https://en.wikipedia.org/wiki/Two%27s_complement)
- **2’s completement notation**
    - to get negative value in java there are two steps
        1. **switch every 0 to 1 and 1 to 0 (complementation)**
        2. **add 1 (&)**
    - for instance to make -7 from 7
    
    ```java
    					0111 -> 4 + 2 + 1 = 7
    switch    1000
    add 1        1
    --------------
              1001 -> -8 + 1 = 7 
    ```
    
- `>>` vs `>>>`
    - `>>` : signed right shift
    - `>>>` : unsigned right shift
    - [https://docs.oracle.com/javase/tutorial/java/nutsandbolts/opsummary.html](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/opsummary.html)
- what is signed and unsigned number?
    - **signed value** : postive or negative
        - left most bit is signbit
        - `0` : positive
        - `1` : negative
    - **unsigned value** : only positive
- why do we need signed and unsgined number?
    - 

| name | value | range | operator | java support |
| --- | --- | --- | --- | --- |
| signed | positive (0) & negative (1) | -2^32-1 ~ 2^32-1  | >> | o |
| unsigned | positive | 0 ~ 2^32-1 | >>> | x |
- how to handle minus bits
