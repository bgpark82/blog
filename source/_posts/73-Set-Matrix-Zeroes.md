---
title: "73.\_Set Matrix Zeroes"
catalog: true
date: 2022-10-05 19:49:51
subtitle:
header-img:
tags:
---
## Conditions

---

- if an element is `0`, set its entire row and column to `0`'s.
- You must do it [in place](https://en.wikipedia.org/wiki/In-place_algorithm).

## Solve by hands first

---

iterate matrix from 0,0. if element at index is 0, makes rows and column to 0. 

The problem is revisiting. **how can we know that entire row and column are 0 when current value is 0 without auxiliary data structure**? Time complexity is O(m*n*m*n)

we can create the temp matrix `visited[m][n]` in order to check if the element was 0 in the first place. Loop throught the `matrix` and mark 1 at `visited` . Iterate through whole matrix again, if the value in `visited` is 1 then we can fill `matrix` and `visited` with 0, -1 each. it needs to skip when the value in `visited` is 1. because we have to distinguish after filling

Time complexity is O(m*n) and space complexity is also O(m*n)

## Solution

---

```java
class Solution {
    
    int[][] visited;
    
    public void setZeroes(int[][] matrix) {
        
        visited = new int[matrix.length][matrix[0].length];
        
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j] == 0) {
                    visited[i][j] = 1;
                }
            }
        }
        
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                if(visited[i][j] == 1) {
                    fill(matrix, i, j);
                }
            }
        }
    }
    
    private void fill(int[][] matrix, int i, int j) {
        for(int x = 0; x < matrix.length; x++) {
            if(visited[x][j] == 1) continue;
            matrix[x][j] = 0;
            visited[x][j] = -1;
        }
        
        for(int y = 0; y < matrix[0].length; y++) {
            if(visited[i][y] == 1) continue;
            matrix[i][y] = 0;
            visited[i][y] = -1;
        }
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6ec99712-21b3-448e-85e5-d4f7378bd845/Untitled.png)

## Lesson I learnt

---

1. [In-place algorithm](https://en.wikipedia.org/wiki/In-place_algorithm)
    - transform input instead of using extra data structure
