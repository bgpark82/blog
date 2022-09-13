---
title: "48.\_Rotate Image"
catalog: true
date: 2022-09-14 01:05:58
subtitle:
header-img:
tags:
---
## Conditions

---

- rotate the image by 90 degree (clockwise)
- DO NOT allocate another 2D matrix and do the rotation.
- matrix has the same length of rows and colums

## Solve by hands first

---

## Solution

---

```java
class Solution {
    public void rotate(int[][] m) {
        
        int l = 0, r = m.length - 1;
        
        while(l < r) {
            for(int i = 0; i < r - l; i++) {
                int t = l, b = r;
                
                int tmp = m[t][l+i];
                m[t][l+i] = m[b-i][l];
                m[b-i][l] = m[b][r-i];
                m[b][r-i] = m[t+i][r];
                m[t+i][r] = tmp;
            }
            l++; r--;    
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6aed74aa-881d-4834-a98c-45d412a716cf/Untitled.png)

range of left and right index is 0 to 3. The coordination to be replaced will be (0,0) (0,3) (3,3) (3,0) and (0, 1) (1, 3) (3, 2) (2, 0) in the next loop. in general form will be (0,0 + i ) (0 + i,3) (3,3 - i) (3 - i,0). the range of index i is 0 to 2 so we can reuse the left and right index

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7687e7f2-d272-4e68-9afc-f686d4d55363/Untitled.png)

after processing most outer layer of the array, we will move on to the inner layer. **To do this, we can increase left index and decrease right index**.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ab5a6c73-8cdf-47dd-b520-72a822c6aadf/Untitled.png)

## Lesson I learnt

---

1. it’s import to have a valid range for matrix problem

```java
while(l < r) {
    for(int i = 0; i < r - l; i++) {

    }
    l++; r--;    
}
```

1. use the actual number at first 

```java
while(l < r) {
    for(int i = 0; i < r - l; i++) {
        int t = l, b = r;
        
        int tmp = m[0][0];
        m[0][0] = m[3][0];
        m[3][0] = m[3][3];
        m[3][3] = m[0][3];
        m[0][3] = tmp;
    }
    l++; r--;    
}
```
