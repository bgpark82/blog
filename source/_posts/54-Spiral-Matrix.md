---
title: "54.\_Spiral Matrix"
catalog: true
date: 2022-09-15 20:19:35
subtitle:
header-img:
tags:
---
## Conditions

---

- return all of the elements of matrix in spiral order

## Solve by hands first

---

In the most of the matrix problem, we have to consider the four ways (left → right, top → bottom, right → left, bottom → top)

```java
left -> rigth : m[0][0] m[0][1] m[0][2]
top -> bottom : m[1][2] m[2][2]
right -> left : m[2][1] m[2][0]
bottom -> top : m[1][0]
```

then we can find some patterns

```java
left -> rigth : m[top][0]    m[top][1]    m[top][2]
top -> bottom : m[1][right]  m[2][right]
right -> left : m[bottom][1] m[bottom][0]
bottom -> top : m[1][left]
```

in each row or column index, there are the fixed value (top, right, bottom, left) and its columns or rows are increased or decreased. range of the increase or decrease value can be (bottom - top) or (right - left)

```java
left -> rigth : m[top][0]    m[top][1]    m[top][2] (right - left)
top -> bottom : m[1][right]  m[2][right]            (bottom - top)
right -> left : m[bottom][1] m[bottom][0]           (right - left)
bottom -> top : m[1][left]                          (bottom - top)
```

after checking the most outlayer done, all we need to do is narrow down the scope (left++, right--, top++, bottom--)

## Solution

---

```python
class Solution {
    public List<Integer> spiralOrder(int[][] m) {
        
        List<Integer> list = new ArrayList();
        
        int t = 0, b = m.length-1, l = 0, r = m[0].length-1;
        
        while(l <= r && t <= b) {
            
            for(int i = l; i <= r; i++) { 
                list.add(m[t][i]);
            }
            t++;
            
            for(int i = t; i <= b; i++) {
                list.add(m[i][r]);
            }
            r--;

            for(int i = r; t <= b && i >= l; i--) {
                list.add(m[b][i]);
            }
            b--;
            
            for(int i = b; l <= r && i >= t; i--) {
                list.add(m[i][l]);
            }
            l++;
        }
        
        return list;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eff2ad78-06df-40f2-b00b-655469371d5b/Untitled.png)

## Lesson I learnt

---

1. we have to consider four different ways in matrix problem 
    - left → right
    - top → bottom
    - right → left
    - bottom → top
2. In case of top is greater than bottom. so it has to be skipped

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/45e05793-13f6-4fee-87e5-b21107b848cd/Untitled.png)

```java
// t = 2, b = 1 
for(int i = r; t <= b && i >= l; i--) { 
    list.add(m[b][i]);
}
b--;

for(int i = b; l <= r && i >= t; i--) {
    list.add(m[i][l]);
}
l++;
```
