---
title: "268.\_Missing Number"
catalog: true
date: 2022-09-22 17:59:31
subtitle:
header-img:
tags:
---
## Conditions

---

- array `nums` containing `n` distinct numbers in the range `[0, n]`
- return *the only number in the range that is missing from the array.*

## Solve by hands first

---

we can create the cache array with size of nums.length + 1. iterate throught the integer array nums and increase the value of cache array at the value of nums as index. Time complexity will be O(n) and space complexity would be O(n)

## Solution

---

```java
class Solution {
    public int missingNumber(int[] nums) {

        int[] t = new int[nums.length + 1];
        
        for(int i = 0; i < nums.length; i++) {
            t[nums[i]]++;
        }
        
        for(int i = 0; i < t.length; i++) {
            if(t[i] == 0) return i;
        }
        
        return nums.length;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5341df08-e709-4fed-99c0-b589b5b07ce1/Untitled.png)

```java
class Solution {
    public int missingNumber(int[] nums) {
        int sum = 0;
        for(int i = 1; i <= nums.length; i++) {
            sum += i;
        }
        for(int i = 0; i < nums.length; i++) {
            sum -= nums[i];
        }
        return sum;
    }
}
```

**we can simply find the sum of 0 to n and take out the sum of the value of nums array**. Time complexity is O(n) and space complexity is O(1). below code is the simplified form of above code

```java
class Solution {
    public int missingNumber(int[] nums) {
        int sum = nums.length;
        for(int i = 0; i < nums.length; i++) {
            sum += i - nums[i];
        }
        return sum;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/60f46f39-d0bd-4a3f-93a8-af0387677a12/Untitled.png)

## Lesson I learnt

---

1. calculate sum of array and take out helps sometimes
    
    ```java
    for(int i = 0; i < nums.length; i++) {
        sum -= nums[i];
    }
    ```
    
2. we can use math to reduce the space complexity
