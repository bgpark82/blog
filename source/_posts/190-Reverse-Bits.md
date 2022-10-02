---
title: "190.\_Reverse Bits"
catalog: true
date: 2022-10-02 14:59:46
subtitle:
header-img:
tags:
---
## Conditions

---

- 32 bits unsigned integer.

## Solve by hands first

---

1. do `&` with the given 32 bits integer and 1
2. shift right the given integer
3. do `&` for cache
4. shift left the cache

For instance, the given 32 bits is 10111

1. 10111 & 1 = 1
2. c = c | 1 (1)
3. n >>> 1 (1011)
4. c << 1 (10)

![88F3E09B-0242-46D2-9D89-87F3960C7F59.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1cff6777-2773-4017-a30c-60f35db8304c/88F3E09B-0242-46D2-9D89-87F3960C7F59.jpeg)

Problem is there is no unsigned shift left(`<<<`) operation in java

so we need to get sign bit from the given bits

```java
int sign = n & (1 << 32);
```

## Solution

---

```java
public class Solution {
    // you need treat n as an unsigned value
    public int reverseBits(int n) {
        int sign = n & (1 << 32);
        
        int c = 0;
        for(int i = 0; i < 32; i++) {
            if((n & 1) == 1) c |= 1;
            n = n >>> 1;
            c = c << 1;
        }
        
        return (c >>> 1) | (sign << 31);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/713c2c04-6f8a-46b9-94d0-b9b8187e14e6/Untitled.png)

## Lesson I learnt

---

1. cache will help for any reverse problem
2. there is **no unsign shift left in java**
    - [https://stackoverflow.com/questions/26246078/difficulty-in-understanding-and-correcting-error-messages](https://stackoverflow.com/questions/26246078/difficulty-in-understanding-and-correcting-error-messages)
