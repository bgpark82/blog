# Thread

## 멀티 스레드
### 역사
1. 하나의 CPU가 여러개의 프로세스를 쪼개어 동작
2. 하나의 CPU가 여러개의 프로세스를 여러개의 스레드로 쪼개어 동작
3. 여러개의 CPU가 여러개의 프로세스를 여러개의 스레드로 쪼개어 동작

### Context Switching
- CPU가 다른 프로세스 정보(Context)로 교체(Switch)시키는 작업
- 프로세스의 정보(Context)는 PCB에 보관된다
- CPU가 PCB의 프로세스 정보를 읽어서 스케쥴링 방식으로 프로세스를 교환한다
- 대표적인 스케쥴링 방식으로 라운드로빈 방식을 사용한다

### 장점
- 놀고 있는 CPU가 줄어든다
- DB, 네트워크 IO 발생 시, 동시에 다른 스레드가 실행될 수 있다

## 문제점
- 자원 공유의 문제
    1. Race Condition
    2. Deadlock
    3. Starvation
    4. Nested Monitor Lockout
  
### 1. Race Condition
- **두개의 스레드**가 **하나의 변수**를 **동시에 쓰기** 할 때 발생
- 하나의 스레드만 변수에 접근하도록 한다 -> synchronized 블록 사용

### 2. Deadlock
- 서로 다른 스레드가 각각의 lock(자원)을 사용하고 있을 때, 서로 상대방이 사용하고 있는 lock을 사용하려 할 때 발생

Deadlock이 발생하는 4가지 조건
1. Mutual Exclusion : 한번에 하나의 스레드만 lock(자원)에 접근 가능할 때 (Mutex)
2. No Preemption : 스레드가 사용할 lock(자원)을 다른 스레드가 선점 할 수 없을 때
3. Hold And Wait : 스레드가 lock(자원)을 가진 상태에서 다른 스레드의 lock(자원)을 기다릴 때
4. Circular Wait : 스레드가 다른 스레드가 원하는 lock(자원)을 순서대로 가지고 있을 때

해결방법
1. Lock Reordering : lock을 하는 순서를 바꾼다
2. Timeout Backoff : Timeout을 준다
3. Deadlock Detection : Deadlock을 감지한다