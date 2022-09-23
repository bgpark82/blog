---
title: "371.\_Sum of Two Integers"
catalog: true
date: 2022-09-23 20:36:05
subtitle:
header-img:
tags:
---
## Conditions

---

- return *the sum of the two integers without using the operators* `+` *and* `-`

## Solve by hands first

---

if we take a look at the first digit of plus operation, **it would act like XOR operation**. both of the digits are the same then it will have 1 in the output. otherwise only one of the two digits is 1 then we will have a 1 in the output 

- 0 + 0 = 0
- 1 + 1 = 0
- 1 + 0 = 1
- 0 + 1 = 1

but in case of 1 + 1, we will have a carry after operation. **we need to add 1 as carry at the left digit**. we can use AND operation to have 1 in the ouput and shift to left and add up to the result of OXR operation.

- 1 + 1 = 1

For instance 2 + 2 = 4 and 10 + 10 = 100 as binary form

```java
		      10 
		      10
-------------
XOR	      00
AND << 1 100
-------------
         100
```

if result of **AND << 1 opration is 0 which mean there is nothing to carry**.(more precisly the result of AND operation since << 1 won’t make any changes) so at that moment we can return the result

## Solution

---

```java
class Solution {
    public int getSum(int a, int b) {
        while(b != 0) {
            int tmp = (a & b) << 1;
            a = a ^ b;
            b = tmp;
        }
        return a;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/97d780a7-2225-4635-82a6-051956034cb1/Untitled.png)

## Lesson I learnt

---

1. bite manipulation
    - [https://www.hackerearth.com/practice/basic-programming/bit-manipulation/basics-of-bit-manipulation/tutorial/](https://www.hackerearth.com/practice/basic-programming/bit-manipulation/basics-of-bit-manipulation/tutorial/)

| X | Y | X & Y (AND) | X | Y (OR) | X ^ Y (XOR) | ~(X) |
| --- | --- | --- | --- | --- | --- |
| 0 | 0 | 0 | 0 | 0 | 1 |
| 1 | 0 | 0 | 1 | 1 | 0 |
| 0 | 1 | 0 | 1 | 1 | 1 |
| 1 | 1 | 1 | 1 | 0 | 0 |
1. XOR  = a ^ b 
    - one of the value has 1 then it will have 1 in the output
    - otherwise return 0
2. AND = a & b
    - both of the digits are 1 then return 1
    - otherwise return 0
