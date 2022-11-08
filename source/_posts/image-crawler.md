---
title: 이미지 클크롤러 개발기
catalog: true
date: 2022-11-08 23:39:37
subtitle:
header-img:
tags:
---

회사에서 새로운 서비스를 준비하면서 AI 이미지 모델에 제공될 이미지 크롤러를 개발한 경험을 공유해보려 합니다.



## 크롤러

웹 크롤러는 **자동화된 방식으로 WWW를 탐색하는 컴퓨터 프로그래밍 방식**입니다. 자세히 말해 Seed Url로 WWW의 컨텐츠를 얻는 방식을 말합니다. 예를들어, 아마존의 웹 페이지 정보가 필요하다면 웹 페이지의 Seed Url로 HTML, CSS, Javascript 자원을 획득하는 것을 크롤러가 하게됩니다. 크롤러에는 크게 2가지 컴포넌트가 존재합니다. 첫번째는, WWW 공간에서 컨텐츠를 얻는 크롤러와 크롤링한 컨텐츠를 저장하는 다운로더가 존재합니다. (주로 인덱싱과 랭킹 작업)

이미지 크롤러는 WWW 공간에서 HTML, CSS, Javascript 대신 이미지(jpg, png)를 획득합니다.

## 1. Requirement

먼저, 시스템에서 functional한 부분과 non-functional한 부분을 나누고, 이미지 크롤러가 핸들링 할 수 있는 예상 수치를 산정하였습니다.

1. Functional Requirement
   - Crawling : 크롤러는 시스템 관리자로 부터 제공된 **seed Url를 Queue 방식으로 전달하여 크롤링** 합니다. (왜?)
   - Storing : Blob 방식으로 획득한 웹 리소스를 저장합니다. 중요한 점은 **URL과 웹 리소스가 Indexing과 ranking 목적으로 검색되도록 프로세싱**해야합니다. (왜?)
   - Scheduling : 크롤러는 자동적으로 반복되야 하므로, **주기적으로 blob 데이터를 업데이트하는 스케쥴러가 주기적으로 동작**해야 합니다 (왜?)

> 체크 포인트
>
> 1. URL을 어디서 구하는지 : 웹 서버의 IP를 스캔해서 가져옴
> 2. 왜 좋은 URL이 중요한지 : WWW는 Graph 데이터 구조로 되어 있어 하나의 노드에서 다른 노드로 이동합니다. 우리는 최대한 많은 자원을 탐색해야 되기 떄문에 좋지 않은 URL은 일부분의 Graph만을 탐색할 수 밖에 없습니다
> 3. 어떻게 seed Url을 선택하는가? 우선순위(Priority)에 따라 다르게 선택합니다.
>    1. 지역
>    2. 내용
>    3. 인기
>       

2. Non-functional Requirement
   - Scalability : 시스템은 짧은 시간에 몇백만건의 컨텐츠를 다운로드하므로 **분산 환경** 및 **멀티스레스 환경**에서 동작해야 합니다. 
   - Extensibility : 확장성을 위해 **여러 네트워크 프로토콜 통신**과 **여러개의 모듈을 추가**할 수 있는 구조로 만들어져야 합니다.
   - Consistecy : 크롤러는 여러개의 워커 크롤러가 동작하므로 크롤러간 데이터의 일관성이 중요합니다
   - Performance : 크롤러는 크롤링할 수 있는 양을 제한할 수 있어야 합니다 (self-throttling). 크롤링 작업은 짧은 순간 몇백만건의 컨텐츠를 다운로드 하므로 **처리량이 최적화**되어야 합니다
   - Improve User Interface : 관리자가 커스텀한 크롤링 을 할 수 있는 기능도 제공되어야 합니다.

### Resource estimation

이미지 크롤러를 개발하기 앞서 크롤러가 처리할 처리량을 계산해야 합니다. 

Assumption

1. 50억 웹 페이지 (5 billion web pages) -> XX개의 스톡 이미지 웹 페이지

2. 웹 페이지 당 텍스트 컨텐츠가 2070KB (text content per webpage 2070kb) -> 페이지 당 이미지 용량 XXkb

3. 웹 페이지의 메타데이터가 500 바이트 (metadata for one web page is 500Bytes) -> 이미지 당 메타 데이터가 XXX 바이트

   

### Storage Assumption

총 50억개의 웹 페이지에서 텍스트 컨텐츠를 저장해야할 양은 다음과 같습니다.

```java
전체 스토리지 용량 = 50억 x (2070KB + 500B) = 10.35PB
```



![image-20221109002914295](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221109002914295.png)

### Traversal time

한번 크롤링 시 걸리는 시간을 측정합니다. 평균 웹 페이지 당 HTTP 순회 요청은 60ms 정도 걸립니다. 우리는 총 50개의 웹 페이지를 순회해야되므로 약 9.5년의 시간이 걸립니다.

```java
전체 요청 시간 = 50억 x 60ms = 3억 초 = 9.5년
```

하지만 우리의 목표는 하루만에 크롤링 해야 합니다. 그래서 다중 서버에서 다중 워커 크롤러를 동작할 수 있는 시스템을 디자인 해야 합니다.

### Number of servers estimation for multi-worker architecture

크롤링이 하루만에 가능하게 하기위해 필요한 서버를 계산해보겠습니다.

```java
하나의 서버의 작업량 = 9.5년 x 365일 = 3468일
```

즉, 해당 작업을 하루에 끝내야 한다면 3468일이 걸리게 됩니다. 만약 10대의 크롤러가 하나의 서버에서 동작한다면 347개의 서버만 있으면 됩니다

```java
10개의 스레드(크롤러) 서버의 작업량 = 3468일 / 10 = 347일
```

### Bandwidth estimation

하루에 10.35PB의 데이터를 처리하려면 총 960Gb/sec의 bandwidth가 필요합니다.

```java
10.35PB / 86400sec = 120GB/sec = 960Gb/sec
```

총 347개의 서버가 하루에 처리한다면 각 서버당 처리할 수 있는 bandwidth는 2770Mb입니다

```java
960Gb/sec / 347서버 = 2770Mb/sec
```

![image-20221109004041929](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20221109004041929.png)

### Building Blocks

우리 시스템에서 사용할 컴포넌트들을 알아보겠습니다.

- Scheduler : DB에 저장된 URL에 대한 크롤링 이벤트를 스케쥴링하는 스케쥴러
- DNS : 웹 페이지의 IP 주소를 resolution하는 용도
- Cache : 각 모듈들이 빨리 작업할 수 있도록 저장된 document를 사용하는 캐시
- Blob Store : 크롤링한 컨텐츠를 저장하기 위한 모듈
- HTML Fetcher : 크롤러와 웹 호스트 간에 네트워크 커넥션을 수립하기 위한 모듈
- Service Host : 워커 간에 크롤링 작업을 매니징하는 모듈
- Extractor : embedded Url과 document을 추출하는 모듈 
- Duplicate Eliminator : URL과 document의 dedepllication을 담당하는 모듈

> Scheduler : https://www.educative.io/collection/page/10370001/4941429335392256/6152021643624448
> DNS : https://www.educative.io/collection/page/10370001/4941429335392256/5728619204182016
> Cache : https://www.educative.io/collection/page/10370001/4941429335392256/5053577315221504
> Blob Store : https://www.educative.io/collection/page/10370001/4941429335392256/4862646238576640



## 2. Design

두번째는 이미지 크롤러의 아키텍처를 그려본 다음 각 컴포넌트에 대한 요구사항을 정리해보았습니다.

앞서 정리한 각 모듈들에 대한 자세한 스펙을 구성해보겠습니다.

### 1. Scheduler

크롤링에 필요한 URL을 스케쥴링하는 중요한 모듈입니다. 주로 Priority Queue와 Relation Database로 구성됩니다.

1. Priority Queue (URL frontier) : Priority와 Update Frequency 두가지 속성에 기반하여 URL을 Queue에 넣습니다.
2. Relational Database : Priority와 Update Frequency에 기반한 모든 URL을 저장하는 Database입니다. 보통 2가지 데이터를 저장합니다. 
   1. 사용자가 추가한 URL
   2. 크롤러가 추출한 URL

> Priority Queue의 사이즈를 예측할 수 있을까? 
> 중앙화 및 분산된 Priority Queue의 장단점은 무엇인가?
>
> 어떻게 URL을 다른 워커들에게 분산할 수 있을까?
> 해당 서버의 목적은?



### 2. DNS Resolver

웹 크롤러는 호스트네임과 IP주소를 매핑하는 DNS Resolver가 필요합니다. 왜냐하면 **DNS lookup은 시간이 상당히 소요되는 작업**이므로 **커스텀 DNS Resolver**와 **time-to-live (TTL) 내 살아있는 자주 사용하는 IP주소를 캐싱**하는게 좋습니다. 

### HTML fetcher







## 3. Improvement

세번째는 이미지 크롤러에서 발생할 수 있는 문제와 개선점에 대해 고민해보았습니다. 예를들어, 링크의 쿼리 파라미터나 링크의 리다이렉션, 사이클링 링크 등의 문제들에 대해 고민하고 개선해보았습니다.



## 4. Evaluation

마지막으로 지금까지 작성한 시스템 디자인이 크롤러의 요구사항에 맞는지 재평가하는 시간을 가져보았습니다.
