---
title: SQL Injection
catalog: true
date: 2022-11-12 15:14:48
subtitle:
header-img:
tags:
---

## 정의
SQL Injection은 **데이터베이스에 악의적인 SQL문을 삽입하여 공격**하는 행위이다



## 종류

### 1. Error based SQL Injection

Error based SQL Injection은 **쿼리문의 논리적인 에러**를 이용하여 악의적인 SQL을 주입하는 방법이다

```sql
SELECT * FROM member
WHERE (id = 'id' AND password = 'password') OR 1=1
```

`1=1`은 항상 참이므로 `member`의 모든 내용을 조회가능하다



## 대응

### 1. Parameter Binding

SQL Injection은 **Parameter Binding**으로 해결가능하다
JDBC는 `PrepareStatement`를 제공하여 타입에 맞는 파라미터만 바인딩할 수 있게한다

MyBatis에서는 SQL Mapper에 `$` 대신  `#` 문법을 사용한다. `$`문법은 Parameter를 쿼리문에 바로 입력되고 `#` 문법은 Parameter에 `'`로 감싸기 때문에 SQL Injection이 불가능하다

JPA의 API는 내부적으로 Parameter Binding을 하고 있다. 하지만 쿼리문을 직접 사용한다면 SQL Injection의 대상이 될 수도 있다



## 출처

https://noirstar.tistory.com/264

https://en.wikipedia.org/wiki/SQL_injection

https://www.youtube.com/watch?v=OUGrSB0CAxs&ab_channel=SQL%EC%A0%84%EB%AC%B8%EA%B0%80%EC%A0%95%EB%AF%B8%EB%82%98

https://www.youtube.com/watch?v=qzas_-u4Nxk&ab_channel=%EC%9A%B0%EC%95%84%ED%95%9CTech

