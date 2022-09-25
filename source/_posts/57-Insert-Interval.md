---
title: "57.\_Insert Interval"
catalog: true
date: 2022-09-25 11:50:20
subtitle:
header-img:
tags:
---
## Conditions

---

- Return `intervals` *after the insertion*
- 0 <= starti <= endi <= 105
- `intervals` is sorted by `starti` in **ascending** order.

## Solve by hands first

---

if the start point of new intervals is greater than start and end point of current interval then it can not be overlapped. start point of new interval is greater than equals to start of current interval and less than end of current interval which means it is overlapping interval. now we can overlap the current interval and new interval which has new start and end point. it’s important to understand that **we should compare to newly created interval and next current interval**. for instance current interval is [3, 5] and new interval is [4, 8], after overlapping new interval becomes [3, 8] now we have to compare with this values.

![1A0BBFAC-7F63-4FFA-B4E3-C5FC5A7E6E00.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/05bfd3b3-dd79-4bb2-8873-8cdf33ff492c/1A0BBFAC-7F63-4FFA-B4E3-C5FC5A7E6E00.jpeg)

## Solution

---

```java
class Solution {
    public int[][] insert(int[][] inv, int[] nInv) {
        List<int[]> list = new ArrayList();
        
        for(int i = 0; i < inv.length; i++) {  
            int cs = inv[i][0];
            int ce = inv[i][1];
            
            int ns = nInv[0];
            int ne = nInv[1];
            
            if(ce < ns) {
                list.add(inv[i]);
            } else if(cs > ne) {
                list.add(nInv);
                // insert the rest of interval
                for(;i < inv.length; i++) {
                    list.add(inv[i]);
                }
                return toMetrix(list);
            } else {
                // overlapping
                nInv[0] = Math.min(cs, ns);
                nInv[1] = Math.max(ce, ne);
            }
        }   
        
        list.add(nInv);
        
        return toMetrix(list);
    }
    
    private int[][] toMetrix(List<int[]> list) {
        int[][] ans = new int[list.size()][2];
        for(int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/350b4862-fd6c-4c02-9ad3-eab435f18454/Untitled.png)

## Lesson I learnt

---

1. define the edge case first!
    
    ```java
    if(ce < ns) {
    	// current interval is alway less than new interval	= edge case
    } else if(cs > ne) {
    	// current interval is alway greater than new interval = edge case
    } else {
    	// overlapping case
    }
    ```
    
2. can return within the loop if it doesn’t need to iterate to the end
    
    ```java
    else if(cs > ne) {
        list.add(nInv);
        for(;i < inv.length; i++) {
            list.add(inv[i]);
        }
        return toMetrix(list); // we don't need to check rest of metrix
    }
    ```
    
3. in case of return in the middle of loop, there must be cases that did not check the condition has the return statement. so we have to add the logic in the condition out of loop 
    
    ```java
    for(int i = 0; i < inv.length; i++) {        
          if(ce < ns) {
              list.add(inv[i]);
          } else if(cs > ne) {
              list.add(nInv);  // this can not be called!
              for(;i < inv.length; i++) {
                  list.add(inv[i]);
              }
              return toMetrix(list);
          } else {
              nInv[0] = Math.min(cs, ns);
              nInv[1] = Math.max(ce, ne);
          }
      }   
      
      list.add(nInv);  // so we have to add that logic
    ```
