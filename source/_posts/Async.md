---
title: '@Async'
catalog: true
date: 2022-11-15 21:48:45
subtitle:
header-img:
tags:
---

최근 Pub/Sub 레거시 코드를 리펙토링하면서 사용하게된 @Async에 대해 한번 분석해보았습니다.

## @Async

Async는 스프링 빈 메소드가 비동기적으로 동작하도록 하는 어노테이션입니다.



## 사용법

간단한 사용법은 다음과 같습니다.

```java
```



## 내부 구조

@Async는 기본적으로 AOP 기반으로 동작합니다. 

![image-20221115220957846](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221115220957846.png)

@Async는 기본적으로 AOP Proxy 기반으로 동작합니다. 

![image-20221115221653032](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221115221653032.png)

Advisor로 `AsyncAnnotationAdvisor`, Advice는 `AnnotationAsyncExecutionInterceptor`를 사용합니다. Advice인 `AnnotationAsyncExecutionInterceptor`는  `AsyncExecutionInterceptor`를 상속받습니다. `AsyncExecutionInterceptor`는  Advice인 부가로직을 실행하는 `invoke`를 가지고 있으므로 `invoke`를 통해 @Async의 동작방식을 확인해보겠습니다.

![image-20221115233044079](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221115233044079.png)

주요 로직은 AsyncTaskExecutor를 받아 doSubmit을 실행시키는 것입니다. AsyncTaskExecutor는 Executor 중 하나로 ThreadPool이라고 생각하면 편합니다. 스프링 빈에는 ThreadPoolTaskExecutor으로 등록되며 내부적으로는 ThreadPoolExecutor으로 동작합니다. 기본적으로 corePoolSize = 8, LinkedBlockingQueue를 가져 fixedThreadPool처럼 동작하게됩니다. 

> 만약 짧은 시간동안 동작하는 비동기 작업이 많다면 기본 스레드풀(ThreadPoolTaskExecutor) 대신 cachedThreadPool을 커스텀하게 사용한다면 성능상 효과를 볼 수 있습니다.  @EnableAsync 대신 수동으로 ThreadPool을 등록하는 방법은 이후에 소개하겠습니다.

 doSubmit 메소드는 `AsyncExecutionInterceptor`의 추상클래스인 `AsyncExecutionAspectSupport`에서 실행됩니다. 

![image-20221115234244864](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221115234244864.png)

doSubmit은 @Async 어노테이션을 가진 메소드의 리턴 타입에 따라 다르게 동작합니다.

1. CompletableFuture
2. ListenableFuture
3. Future
4. void

void의 경우, ThreadPool을 submit하여 스레드의 작업을 실행 시키게 됩니다. 메소드의 리턴타입이 없는 fire and forget 메소드의 경우 값을 반환하지 않게 됩니다.



## 수동 설정

```java
```





## 주의사항

@Async 어노테이션은 AOP Proxy 방식으로 동작하므로 기본적으로 AOP의 제약사항을 그대로 따르게 됩니다.

1. private 메소드에 적용불가
2. 같은 객체 내 메소드끼리 호출 시 동작하



## 정리

@Async는 AOP Proxy를 기반으로 동작합니다

내부적으로는 ThreadPool을 사용하여 스레드 자원을 효율적으로 사용할 수 있습니다.





https://woodcock.tistory.com/31

https://steady-coding.tistory.com/611

https://brunch.co.kr/@springboot/401

https://dzone.com/articles/effective-advice-on-spring-async-part-1
