---
title: Kafka signup issue
catalog: true
date: 2022-11-21 19:20:43
subtitle:
header-img:
tags:
---

Kafka를 운영하면서 발생했던 경험에 대해 공유해보도록 하겠습니다

## 배경

------

현재 회원가입 구조는 사용자가 회원가입을 하면 계정 서비스가 요청을 받아 카프카 브로커에 토픽을 생성해 전송합니다. 토픽을 수신하는 서비스는 해당 레코드를 받아 처리하게 됩니다.

이런 구조는 다음과 같은 장점을 가지고 있습니다.

- 계정 서비스와 메인 서비스간 결합도 제거
- 수신 서비스에 장애가 발생해도 브로커는 이벤트를 일정 기간동안 유지
- 토픽이 여러개의 파티션에 분산되어 하나의 브로커에 장애가 발생해도 복구 가능 (가용성)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e99eeab6-f8b0-41a0-9cea-eddc63465c38/Untitled.png)

## 원인 파악

------

### 1. 카프카 구동 확인

운영 서버에는 총 3대의 카프카 브로커가 존재합니다. 각각의 브로커에 접근하여 모두 구동 중인지를 확인해보겠습니다.

```sql
ps -ef | grep kafka
```

총 3대의 브로커 중 **1번 카프카 서버가 다운된 상태였습니다**.

하지만 카프카가 다운 되더라도 다른 브로커에 복제가 되어 이벤트를 수신할 수 있을텐데 왜 동작하지 않는지 의문이었습니다.

그래서 먼저 topic의 설정값을 먼저 살펴보기로 했습니다. 회원가입 시, 계정 서버에서 전송되는 이벤트는 account_event입니다. 해당 토픽의 설정에서 replication 여부를 먼저 확인해보겠습니다.

### 2. topic 확인

```sql
./bin/kafka-topics.sh --zookeeper localhost:2181 --topic account_event --describe

**Topic:account_event**
PartitionCount:1        ReplicationFactor:1     Configs:
Topic: account_event    Partition: 0    Leader: 1       Replicas: 1     Isr: 1
```

- ```
  *--zookeeper*
  ```

   vs 

  ```
  *--bootstrap-server*
  ```

  - /bin/kafka-topics.sh에서 사용
  - `*--boostrap-server*` : 브로커에 바로 연결할 때 사용
  - `*--zookeeper*` : 주키퍼에 연결할 때 사용 (*deprecated*)

**account_event 토픽을 확인하면 replicas가 1임을 알 수 있습니다.**

**즉, 해당 이벤트가 전송된 카프카가 다운된 경우 복원할 방법이 없게 됩니다.**

먼저 **왜 이런 일이 발생했는지 생각해보면 기존에 topic을 Publisher로 자동으로 생성했기 때문에 브로커에서 설정한 기본 옵션으로 토픽을 생성하게 됩니다. 이 부분은 추후 개선할 예정입니다**

그럼 언제부터 다운되었는지 한번 살펴보겠습니다.

### 3. commit log 확인

```java
./bin/kafka-run-class.sh kafka.tools.DumpLogSegments --deep-iteration --files ../kafka-logs/account_event-0/00000000000000052262.log
Dumping ../kafka-logs/account_event-0/00000000000000052262.log
| offset: 52659 CreateTime: **1664956976908** keysize: -1 valuesize: 157 sequence: -1 headerKeys: []
baseOffset: 52660 lastOffset: 52660 count: 1 baseSequence: -1 lastSequence: -1 producerId: -1 producerEpoch: -1 partitionLeaderEpoch: 3 isTransactional: false isControl: false position: 90289 CreateTime: 1664965080597 size: 229 magic: 2 compresscodec: NONE crc: 428458902 isvalid: true
| offset: 52660 CreateTime: **1664965080597** keysize: -1 valuesize: 159 sequence: -1 headerKeys: []
baseOffset: 52661 lastOffset: 52661 count: 1 baseSequence: -1 lastSequence: -1 producerId: -1 producerEpoch: -1 partitionLeaderEpoch: 3 isTransactional: false isControl: false position: 90518 CreateTime: 1664965464265 size: 223 magic: 2 compresscodec: NONE crc: 2949796221 isvalid: true
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0c770232-dcea-4013-ad13-3786208da741/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/46ee04f9-e700-4747-9508-4e2cc71d7321/Untitled.png)

카프카는 commit하는 시점에 CreateTime을 생성하는데 마지막으로 commit된 로그를 확인해보겠습니다.

**10월 22일 5시부터 복구 시점인 7시 18분까지 2시간여 동안 commit되지 않은 것을 알 수 있었습니다.**

`server.log`를 살펴봐도 실제 운영시간을 알 수 있다.

```java
vim server.log.2022-10-05-17 
[2022-10-05 17:05:39,869] INFO [GroupMetadataManager brokerId=1] Removed 0 expired offsets in 0 milliseconds. (kafka.coordinator.group.GroupMetadataManager)
```

**실제로 17시 5분부터 서버가 다운**되어있다. 그렇다면 왜 서버가 다운되었을까요?

### 4. kafka 1호기 서버 다운 원인 확인

카프카에서는 에러 로그를 발견할 수 없었습니다.

예상되는 이슈는 **Failed to clean up log for __consumer_offsets** 에러입니다.

- 카프카 데이터를 retention 주기만큼 보관하다가 cleaner가 삭제한다. 삭제할 로그 파일이 존재하지 않는 경우
- 해당 이슈는 서버가 `*/tmp`* 디렉토리를 자동으로 비울 때 발생한다
- 하지만 우리 서버는 home 디렉토리에 로그 파일이 있으므로 해당 이슈는 아니다.
- 더군다나 해당 에러를 발견할 수도 없었다.

현재 이슈 복원을 할 수 없으므로 나중을 대비해 모니터링이나 로그 시스템을 갖추는게 좋을 것 같습니다.

## 긴급 조치

------

일단 회원가입한 사용자의 정보는 수신 서비스의 데이터베이스와 동기화를 해야했기 때문에 아래와 같이 조치하였습니다.

### **카프카 이벤트 수동 전송**

다행히 계정 서비스의 데이터베이스에는 회원정보가 잘 담겨 있어 수동으로 카프카에 데이터를 전송하도록 했습니다.

카프카가 다운되었던 2시간동안의 데이터를 쿼리하여 토픽을 생성한 후 전달하여 동기화 했습니다. 다행히 약 50건 정도의 데이터만 유실된 상황이라 빠르게 조치할 수 있었습니다.

```java
{
		**"accountId": 260939,
    "group": "account",
    "email": "1234@hanmail.net",
    "accountEvent": "SIGN_UP",**
    "properties": {
        **"role": "USER",**
        **"name": "이지원",**
        **"id": "12345678"**
	  }
}
```

## **후속 조치**

------

### 1. 모니터링

여러가지 UI 툴들이 있지만 그중에 아래 3가지를 우선순위로 선정했습니다.

- UI for Apache Kafka
- Confluent
- Conduktor

![https://github.com/schooldevops/kafka-tutorials-with-kido/blob/main/imgs/ui_tools_kafka.png?raw=true](https://github.com/schooldevops/kafka-tutorials-with-kido/blob/main/imgs/ui_tools_kafka.png?raw=true)

UI for Apache kafka는 다음과 같은 장점을 가지고 있습니다.

- 무료
- 가독성이 좋고 유연한 UI
- 대부분의 서비스를 지원

현재 pinpoint로 모니터링하고 있지만 kafka가 조회되지 않는 도중이기 때문에 무료인 UI for apache kafka를 사용하는 것이 부담없다고 판단했습니다. 그 외 Confluent CC나 Conducktor 같은 UI 툴들이 있지만 대부분 비용이 들거나 많은 기능들을 지원하지 않았습니다. UI for Apache Kafka 또한 현재 개발중이라는 단점이 있지만 그만큼 개선점이 있을 것이라 생각했습니다.

### 2. 카프카 생성 시 기본 옵션 적용

현재 **카프카 토픽 생성 시 자동생성은 다음과 같습니다.**

- **replicasFactor = 1** : replicas 1개로 복제 안됨
- **partition = 1** : 1번 브로커에만 파티션 존재
- **isr = 1** : 1번 파티션만 리더 파티션

```markdown
**account_event** 
- partition: 1
- replicas: 1
- isr: 1 (leader partition: 1)
```

### 2-1. topic

- **replicasFactor = 3** : 브로커 3개로 운영중이니 replicas는 기본 3개로 설정

  - 팔로우 파티션은 리더 파티션의 offset을 조회하여 차이가 나면 offset을 복제해간다

  - 만약 리더 파티션이 다운되면 다른 파티션이 leader partition이 되도록 복제를 해놓는다

  - 테스트

    ```java
    which kafka-topics
    
    /usr/bin/kafka-topics
    
    kafka-topics --zookeeper zookeeper-1:2181 --list
    ```

    자세한 내용은 여기 https://kafka.apache.org/documentation/#basic_ops_increase_replication_factor

    ```java
    root@011abbdc1903:/usr/bin# kafka-topics --zookeeper zookeeper-1:2181 --topic test --describe
    Topic: test	PartitionCount: 3	ReplicationFactor: 1	Configs:
    	Topic: test	Partition: 0	Leader: 1	Replicas: 1	Isr: 1
    	Topic: test	Partition: 1	Leader: 1	Replicas: 1	Isr: 1
    	Topic: test	Partition: 2	Leader: 1	Replicas: 1	Isr: 1
    ```

    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/427e5fb9-cda1-4e6f-a58d-f35c9653eb20/Untitled.png)

    ```java
    {
    "version":1,
    
    "partitions":[
    
        {"topic":"test","partition":0,"replicas":[2,3]},
        {"topic":"test","partition":1,"replicas":[2,3]},
        {"topic":"test","partition":2,"replicas":[2,3]}
    
    ]}
    ```

    ```java
    kafka-reassign-partitions --zookeeper zookeeper-1:2181 --reassignment-json-file rf.json --execute
    ```

  ```java
  {
  	"version":1,
  	"partitions":[
  	    {"topic":"account_event","partition":0,"replicas":[1,2,3]},
  	    {"topic":"account_event","partition":1,"replicas":[1,2,3]} // 2,3만 하면 덮어씌워버린다. 1은 제외
  	]
  }
  ```

  주의할 점은 replicas에 [1,2,3]번 브로커 모두 지정해줘야한다. 현재 브로커 1번에 파티션이 있다고 해서 [2,3]번만 업데이트해버리면 [2,3]번 브로커에만 replicas가 생긴다. 그래서 [1,2,3] 번 모두 업데이트 한다

  ```java
  bin/kafka-reassign-partitions.sh --zookeeper localhost:2181 --reassignment-json-file rf_account_event.json --execute
  ```

  replicas 숫자를 성공적으로 늘릴 수 있었다.

  ```java
  Current partition replica assignment
  
  {"version":1,"partitions":[{"topic":"account_event","partition":1,"replicas":[1,3],"log_dirs":["any","any"]},{"topic":"account_event","partition":0,"replicas":[2,3],"log_dirs":["any","any"]}]}
  
  Save this to use as the --reassignment-json-file option during rollback
  Successfully started reassignment of partitions.
  ```

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2f6c84dd-0986-47e2-a5af-754c15863fca/Untitled.png)

  ```java
  Topic:account_event     PartitionCount:2        ReplicationFactor:3     Configs:
          Topic: account_event    Partition: 0    Leader: 2       Replicas: 1,2,3 Isr: 3,2,1
          Topic: account_event    Partition: 1    Leader: 1       Replicas: 1,2,3 Isr: 3,1,2
  ```

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/737cc664-758b-43ed-83d7-6aa981820a14/Untitled.png)

- **partition = 2** : 베이비페이스 인스턴스가 기본 2개이니 consumer(partition)은 최소 2개로 설정

  - 파티션과 컨슈머 개수는 병렬 처리 성능에 중요한 역할
  - 컨슈머가 2개인데 파티션이 하나라면 나머지 하나의 컨슈머는 idle 상태가 된다
  - 파티션 개수를 늘렸을 때 매칭된 파티션과 메세지 키의 매칭이 깨져 전혀 다른 파티션에 데이터가 할당
  - 그래서 파티션 개수가 달라지면 메세지 키를 사용할 때 컨슈머는 특정 메세지의 키의 순서를 보장 못받는다
  - 하지만 이 경우는 순서가 중요하지 않으므로 파티션 변경을 자유롭게 할 수 있다

  ```java
  ./bin/kafka-topics.sh --bootstrap-server kafka1.alethio.io:9092 --topic account_event --alter --partitions 2
  ```

  파티션을 업데이트하면 모든 브로커의 파티션이 증가하게된다

  ```java
  ./bin/kafka-topics.sh --zookeeper localhost:2181 --topic account_event --describe
  Topic:account_event     PartitionCount:2        ReplicationFactor:1     Configs:
          Topic: account_event    Partition: 0    Leader: 1       Replicas: 1     Isr: 1
          Topic: account_event    Partition: 1    Leader: 2       Replicas: 2     Isr: 2
  ```

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/41ff4d4d-0dfa-41c2-a3ee-955a8f9b92be/Untitled.png)

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/571b332d-5cb2-49a8-a352-7ba9e450f1d2/Untitled.png)

  파티션을 2개 증가

  - Isr 2 생성 : isr 2는 isr이 1 증가가 아니라 isr에 포함된 브로커의 번호가 된다.
  - replicas 2 : replicas 2는 2번 파티션에 데이터(복제본)이 존재한다는 뜻. 현재는 2번 파티션에만 존재하므로 replicas가 없는 상태이다.
  - leader 2 생성 : leader 2는 2번째 파티션의 리더 파티션이 2번이라는 뜻

- **isr = 3** : leader partition도 하나가 다운되면 다른 partition이 leader partition이 되도록 설정

  - isr은 리더 파티션과 팔로워 파티션의 offset이 모두 싱크가 된 상태를 뜻한다
  - `replica.lag.time.max.ms`이 주기로 팔로워 파티션이 데이터를 복제했는지 확인. 왜냐하면 팔로워 파티션이 리더 파티션을 복제하는 시간때문에 offset 차이가 나기 때문이다. 해당 기간 이후에는 ISR 그룹에서 해당 팔로워 파티션을 제외한다
  - isr로 묶인 파티션들은 리더 파티션과 데이터와 offset이 동일하므로 리더 파티션에 선출될 자격을 가질 수 있다

```markdown
**account_event** 
- partition: 2
- replicas: 3
- isr: 3 (leader partition: 3)
```

### 2-2. producer

- acks = all : 리더 파티션과 팔로워 파티션에 모두 정상 적제되었는지 확인

  - 1 : 리더 파티션에 데이터가 저장되면 전송 성공 (default)

  - 0 : 전송 즉시 저장 여부 관계없이 성공

  - -1, all : 

    ```
    min.insync.replicas
    ```

     개수에 해당하는 리더와 팔로워 파티션에 데이터가 저장되면 성공

    - 리더와 팔로워 파티션 둘 다 확인하므로 속도가 느지리만 안정적이다
    - 모든 리더와 팔로워는 아니고 ISR 범위 내에서의 파티션들이다

  - 복제 개수가 1인 경우 ack에 성능 변화가 없다 하지만 2개이상인 경우 ack에 따라 동작방식이 다르다

  - 회원가입 이벤트는 매주 500건 정도만 발생하므로 속도보다는 안정성이 더 중요하다 판단했다

- min.insync.replicas = 2 : 브로커가 3개이므로 1개의 브로커가 종료되어도 2개에 최소한 복제가 가능

  - 만약 3개의 브로커를 가진 경우 3으로 설정하면 1개의 브로커가 종료될 경우 여전히 3개의 브로커가 sync될때가지 기다리므로 프로듀서는 더 이상 데이터를 전송할 수 없다

> 파티션을 늘렸을 때 우려할 점은 다음과 같습니다

기본적으로 **메세지의 키가 있는 경우** **키의 해시값으로 파티션을 매칭**시킵니다. 파티션을 늘렸을 때 기존의 키의 해시값으로 매칭되던 파티션이 아닌 다른 파티션으로 데이터가 할당될 수도 있습니다. 그 경우 **컨슈머는 특정 메세지의 키의 순서를 보장받지 못하게 됩니다**. 그래서 순서가 중요한 메세지의 경우 주의해야 합니다. 회원가입 이벤트의 경우는 순서가 중요하지 않으므로 파티션을 늘려주면 됩니다

> 

## 결론

------

카프카의 토픽 설정을 수정하여 모든 브로커로 메세지가 복제되도록 설정하였습니다.

또한 Isr 범위에서 리더 파티션이 죽더라도 팔로워 파티션이 리더 파티션이 되어 지속적으로 메세지를 수신하도록 설정했습니다.

당연한 이야기지만 기본적으로 **카프카 토픽은 수동으로 생성**해야된다는 것을 다시 깨닫게 되었습니다.

토픽은 기본적으로 성능보다 안정성이 중요한 경우 아래와 같이 설정하는게 좋을 것 같습니다. (브로커 3대 기준)

1. topic
   - partition = 3
   - replicasFactor = 3
2. producer
   - acks = all
   - min.insync.replicas = 2

## Reference

------

https://kafka.apache.org/22/documentation.html

https://velog.io/@jwpark06/Kafka-시스템-구조-알아보기

https://shinwusub.tistory.com/133

https://parkcheolu.tistory.com/196

https://www.confluent.io/blog/kafka-listeners-explained/

https://devocean.sk.com/blog/techBoardDetail.do?ID=163980

https://towardsdatascience.com/overview-of-ui-tools-for-monitoring-and-management-of-apache-kafka-clusters-8c383f897e80

[카프카를 debugging 가능한 상태로 설정](https://www.ibm.com/docs/en/cloud-paks/cp-biz-automation/19.0.x?topic=emitter-enabling-kafka-broker-trace) → cron으로 주기적으로 제거

https://allg.tistory.com/65

https://www.popit.kr/kafka-운영자가-말하는-topic-replication/

https://www.popit.kr/kafka-운영자가-말하는-replication-factor-변경/

https://docs.confluent.io/platform/current/platform-quickstart.html#ce-docker-quickstart

https://www.baeldung.com/kafka-docker-connection

https://log-laboratory.tistory.com/205

https://dev.to/optnc/kafka-image-wurstmeister-vs-bitnami-efg
