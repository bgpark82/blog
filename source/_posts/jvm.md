---
title: jvm
catalog: true
date: 2022-11-28 23:04:25
subtitle:
header-img:
tags:
---

# 1. JVM이란?

Java Virtual Machine(자바 가상 머신)의 약자이다

JVM은 **자바의 바이트 코드를 실행하는 주체**이다

개발자가 작성한 자바 코드를 java라는 명령어로 실행하면 JVM 위에서 어플리케이션이 동작한다

JVM은 2가지로 분리할 수 있다

1. JDK (Java Development Kit) : 
2. JRE (Java Runtime Environment) : 자바를 실행할 수 있는 환경의 집합



> 정확하게 JVM은 Specification에 지나지 않는다
>
> JVM의 Spec을 바탕으로 벤더사마다 JVM을 다르게 구현할 수 있다

# 2. JVM의 용도

1995년 자바 언어를 만들면서 Write Once, Run Anywahere이라는 목표로 개발되었다

즉, 개발자는 OS에 관계없이 JDK 버전만 맞다면 OS에서 컴파일 후 실행이 가능하다

이는 C가 OS별로 컴파일해야 실행할 수 있었던 점을 보완하기 위함으로 보인다

# 3. JVM 구조

![image-20220408003427674](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220408003427674.png)

JVM은 총 4가지로 구성된다

1. Class Loader
2. Execution Engine
3. Garbage Collector
4. Runtime Data Area

> 그럼 JDK, JRE를 나누는 단위는 뭐지?

## 3-1. Class Loader

**컴파일된 `.class` 코드의 Class들을 JVM의 Runtime Data Area에 적재**하는 역할을 한다.



### 3-1-1. Dynamic Loading

ClassLoader는 Dynamic Loading을 담당하는 주체이다

Dynamic Loading은 **Class가 참조되는 순간 동적으로 메모리에 배치하는 방식**이다

Dynamic Loading은 Load 시점에 따라 Load Time Dynamic Loading과 Runtime Dynamic Loading으로 나뉜다

***Load Time Dynamic Loading***은 Class를 Loading하는 과정에 **관련 Class를 모두 Loading**한다. 

예를들어, main 메소드를 실행하면 내부적으로는 System 클래스를 사용하고 args로 String을 받으므로 JVM 구동 시 해당 클래스를 모두 호출한다

***Runtime Dynamic Loading***은 소스 코드에서 **객체를 참조하는 순간 동적으로 Loading**한다 

예를들어, 메소드를 호출할 때, 파라미터로 무엇이 넘어올지는 호출되는 시점에 알 수 있다. 



### 3-1-2. Name Space

Class Loader는 JVM으로 Class를 Load하고 배치하는 역할을 한다

하지만 **동일한 Class를 중복으로 Loading하지는 않는다**

자바는 **Class의 이름**으로 JVM에 이미 Class가 Load되었는지 확인한다

Class의 이름은 Class Loader Name + Package Name + Class Name이 동일해야 같은 클래스가 된다

```java
com.bgpark.domain.Person.class
```

하지만, Class Loader는 **JVM의 모든 Class의 이름을 확인하는 대신 Namespace를 이용**한다

**ClassLoader는 자신이 Load한 Class의 Class 이름을 Namespace에 저장해놓는다**

![image-20220417235652367](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220417235652367.png)

Class Loader 1은 Namespace에 com.bgpark.domain.Person이라는 클래스 이름을 가지고 있으므로 같은 이름의 클래스를 Load하지 않는다

Class Loader 2는 Namespace에 com.bgpark.domain.Person이라는 클래스 이름이 없으므로 해당 클래스를 Load한다



그럼 Class Loader는 언제 Namespace를 검색할까?

Class Loader는 **Symbolic Reference를 수행할 때 Namespace를 검색**한다

자바는 메모리 주소로 객체를 참조하지 않고 **Symbolic Reference라는 객체의 이름으로 참조**한다

Class Loader는 **객체를 참조할 때 Namespace에서 Class의 이름을 찾는다**



자바는 참조되는 **Class를 Load 할 때는 반드시 참조하는 Class와 같은 Class Loader를 사용해야한다**

그렇다면 왜 JVM에 여러개의 ClassLoader를 사용하는 것일까?

> WAS에는 상당히 많은 수의 Class Loader가 존재한다
>
> 많은 수의 Class Loader는 각각 Java API와 Classspath의 클래스들을 Load할 것이니 자원낭비라 생각할 수 있다
>
> Class Loader Delegation Model을 보면 이해할 수 있다



###  3-1-3. Class Loader Delegation Model

자바는 **여러개의 Class Loader의 계층구조를 바탕으로 서로에게 책임을 위임(Delegation)하는 방법을 사용**한다

![image-20220418001203651](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220418001203651.png)

***Boostrap Class Loader***는 가장 상위의 Class Loader이다

Boostrap Class Loader는 JVM 기동시 실행되어 `$JAVA_HOME/jre/lib/rt.jar`를 Load한다

rt.jar는 Runtime Jar의 약자로 **자바를 실행하기 위한 Core Java API 클래스들을 가진 파일**이다.

자바를 설치하면 JRE라는 자바를 실행할 수 있는 환경이 있고 그 경로 아래에 자바를 실행하기 위한 API인 `rt.jar`가 존재한다 

JVM을 실행하면 Boostrap Class Loader가 `$JAVA_HOME/jre/lib/rt.jar` 경로로 `rt.jar`의 Core Java API 클래스를 Load 한다.

그래서 처음 자바를 설치하고 `$JAVA_HOME` 경로를 지정하지 않아 자바가 실행이 안되는 경우를 많이 볼 수 있게 된다

https://javarevisited.blogspot.com/2015/01/what-is-rtjar-in-javajdkjre-why-its-important.html#axzz7QjGfi06A



***Extension Class Loader***는 **``$JAVA_HOME/jre/lib/ext` 경로 아래 존재하는 확장 Class들을 Load**한다

Boostrap Class Loader와 Extension Class Loader는 JVM을 위한 Class Loader이다



***Application Class Loader***는 **`$CLASSPATH` 혹은 `java.class.path`에 위치한 Class들을 Load**한다

***User Defined Loader***는 **Application에서 직접 생성** 할 수 있다.



## 3-2. Execution Engine

**Runtime Data Area에 적재된 바이트 코드를 기계어로 번역해 명령어 단위로 실행하는 역할**을 한다

명령어 단위로 실행하는 방법은 2가지가 있다.

1. **인터프리터** : 명령어를 하나씩 실행
2. **JIT 컴파일러** : 반복되는 바이트 코드를 네이티브 코드로 변경하여 실행하는 방식 (성능 향상)

## 3-3. Garbage Collector

**Heap 메모리 영역에 생성된 객체 중 참조되지 않은 객체들을 제거하는 역할**을 한다

GC 되는 동안 GC 스레드를 제외한 모든 스레드가 멈추는 Stop the world가 발생한다

## 3-4. Runtime Data Area

**Class Loader로 바이트 코드가 적재되는 메모리 공간**이다

Runtime Data Area는 JVM이 수행되도록 OS에서 메모리 공간을 할당 받는다

Runtime Data Area는 4가지 영역으로 나눠진다

1. Method Area
2. Heap Area
3. Stack Area
4. PC Register
5. Native Method Stack

# 4. JVM 메모리 구조

JVM의 메모리 구조는 **Runtime Data Area**를 칭한다.

![image-20220408003525640](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220408003525640.png)

## 4-1. Method Area

![image-20220417235408400](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220417235408400.png)

Method Area는 **ClassLoader로 부터 받은 Class 파일에서 타입 정보를 추출하여 저장**하는 공간이다

저장되는 정보는 모든 변수, 상수, 참조값, 메소드 데이터 등을 포함한다

Method Area는 **모든 Thread들이 공유하는 영역**이다

Method Area는 **JVM이 기동할 떄 생성**되며 **GC 대상**이 된다

Method Area의 타입 정보는 7개의 정보로 구성된다 

1. Type Information
2. Constant Pool
3. Field Information
4. Method Information
5. Class Variables
6. Reference to class *ClassLoader*
7. Reference to class *Class*

### 4-1-1. Type Information

Type Information은 **타입에 대한 전반적인 내용이 포함**된다

### 4-1-2. Constant Pool

Constant Pool은 Type의 **모든 Constant 정보를 가지고 있는 부분**이다

Constant는 상수, 멤버 변수, 클래스 변수, 메소드의 Symbolic Reference까지 모두 포함한다



Symbolic Reference는 **객체의 이름으로 해당 객체를 참조**하는 자바의 방식이다

자바는 객체를 참조할 때 메모리 주소를 사용하지 않고 객체의 이름으로 해당 객체를 참조하게 된다

Constant Pool에서는 이 Symbolic Reference를 저장한다

**JVM은 참조하는 객체에 접근하려면 Method Area의 Constant Pool에서 Symbolic Reference를 찾아 객체의 메모리 주소로 동적으로 연결한다**

그래서 Constant Pool은 JVM에서 매우 중요한 역할을 한다

### 4-1-3. Field Information

Field Information은 **타입에서 선언된 모든 Field의 정보**이다

자바에서는 4가지 종류의 변수가 있다

1. 인스턴스 변수 (non static field)
2. 클래스 변수 (static field)
3. 로컬 변수
4. 파라미터

이 중 **인스턴스 변수**와 **클래스 변수**를 Field라고 부른다

### 4-1-4. Method Information

Method Information은 타입에서 **선언된 모든 메소드 정보**를 말한다

### 4-1-5. Class Variable

Class Variable은 Class에서 **static으로 선언된 변수**이며 하나의 값으로 유지된다

Class Variable은 **모든 인스턴스에서 공유**되므로 **동기화 이슈가 발생**한다

Class Variable은 **Class를 사용하기 전부터 Method Area에 미리 메모리를 할당** 받는다

Class Variable을 final로 선언하면 Constant Pool에 Literal Constant (상수) 취급을 받는다

> Class와 Instance의 관계
>
> 흔히 Class와 Instance의 관계를 붕어빵 틀과 붕어빵으로 비유한다
>
> Class가 Class Loader에 의해 Load되면 **Method Area에 Class의 모든 정보들이 추출되어 저장**된다
>
> 클래스 변수는 Class 정보가 있는 Method Area의 Class Variable에 저장된다
>
> 즉, 하나의 **붕어빵 틀**이 만들어진다
>
> Class로 Instance를 만들려면 Method Area의 Class 정보로 Heap에 Instance를 하나 찍어낸다
>
> 인스턴스 변수(멤버 변수)는 Heap 영역에 생성된 Instance에 저장된다
>
> 즉, 하나의 틀로 **붕어빵**을 만들 수 있다

### 4-1-6. Reference to Class *ClassLoader*

ClassLoader의 참조값이 필요한 이유는 **타입이 다른 타입을 참조할 때는 ClassLoader를 사용**하기 때문이다

타입이 JVM에 Load 될 때 어떤 Class Loader들에 의해 Load되었는지 추적된다

Class Loader는 User-Defined ClassLoader와 Bootstrap ClassLoader로 나뉜다

타입이 User-Defined ClassLoader를 통해 Load되면 **ClassLoader의 참조값을 User-Defined ClassLoader의 참조값으로 저장**한다

타입이 Bootstrp ClassLoader를 통해 Load되면 **ClassLoader의 참조값을 null로 저장**한다 

### 4-1-7. Reference to Class *Class*

**타입이 JVM에 Load되면 java.lang.class의 인스턴스가 생성**된다

Method Area에서는 class의 인스턴스의 참조값을 저장하게 된다 

그래서 모든 클래스는 getName()으로 클래스의 이름을 찾거나 isInterface()로 인터페이스 여부를 알 수 있다

### 4-1-8. Method Table

Method Table은 Class의 Method에 대한 Direct Reference를 자료구조이다

즉, Method Table은 Method의 참조를 빠르게 하기위해 정리된 공간이다

알다시피 Reference로 객체를 찾는 일은 너무 빈번하게 일어나고 Stack영역에서 Heap영역의 참조를 찾는 것은 CPU를 사용하기도 한다

그래서 **Reference를 쉽게 찾아 성능을 높히기 위해 Method Table이라는 데이터 구조를 사용**한다

## 4-2. Heap Area

Heap은 **인스턴스와 Array가 저장되는 공간**이다

Heap은 모든 **Thread에 의해 공유**되어 **동시성 이슈가 발생**된다



### 4-2-1. Object 구조 (Hotspot JVM 기준)

Heap에 저장되는 데이터는 모두 Header와 Data로 나눠져 있다

Header는 **Object 앞 부분에 위치한 고정 크기의 공간**이다 (32bits 환경에서는 32bits, 64bits 환경에서는 64bits)

Object는 Mark Word와 Class Address라는 2개의 Header를 가지고 있다

Mark Word는 **GC**와 **Synchronization 작업**을 위해 사용된다 

Class Address는 **Method Area의 Class 정보를 가르키는 Reference 주소**가 저장된다

Data는 **가변 크기의 공간**이다

> Mark Word의 age 정보는 Young Generation에서 Eden과 Survivor 영역을 넘나든 횟수를 저장한다
>
> 일정 횟수 이상이 넘으면 Old Generation으로 Object가 이동된다

### 4-2-2. Heap의 구조 (Hotspot JVM 기준)

Heap은 **Young Generation**과 **Old Generation**으로 나눠져있다

Young Generation은 **Eden**과 **Survivor** 영역으로 나눠진다

Eden 영역은 **인스턴스가 최초로 할당되는 공간**이다 (*Allocation*)

Eden 영역이 꽉 차면 참조 여부에 따라 **참조가 있으면 Live Object로 Survivor 영역**으로 **참조가 끊어지면 Garbage Object로 Eden**에 남는다

모든 Live Object가 Survivor 영역으로 넘어가면 Eden의 Garbage Object를 비운다

Survivor 영역은 **Eden에서 살아남은 인스턴스가 잠시 지내는 곳**이다

Old Generation은 **Young Generation에서 Live Object로 오래 살아남은 Object가 이동하는 공간**이다 (*Promotion*)

## 4-3. Stack Area

![image-20220417235227665](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220417235227665.png)

Stack은 **Thread의 동작을 기록하는 메모리 영역**이다

Stack은 Native Method Stack과 PC Register와 마찬가지로 **Thread 별로 생성**된다

그래서 다른 Thread는 접근할 수 없고 공유되지 않으므로 **동기화 이슈가 발생하지 않는다** (예, 메소드의 지역변수)



Stack은 데이터를 순서대로 넣고 마지막을 먼저 꺼내는 FILO(First In Last Out) 자료구조이다

JVM의 Stack도 동일하다

JVM의 Stack은 **Frame**이라는 데이터를 FILO하게 된다

Frame은 Thread가 메소드를 실행할 때마다 하나씩 생성하여 Stack에 넣어준다(push)

Frame은 **메소드에 사용되는 파라미터, 지역변수, 결과값 등을 저장**하게 된다

메소드가 종료되면 Stack에서 Frame을 꺼내어 제거한다(pop)

우리가 흔히 에러가 발생했을 때 나타나는 **StackTrace의 한줄한줄은 Frame 정보를 나타낸다**

즉, Stack으로 쌓인 Frame 정보를 하나하나씩 빼면서 자취(Trace)를 표현한 것이다

### 4-3-1 Frame

앞서 설명한 것 처럼 Frame은 **Thread가 실행하는 메소드 정보를 기록하는 곳이다**

참고로 Fame은 Class의 메타정보를 이용하여 적절한 크기로 생성된다

Frame은 **Local Variable Section**, **Operand Stack**, **Frame Data**로 총 3가지 영역으로 구성되어 있다.

1. Local Variable Section

   로컬 변수 섹션은 메소드의 **파라미터**와 **지역변수**를 저장한다

   파라미터와 지역변수가 **원시타입이면 원시타입의 크기만큼 할당**을 받고, **참조타입이면 Heap영역의 주소 크기만큼 공간을 할당**받는다

2. Operand Stack

   Operand Stack은 JVM의 **작업 공간**이다

   **Local Variable Section의 데이터를 Operand Stack에 넣고(push) instructor가 데이터를 빼서(pop) 연산하게 된다**

   메소드의 연산을 실행하면 Local Variable Section의 데이터가 Operand Stack으로 차례대로 쌓이게 된다(push)

   메소드가 정상적으로 종료하여 반환값이 있는 경우도 Operand Stack으로 쌓이게 된다(push)

   

3. Frame Data

   Frame Data 영역은 아래와 같은 정보를 담는다

   - Constant Pool Resolution 정보

   - Method 정상 종료시 정보

   - Method 비정상 종료시 Exception 정보

     

   Class의 모든 Symbolic Reference는 JVM Runtime Area의 Method Area에 Constant Pool에 저장되어 있다

   Contant Pool Resolution은 이 **Constant Pool의 주소 정보를 저장**한다

   그래서 Class의 상수, 메소드, 변수 혹은 다른 Class를 참조하려면 Constant Pool에 접근하여 Class의 모든 참조 정보를 얻을 수 있다

   

   Method가 정상적으로 종료되면 Stack에서 Frame이 제거된다 (pop)

   그러면 이전에 제거된 Frame을 호출했던 Frame을 알아야 Frame의 메소드로 돌아갈 수 있다

   Frame Data는 자신을 호출한 Frame의 instructor 주소를 가지고 있다

   메소드가 종료되면 **PC Register에 자신을 호출한 메소드 주소를 넣어준다**

   메소드의 반환값이 있는 경우 **반환값을 Operand Stack에 넣어준다**

   

   Method가 비정상적으로 종료되면 Exception을 핸들링해야한다

   Frame Data는 **발생한 Exception 정보를 저장**한다

   정확히는 Exception 테이블의 참조값을 저장한다

   

> 참고로 **Heap의 객체를 찾는 것은 CPU 연산을 필요하여 시스템 자원을 사용**하게 된다
>
> 또한 **객체의 생성은 Method Area의 Class 정보를 읽어 인스턴스를 생성하여 Heap에 저장하는 과정도 추가된다**
>
> 그래서 메소드에서  Integer 보다 int가 조금 더 효율적이다  

## 4-4. PC Register

![image-20220417235338866](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220417235338866.png)

컴퓨터는 **CPU에서 명령어(instruction)을 수행**하면서 프로그램을 동작시킨다

CPU는 명령어를 수행하는 동안 Register라는 **CPU 내 공간을 사용**한다

명령어는 게산에 사용되는 데이터인 operand와 연산인 instruction으로 구성된다

예를들어, 1+2을 표현하면 1과 2를 operand, +를 instruction으로 Register에 저장한 후 CPU가 계산한다



하지만 **Runtime Data Area의 PC Register는 조금 다르다**

JVM은 **Stack에서 사용되는 operand를 PC Register라는 메모리 공간에 저장**한다

JVM의 목적이 OS에 종속되지 않게 하기 위해 CPU의  Register를 사용하지 않고 JVM의 Runtime Data Area에 PC Register라는 공간을 만들었다.



PC Register는 Stack, Native Method Stack과 마찬가지로 **Thread마다 하나씩 존재**한다

그래서 Thread가 메소드를 수행하면 Stack에 있는 매개변수, 변수 등의 주소를 PC Register가 가지게 된다

> Native Method는 어떻게 될까?
> Native Method는 OS에 종속될 수 밖에 없으므로 JVM을 거치지 않는다

## 4-5. Native Method Stack

자바는 Native 메소드의 호출을 직접 수행할 수 있다

JNI (Java Native Interface)라는으로 **자바 외 언어를 사용하기 위한 표준 규약**을 사용한다



JVM에서 메소드를 호출하면 Thread의 Stack 영역에 Frame을 생성하여 넣어주었다(push)

**자바는 자바 외 언어의 메소드를 실행하기 위해 Native Method Stack이라는 메모리 공간을 가진다**

자바에서 Native Method를 실행하면 Native Method를 실행한 자바 메소드의 Frame이 생성되어 Stack에 저장된다

동시에 Native Method를 위한 Frame을 생성해서 Stack에 저장한다

그리고 Native Method Stack에서 Frame을 생성하여 Native Method 작업을 수행한다

Native Method Stack에서 Native Method의 작업이 끝나면 Stack 영역의 Native Method를 위한 Frame으로 돌아간다



**실제로 IBM이나 HotSpot JVM은 Stack과 Native Method Stack을 구분하지는 않는다**

왜냐하면 JVM이 사용하는 Thread는 Native Thread를 사용하기 때문이다

초기에는 OS 스레드가 아닌 JVM 전용 스레드를 사용하였기 때문에 구분하여 설명하였다

# 5. Java 7과 Java 8의 메모리 구조 차이







https://jeong-pro.tistory.com/148
