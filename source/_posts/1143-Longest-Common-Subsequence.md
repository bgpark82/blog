---
title: "1143.\_Longest Common Subsequence"
catalog: true
date: 2022-09-11 16:39:47
subtitle:
header-img:
tags:
---
## Conditions

---

- find the longest subsequnce of both of text1 and text2
- new string is generated from the original string **without change order**

## Solve by hands first

---

we will start from **Brute Force** algorithm with multi indices. we need 2 pointer for each String. if each character at pointer is not equal, then move the pointer to next index. **Problem of this solution is we can’t decide which index should we move first**. 

but how can I decide which index should I move?

is the distinct value in array?

can it be different order?

- I can sort the string first then compare

Next solution can be using **alphabet array**. initialize the integer array with size 26 with -1. increase the index value +1 with text1. **Problem of the solution is it doesn’t care of order**. so It ignore the order, then we will get unexpected result

## Solution

---

```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        return dp(text1, text2, 0, 0);
    }
    
    int dp(String text1, String text2, int i, int j) {
        if(i == text1.length() || j == text2.length()) return 0;
        
        if(text1.charAt(i) == text2.charAt(j)) return 1 + dp(text1, text2, i + 1, j + 1);
        
        else return Math.max(dp(text1, text2, i + 1, j), dp(text1, text2, i, j + 1));
    }
}
```

![67D53F86-2742-4880-8EBC-FAF283417715.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bcf39165-2848-4ed2-9275-e88183243f89/67D53F86-2742-4880-8EBC-FAF283417715.jpeg)

I ended up used Dynamic Programing. the goal is to find the longest subsequence so we can divide into sub problem. **The sub problem can be defined by finding the longest subsequence and return the length of subsequence**. the we can compare the return value of sub problems since it only divided by 2 sub problems. If one of the sub problem returns longer subsequence

### Top Down

```java
class Solution {
    
    int[][] m;
    
    public int longestCommonSubsequence(String text1, String text2) {
        m = new int[text1.length()][text2.length()];
        
        return dp(text1, text2, 0, 0);
    }
    
    int dp(String text1, String text2, int i, int j) {
        if(i == text1.length() || j == text2.length()) return 0;
        
        if(m[i][j] != 0) return m[i][j];
        
        if(text1.charAt(i) == text2.charAt(j)) {
            return m[i][j] = 1 + dp(text1, text2, i + 1, j + 1);
        } else {
						return m[i][j] = Math.max(dp(text1, text2, i + 1, j), dp(text1, text2, i, j + 1));
				}
    }
}
```

**Problem of this solution is there are duplicate sub problems**. for instance, “ee” appears twice in dp. It can be appeared many time and ended up exceeding time limit. So **we have to use memoization to reduce i**t.

![6326514A-69EC-4BD2-B5EB-00965F07EFC4.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/953f2d58-9a79-4c77-929b-e4afe73bcf00/6326514A-69EC-4BD2-B5EB-00965F07EFC4.jpeg)

Since dp uses stack. it goes to one side and travel back to next sub problem 

```java
return m[i][j] = Math.max(
		dp(text1, text2, i + 1, j), // check this method to the end
		dp(text1, text2, i, j + 1)  // travel back to this method
);

```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/98518a7c-e9bb-4ae4-95b1-7917ca1e4ef7/Untitled.png)

### Bottom Up

```java
class Solution {
    
    int[][] m;
    
    public int longestCommonSubsequence(String text1, String text2) {
        m = new int[text1.length()+1][text2.length()+1];
        
        for(int i = 1; i <= text1.length(); i++) {
            for(int j = 1; j <= text2.length(); j++) {
                if(text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    m[i][j] = 1 + m[i - 1][j - 1];
                } else {
                    m[i][j] = Math.max(m[i - 1][j], m[i][j - 1]);
                }   
            }
        }
        
        return m[text1.length()][text2.length()];
    }
}
```

![C09A627F-47BF-4C8E-916D-378242C6247D.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/adff9992-f002-4a32-9d83-4102ce293300/C09A627F-47BF-4C8E-916D-378242C6247D.jpeg)

we also can use bottom up way. it check the value at i = 1, j = 1 so It covers two different cases when it meets condition. the condition is whether the value at i = 0, j = 0 on both of letter in text1, text2 is the same or not 

1. **if it is the same** → retrieve value at **i = 0, j = 0**
2. **if it is not the same** → retrieve value at **i = 1, j = 0** or **i = 0, j = 1**

if it reaches to the last index, it will have to the longest length of the subsequence. **it is important that array m has extra space to store the calculated longest length**. so the size of array is bigger than length of each text which are `text1.length() + 1`, `text2.length() + 1` 

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/aa9eabc6-e550-4ebc-825f-cf5a2657de10/Untitled.png)

Why bottom up is faster than top down?

- My initial thoguth was top down is faster than bottom up since we don’t need to loop throught whole array. But I assume that memory stack takes more time

## Lesson I learnt

---

1. **Dynamic programing** is part of **Stack**.
    - it goes **all the way down to the end of condition** and travel back up to the node has next branch.
2. We need to **cache the value** and **return the cached value** for memoization 
    
    ```java
    int dp(String text1, String text2, int i, int j) {        
        if(m[i][j] != 0) return m[i][j];
        
        if(text1.charAt(i) == text2.charAt(j)) {
            return m[i][j] = 1 + dp(text1, text2, i + 1, j + 1); // caching and return
        } else {
    				return m[i][j] = Math.max(dp(text1, text2, i + 1, j), dp(text1, text2, i, j + 1));
    		}
    }
    ```
    
3. bottom up needs extra space
    - The cache array needs to bigger than original array to store the return value at the last index
    - it usually starts from the 1 more large index and compare to smaller index
        - target = (1,1)
        - compare = (0,0), (1,0), (0,1)
    
    ```java
    class Solution {
        
        int[][] m;
        
        public int longestCommonSubsequence(String text1, String text2) {
    
            m = new int[text1.length()+1][text2.length()+1]; 
            
            for(int i = 1; i <= text1.length(); i++) {
                for(int j = 1; j <= text2.length(); j++) {
                    if(text1.charAt(i - 1) == text2.charAt(j - 1)) {
                        m[i][j] = 1 + m[i - 1][j - 1];
                    } else {
                        m[i][j] = Math.max(m[i - 1][j], m[i][j - 1]);
                    }   
                }
            }
            
            return m[text1.length()][text2.length()];
        }
    }
    ```
