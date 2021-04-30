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

### 문제점
- 자원 공유의 문제
    1. Race Condition
    2. Deadlock
    3. Starvation
    4. Nested Monitor Lockout