---
title: Thread
catalog: true
date: 2022-11-13 16:03:30
subtitle:
header-img:
tags:
---
# Thread

스레드는 **프로세스 내 실행 흐름의 단위**이다

Thread는 JVM의 Runtime Data Area에서 Stack에 집중된다 

예전에는 JVM Stack과 Native Stack을 구분해서 사용하였다

왜냐하면 JVM Thread와 OS Thread가 나누어져 있었기 때문이다

지금은 **Stack 영역에 JVM용 Frame과 OS용 Frame을 적재**해서 사용한다



>  초기 자바는 JVM이 직접 Thread를 생성하고 관리했다
>
>  JVM이 관리하는 Thread를 Green Thread, OS가 관리하는 Thread를 White Thread(=Native Thread)라고 불렀다
>
>  하지만 Green Thread는 White Thread를 시분할 방식으로 나눠 사용했기 때문에 성능이 좋지않았다
>
>  그래서 1.4버전부터는 **OS Thread만 사용**하기 시작했다
>
>  **Native Thread는 OS가 관리하므로 JVM이 Thread를 자유롭게 관리할 수 없는 단점**도 있다



# Synchronization 동작 방식

synchronization은 **스레드 간 실행 순서를 제어할 수 있는 기술**을 말한다

자바는 Monitor라는 Synchronization(=동기화) 메커니즘을 사용한다

Monitor는 **객체나 코드 블록에서 하나의 Thread만 접근할 수 있는 Lock**을 뜻한다

Monitor는 **상호배제(=Mutual Exclusion)**와 **공동작업(=Cooperation)**을 위해 사용된다



### 1. Mutual Exclusion (=Mutex)

Mutual Exclusion은 **Thread가 공유데이터를 사용하는 동안 다른 Thread가 사용하지 못하도록 배제**하는 것을 뜻한다

공유 데이터는 Runtime Data Area 중 Thread가 공유가능한 **Method Area**와 **Heap** 영역을 뜻한다

특히, Thread가 수정이 가능한 **Heap의 인스턴스 변수**와 **Method Area의 클래스 변수**가 대표적이다

> 데이터베이스의 동기화는 update와 delete 할 때만 동기화 할 수 있다
>
> 자바의 동기화는 **모든 경우에 동기화가 적용**된다



### 2. Critical Section

**Synchronization이 필요한 영역**을 Critical Section이라고 부른다

즉, **공유 데이터를 동기화하기 위해 Thread는 Critical Section으로 들어가야 한다**

이때 Monitor라는 Lock을 획득해야 Critical Section 내 데이터에 접근하여 수정할 수 있다

모든 Object의 Header에는 Lock Counter를 가지고 있다

Thread가 Lock을 획득하면 Lock Count를 1 증가시키고, Lock을 놓으면 1 감소시킨다

Object의 Lock Count가 0이면 다른 Thread가 Lock을 획득할 수 있다

```java
// synchronized method
public synchronized void method() { }

// synchronized statement
public void method() { 
  synchronized(obj) {
    
  }
}
```

자바의 Critical Section은 **Synchronized Statement**와 **Synchronized Method**가 존재한다



### 2-1. Synchronized Statement

Synchronized Statement는 메소드 내부 코드 블록에 `synchronized` 키워드를 사용한다

Synchronized Method는 메소드에 `synchronized` 키워드를 사용한다

즉, `synchronized` 블록을 접근하기 위해는 Monitor Lock을 획득해야 한다

```java
class Sync {

  private int a;

  void syncBlock() {
    synchronized(this) {
      a = 1;
    }
  }
}
```

```java
    Code:
      stack=2, locals=3, args_size=1
         0: aload_0
         1: dup
         2: astore_1
         3: monitorenter // monitor 락 획득
         4: aload_0
         5: iconst_1
         6: putfield      
         9: aload_1
        10: monitorexit // monitor 락 해제
        11: goto          19
        14: astore_2
        15: aload_1
        16: monitorexit // exception이 발생하면 예외 발생 직전에 monitor 락 해제
        17: aload_2
        18: athrow
        19: return
```

`synchronized` 블록에 접근하면 `moniterenter`고 `monitorexit` 이라는 Opcode를 확인할 수 있다

`monitorenter`가 실행되면 **Stack의 Object Reference로 참조된 인스턴스에 대해 Lock을 획득**한다

인스턴스의 Lock Count를 1 증가 시키고 Lock을 획득한다

만약 다른 Thread가 Lock을 획득할 수 없으면 Lock을 획득할 때까지 Block 상태로 대기한다

`monitorexit`이 실행되면 **Lock Count를 1 감소시키고 0이되면 Lock을 해제**한다

두번째 `monitorexit` 은 예외발생 시, 예외발생 직전에 Lock을 해제시키기 위해 사용된다 (try-catch와 비슷한 역할)

Synchronized Statement는 런타임에 Monitor Lock을 획득한다



### 2-2. Synchronized Method

Synchronized Mehod는 **메소드를 선언할 때 `synchronized`를 사용하여 메소드 전체를 Ciritial Section으로 지정**한다

즉, 메소드를 호출할 때 Monitor Lock을 획득한다

```java
class Sync {

  private int a;

  synchronized void syncBlock() {
      a = 1;
  }
}
```

```java
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: iconst_1
         2: putfield      #2                  // Field a:I
         5: return
```

Synchronized Statement와 다르게 바이트 코드에는 Monitor Lock가 명시되지 않는다

Synchronized Method의 Monitor Lock 획득은 메소드의 Symbolic Reference를 Resolution하는 과정에서 결정된다

Synchronized Method는 메소드의 내용이 Ciritical Section이 아니라 **메소드의 호출 자체가 Ciritical Section**이 되기 때문이다

**인스턴스 메소드의 경우는 `this`의 Lock을 획득**하고, **클래스 메소드의 경우 Class에 대해 Lock을 획득**한다



## 3. Cooperation

Cooperation은 **Thread가 Monitor Lock을 해제할 때, 다른 Thread에게 알려주어 Monitor Lock을 획득하도록 협동**(=Cooperation)하는 작업이다

자바에서는 `wait-notify` 형태의 Monitor Lock을 사용한다

![image-20220418222746986](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220418222746986.png)

Entry Set에 대기하는 스레드는 Ciritical Section에 스레드가 존재하지 않으면 지체없이 Monitor Lock을 획득한다

작업을 다 끝내면 객체가 `wait()` 메서드롤 호출하고 해당 스레드는 Monitor를 잠시 놓고 Wait Set에서 기다린다

다른 스레드가 Ciritical Section에서 작업을 하고 객체의 `notify()` 메서드를 실행하여 Wait Set에 대기중이던 스레드를 깨운다

이때 `notify()` 시점에 Ciritical Section에 있던 스레드가 Monitor Lock을 release해야만 깨워진 스레드가 Monitor Lock을 획득할 수 있다

정리하면 스레드가 객체의 `wait()`만 수행하고 Wait Set에 들어가면 Monitor Lock을 획득은 **Entry Set의 스레드들의 경쟁**을 통해 가능하다

스레드가 객체의 `wait()` 이전에 `notify()`를 수행하면 **Entry Set과 Wait Set의 스레드들이 경쟁**하게 된다

> `notify()`와 `notifyAll()` 차이점
>
> `notify()`는 Wait Set의 스레드 중 **임의의 한 스레드만 Monitor Lock 획득 경쟁에 참여**하게 하고
>
> `notifyAll()`은 Wait Set의 **모든 스레드가 Monitor Lock 획득 경쟁에 참여**하게 한다.

## 4. Lock

자바의 Lock은 Heavy Weight Lock과 Light Weight Lock으로 나뉜다

Heavy Weight Lock은 **Monitor Lock**을 의미한다

Monitor Lock은 각 객체에 대해 OS Mutex와 조건 변수 등으로 다소 무겁게 구현되어 있다

> OS Metex?

Light Weight Lock은 **Atomic Lock**을 이용한다

Atomic은 특정 스레드가 실행 중에는 다른 스레드로 부터 간섭받지 않는 것을 말한다

**OS Mutex 같이 리소스를 사용하지 않고 동기화 처리**를 할 수 있다

> 자바의 Atomic은 CAS 알고리즘을 사용한다 (Compared and Swap)
>
> 멀티 스레드 환경에서 CPU는 메인 메모리의 변수가 아닌 CPU 캐시 영역 값을 참조한다
>
> 이때, 메인 메모리 값과 CPU 캐시 값이 차이날 수 있다
>
> CAS 알고리즘은 **스레드에 저장된 값과 메인 메모리 값이 같으면 새로운 값**으로 교체되고, 다르면 재시도한다
>
> `volatile` 키워드는 **CPU 캐시가 아닌 메인 메모리의 값을 참조**한다



https://beomseok95.tistory.com/225
