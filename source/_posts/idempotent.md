---
title: idempotent
catalog: true
date: 2022-12-03 23:43:58
subtitle:
header-img:
tags:
---


멱등성은 **동일한 요청을 여러번 보냈을 때 동일한 결과를 반환하는 것**을 말한다 

올바르게 구현한 전제하에 `GET`, `HEAD`,  `PUT`, `DELETE` 메소드는 멱등성을 가진다



## 멱등성을 가지는 메소드

멱등성을 판단하는 것은 **반복 호출에 따른 백엔드의 상태 변화 유무**이다 

`GET` 메소드는 멱등하다. 왜냐하면 `GET` 메소드를 여러번 호출해도 백엔드 상태에 변화가 없다

`PUT` 메소드도 멱등하다. 왜냐하면 `PUT` 메소드는 백엔드의 상태를 모두 변경하기 때문이다. 그래서 여러번 호출해도 동일한 결과가 나온다.

```
PUT /modify_row HTTP/1.1   -> Returns 200 if idX exists
PUT /modify_row HTTP/1.1   -> Returns 404 as it just got deleted
PUT /modify_row HTTP/1.1   -> Returns 404
```



`DELETE` 메소드도 멱등하다. 왜냐하면 첫번째 데이터 삭제 이후, 계속 호출해도 백엔드 상태에는 변화가 없다. 물론 데이터가 삭제될 때 `200` 혹은 `204` 응답이 오고, 이후부터는 `404`가 오게되지만 멱등성과는 연관이 없다

```
DELETE /idX/delete HTTP/1.1   -> Returns 200 if idX exists
DELETE /idX/delete HTTP/1.1   -> Returns 404 as it just got deleted
DELETE /idX/delete HTTP/1.1   -> Returns 404
```



`POST` 메소드는 멱등하지 않다. 왜냐하면 `POST`를 여러번 호출할 경우 여러개의 열을 추가하기 때문이다

```
POST /add_row HTTP/1.1
POST /add_row HTTP/1.1   -> Adds a 2nd row
POST /add_row HTTP/1.1   -> Adds a 3rd row
```



> 왜 멱등성이 중요한가?
>
> CQRS와 관련있나?



## 안전한 메소드

멱등성이 있는 메소드는 안전한 메소드라 부른다

안전함은 HTTP 메서드가 **상태를 바꾸지 않으면 안전하다**고 말한다

즉, 안전한 메소드는 멱등성을 자연스럽게 가지게 된다

예를들어 `GET` 은 서버의 상태를 바꾸지 않으므로 안전한 메소드이다

그래서 `GET` 은 멱등성을 가지는 메소드이기도 하다.

 `PUT`, `DELETE`는 서버의 상태를 바꾸므로 안전하지 않은 메소드지만 반복요청시 상태가 그대로 이므로 멱등한 메소드이다. 

정리하면 **안전한 메소드는 상태를 변경하지 않는 메소드**, **멱등한 메소드는 반복했을 때 상태가 변하지 않는 메소드**이다. 



## 출처

https://developer.mozilla.org/ko/docs/Glossary/Idempotent

https://datatracker.ietf.org/doc/html/rfc7231#section-4.2.2

