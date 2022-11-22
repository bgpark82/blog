2022년 8월부터 10월까지 진행된 이미지 크롤러를 개발했던 경험을 공유하려 합니다.

## 배경

------

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0b58b1e0-6cae-47cd-a66f-39f89903dd4d/Untitled.png)

올해 초 Amazon에서 기술이사로 계셨던 Keun이 공동 대표로 오시면서 회사에 많은 변화들이 생겨났습니다.

그 중 우선순위가 높았던 것은 기존의 캐시카우 외에 새로운 먹거리(?)를 찾기 위한 노력이었습니다. 기존 서비스는 리텐션이 낮은 도메인에 고객층이 한정되어 있다보니 매력적인 아이템이지만 투자자들에게 높은 성장률을 어필하기가 많이 힘들었다는 판단이 있었던 것 같습니다. 회사의 서비스가 이미지 변환 모델 기반의 AI 스타트업이다 보니 그와 관련된 사업들을 하기가 편했고, 새로운 사업의 MVP를 시작하기 전 다량의 AI이미지용 소스를 구하자 크롤러를 개발하기로 했습니다.

## 비즈니스 요구사항

------

AI 이미지로 다른 Stock 이미지 시장과의 챌린지로 여겼던 부분은 다음과 같습니다

1. quality (품질)
2. relevant (적합성)
3. breath of content (구색)

**quality**는 의미없는 사진을 제외한 고품질의 이미지를 획득을 목표로 했습니다.

**relevant**는 고객이 이미지를 검색했을 때 얼마나 정확한 이미지를 제공할지 고민하는 것이었습니다.

**breath of content**는 제공되는 이미지의 구색이 얼만큼 다양한가에 초점을 맞췄습니다. 예를들어 나무라는 이미지를 검색할 때 여러 구도의 나무를 제공하여 고객이 다양한 선택지를 제공하는 것을 목표로 삼았습니다.

그 중, 이미지 크롤러는 **quality**와 **relevant**에 초점을 맞추어 개발하였습니다.

즉, **높은 품질의 이미지를 고객의 적합하게 선택할 수 있는 정보들을 획득하는 것이 목표**로 설정하게 되었습니다. (breath of content의 경우, 소스 이미지를 통해 이미지 변환 모델을 통해 나오는 결과물이므로 크롤러와는 별개의 요구사항으로 처리했습니다)

## 비즈니스 목표

------

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/86a7daad-f677-4732-8988-1cc209619e45/Untitled.png)

목표는 **30억개의 이미지를 생성하기 위해 크롤러가 어떤 문제를 해결해야되는가** 였습니다.

이미지 변환 모델에서 30억개의 이미지를 생성하기 위해서는 1억개의 소스 이미지가 있으면 된다고 가정을 하였습니다. MVP까지는 총 2달 동안의 기간을 산정하여 고품질의 이미지 30억개를 생산하는 것을 목표로 삼았습니다. 그래서 **MVP 2달만에 1억개의 이미지 소스를 어떻게 생성할 것인가** 를 목표로 개발을 진행했습니다

> 개발 목표 : **MVP 2달동안 1억개의 이미지 소스를 생성**

## 리소스 산정

------

개인적으로 리소스는 **비즈니스 목표를 기준**으로 산정하는게 좋다고 생각합니다.

즉, **비즈니스 목표는 MVP 2달 기간동안 1억개의 소스 이미지를 확보**하는 목표를 기준으로 잡았습니다. 해당 목표를 기반으로 필요한 **Object Storage 용량**과 **서버의 개수**를 산정하였습니다. 개인적인 경험으로 미리 대략적인 용량과 서버의 개수 등을 산정해 놓으면 아키텍처 구성이나 비용 측면에서 좋은 결과를 도출하곤 했습니다. 이번에도 비슷한 방법을 적용해보려 합니다.

### 1. 전체 용량 산정

먼저, **1억개의 이미지에 필요한 Object Storage 용량**을 산정하였습니다

```java
전체 이미지 용량 = 전체 이미지 개수(1억) x 이미지 한개의 크기
```

전체 이미지 개수는 목표 개수인 1억개로 고정되어 있으므로 **이미지 한개의 크기**만 있으면 됩니다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3a018998-9d5a-4614-bcd0-962abbfcd0bb/Untitled.png)

미리 Shutterstock에서 대략 9,900개 정도의 샘플을 채취하여 S3에 담아 놓았습니다. 총 9,974개의 이미지 샘플이 955.4M를 차지하므로 **이미지 하나의 크기**는 **955.4MB / 9,974** = 약 **95.7KB**의 크기를 가지게 됩니다.

```java
전체 이미지 용량 = 전체 이미지 개수(1억 x 이미지 한개의 크기 = 100,000,000 x 95.7KB = 9.57TB 
```

즉, **1억개의 이미지를 얻기 위해서는 총 9.57TB의 Object Storage 용량**이 필요합니다. **AWS S3의 경우, 최대 5TB까지 업로드**할 수 있으므로 **약 2개 이상**의 S3 버킷이 필요한 것을 알 수 있습니다.

```java
전체 용량 = 9.57T
```

### 2. 서버 개수 산정

서버개수는 **MVP 2달을 기준**으로 산정했습니다.

이미지를 크롤링하기 위한 **전체 latency**를 계산하면 **2달 기간 동안 필요한 서버 의 개수**를 예상있습니다.

```java
전체 요청 횟수 = 전체 seed URL 개수 x 페이지 수
전체 요청 시간 = 전체 요청 횟수 x 평균 http latency(ms) (= 인스턴스 하나가 처리하는데 걸리는 시간)
서버 개수 = 전체 요청 시간 / MVP 기간(2달)
```

전체 요청 횟수

- seed URL로 이미지를 검색하면 여러개의 페이지가 나옵니다.
- 우리는 모든 페이지를 돌면서 이미지를 다운받아야 합니다.
- **모든 seed URL**로 **모든 페이지를 돌면서 요청하는 횟수**가 전체 요청횟수가 됩니다

전체 요청 시간

- 전체 요청 횟수에서 한번 요청에 걸리는 latency를 곱하면 전체 요청 시간입니다.
- 이는 **서버 한대가 처리할때 걸리는 시간**이 됩니다.

서버 개수

- 전체 서버 요청 시간은 서버 한대가 처리할때 걸리는 시간입니다.
- 예를들어, 서버 한대가 이미지를 모두 다운받는데 43,200,000ms가 걸린다고 가정해봅시다 (약 4달)
- 한대가 4달 걸리는 작업을 2달로 만들려면 2대의 서버가 필요합니다.
- 전체 요청 시간(4달) / MVP 기간(2달) = 서버 개수

### **2-1. 전체 seed URL 개수**

전체 seed URL 개수를 구하는 계산식은 다음과 같이 정했습니다.

```java
전체 seed URL 개수 = 하나의 keyword로 만들 수 있는 조합(seed URL) 개수 x 목표 keyword 개수 (1억개 기준)
```

하나의 keyword로 만들 수 있는 조합 개수

- 검색 쿼리 특성상 **keyword와 parameter의 조합으로 seed URL을 생성**할 수 있습니다.
- 예를들어, human이라는 keyword가 gender, age 등의 여러 parameter 조합들로 seed URL을 생성합니다.
- 즉, **하나의 keyword로 만들 수 있는 조합**의 개수가 seed URL의 개수가 됩니다.

목표 keyword 개수

- 우리는 목표 개수인 1억개의 이미지를 얻기 위해서는 하나의 keyword 당 몇개의 이미지가 있어야 하는지 알아야 합니다.
- 예를들어 Shutterstock의 keyword 하나가 평균 10,000,000(천만)개의 이미지를 가지고 있으면 1억개의 이미지를 얻기위해 10개의 keyword가 필요합니다.

즉, **하나의 keyword로 만들 수 있는 조합이 seed URL이 되고**, 이를 **목표 keyword 개수**(1억개의 이미지를 다운로드하기 위한 keyword 개수)를 곱하면 **전체 seed URL**이 됩니다. 전체 seed URL의 개수 기반으로 전체 요청 횟수를 구할 수 있습니다

**하나의 keyword로 만들 수 있는 조합 개수**는 다음과 같이 알 수 있습니다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/891a5930-0353-4d01-a314-58d2fe170a4a/Untitled.png)

Shutterstock의 경우, **keyword에 sort, page, language, age 등 다양한 파라미터들**을 가질 수 있습니다.

각 parameter도 옵션들을 가지는데 age의 경우 infants, children, teenagers, 20s, 30s, 40s, 50s, 60s, older 등 9개의 옵션들을 가집니다. Shutterstock의 경우 약 4,320개의 keyword와 parameter 조합이 나옵니다. 해당 조합으로 seed URL을 만드므로 seed URL이 **4,320개** 라고 할 수 있습니다.

```java
keyword당 조합(seed URL) 개수 = 2 x 2 x 9 x 4 x 15 x 2 = 4,320개
```

다음으로 **목표 keyword 개수**(1억 개의 이미지를 만들기 위한 keyword 개수)를 계산해보았습니다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/02a05918-137b-44f3-b091-ce7eaa350d80/Untitled.png)

Shutterstock에서 미리 keyword가 가지는 이미지의 개수를 계산해보았습니다.

대략 81개의 keyword를 대표값으로 평균적으로 keyword 당 약**12,800,000개**를 가지고 있는 것으로 확인할 수 있었습니다. 비즈니스의 목표는 1억개의 소스 이미지가 필요하므로 약 10개의 keyword라면 약 128,000,000(1.2억)개의 이미지를 얻을 수 있습니다. parameter별로 약 20%의 중복이 생긴다고 가정하면 **10개의 키워드**로 1억개의 소스 이미지를 생성할 수 있는 것을 알 수 있습니다.

```java
전체 seed URL 개수 = 하나의 keyword 조합(seed URL) 개수 x 목표 keyword 개수 (1억개 기준)
```

즉, 전체 seed URL을 만들기 위해 10개의 keyword가 필요하므로 4,320개 x 10 = **43,200개**의 seed URL이 필요한 것을 알 수 있습니다.

### **2-2. seed URL 당 페이지 수**

하나의 keyword에 12,000,000개의 이미지를 가지고 parameter에 따라 필터링을 하게됩니다. 대략 하나의 keyword 조합으로 이미지를 조회하면 12,000,000/4,320 = 465,740개의 결과 이미지가 나올 것이라 가정할 수 있습니다. Shutterstock의 경우 한 페이지에 100개의 이미지가 조회되므로 대략 465,740/100 = **466개**의 페이지를 요청해야 합니다.

### 2-3. 서버 개수

최종적으로 서버 개수를 계산하면 다음과 같습니다.

```java
전체 요청 횟수 = 전체 seed URL 개수 x 페이지 수
전체 요청 시간 = 전체 요청 횟수 x 평균 http latency(ms) (= 인스턴스 하나가 처리하는데 걸리는 시간)
서버 개수 = 전체 요청 시간 / MVP 기간(2달)

전체 요청 횟수 = 4,320 x 466 = 2,013,120
전체 요청 시간 = 2,013,120 x 60ms = 1,207,872,000ms = 약 11달
서버 개수 = 11달 / 2달 = 약 6대
```

즉, **서버 한대가 약 11달 동안 작업을 해야 1억개의 이미지를 모두 다운**받을 수 있습니다.

11달 동안 해야될 작업을 MVP기간인 2달안에 마치려면 **총 6대의 크롤러 인스턴스**가 필요합니다. 비용적 측면을 고려했을 때 **하나의 인스턴스에 6개의 워커 스레드**를 돌리게 되면 목표 기간안에 이미지 다운로드가 가능할 것으로 예측했습니다.

> 물론 MVP 기간 이후에는 다른 목표치로 크롤링 작업을 해야합니다. 확장성을 고려해서 아키텍처를 구성한다면 단순히 크롤러 인스턴스만 추가함으로서 부하를 조절할 수 있게 될 것입니다

## 개발 요구사항

------

개발 요구사항을 **기능적 부분**과 **비기능적 요구사항**으로 나눠보았습니다.

먼저 비 기능적 요구사항은 다음과 같습니다.

1. 확장성
2. 일관성
3. 성능

먼저 **확장성**은 확장 가능한 구조의 크롤러를 만드는 것이었습니다. 크롤러 컨트롤러와 크롤러, 다운로더 간의 결합을 낮추고 처리량에 따라서 크롤러와 다운로더를 추가할 수 있는 구조가 필요했습니다.

**일관성**은 중복되지 않은 일관된 데이터를 처리하는 것이었습니다. 이미지의 경우 매우 많은 데이터를 저장해야 하므로 중복이 발생한다면 리소스의 소모가 발생할 수 있습니다.

**성능**은 1초에 수많은 이미지를 다운받다보면 매우 느려질 수 있습니다. 분산환경에서 멀티스레드를 활용해서 최적의 성능을 유지하는 것을 목표로 삼았습니다

기능적 요구사항은 단순합니다

1. Crawler Controller : Crawler의 공통 데이터를 처리하는 모듈
2. Crawler : 크롤링 작업을 수행하는 모듈
3. Downloader : 다운로드 작업을 수행하는 모듈

기능적 및 비기능적 요구사항을 통해 다음과 같이 요구사항을 정리할 수 있었습니다

1. keyword와 parameter을 주기적으로 요청한다
2. Crawler와 Downloader는 높은 가용성과 복구 탄력성을 가진다
3. Crawler와 Downloader는 중복 데이터를 허락하지 않는다
4. Crawler Controller, Crawler와 Downloader 간 결합도를 최대한 낮춘다

## 기술 스택

------

이미지 크롤러의 기술스택은 다음과 같습니다

- Python3
- Fastapi
- Apache Kafka
- AWS S3
- AWS DocumentDB
- AWS MySQL

### 1-1. DocumentDB vs DynamoDB

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f98607e4-cb34-42f9-a6d7-0fb0e588b90d/Untitled.png)

이미지의 메타데이터를 저장하기위해 NoSQL을 사용하기로 했습니다.

AWS를 사용하는 상황에서 NoSQL로는 크게 2가지 옵션이 있었습니다.

- DocumentDB
- DynamoDB

저희는 **DocumentDB**를 사용하기로 했습니다. DocumentDB는 MongoDB와 DynamoDB의 중간 정도되는 NoSQL 데이터베이스입니다. 즉, MongoDB(up until 3.6v)의 feature를 대부분 지원하는 동시에 auto-scaling과 replication을 손쉽게 할 수 있습니다.

초기에는 MongoDB를 사용하려했습니다. 하지만 처리해야 할 메타데이터의 양을 생각하면 스케일을 세팅을 고려하지 않을 수 없었습니다. 또한 어느정도 안정화되면 MongoDB로 이관하려 했기 때문에 DocumentDB가 적합하다 판단했습니다. AWS 서비스이기 때문에 기본적인 설정 또한 쉽게 가능했습니다. 로컬에서 테스트 시 MongoDB로 대체할 수 있다는 부가적인 장점도 있었습니다.

문제는 **비용**이었는데요. 단일 DocumentDB 기본 서버 스펙 매우 고스펙이었습니다. 2CPU에 16GB RAM이었는데요. 대략 200달러가 넘는 금액입니다. 이는 AWS에서 99.99%의 가용성을 보장하도록 디자인했기 때문이라 하는데, 그것을 감안하더라도 조금 높지 않나 생각이 됩니다.

### 1-2. Kafka vs AWS SQS/SNS vs RabbitMQ

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/de2a0752-3605-4745-bf93-77ee48e4ac82/Untitled.png)

이미지 크롤러에서 핵심 기능은 모듈들 간의 디커플링과 확장성이었습니다.

비동기적으로 메세지를 전달하고 확장이 용이한 publish/subscribe 모델의 Message Queue를 사용하기로 했습니다. Message Queue로는 Kafka, SQS/SNS 그리고 RabbitMQ 3가지 옵션이 있었습니다.

- Kafka
- SQS/SNS
- RabbitMQ

저희는 Message Queue로 **Kafka**를 사용하기로 했습니다. 이미지 크롤러 개발에서 핵심 기능 중 하나가 복구기능이었는데요. 크롤러나 다운로더가 어떠한 이유에서 다운되더라도 데이터를 계속 이어나가야 했었습니다. RabbitMQ의 경우는 메세지가 한번 소비되면 메세지가 사라지는 반면 Kafka는 주어진 retention 기간동안 메세지를 지우지 않고 파일 시스템에 저장하는 방식이라 언제든지 복구할 수 있었습니다.

또한 오픈 소스였기 때문에 언제든지 이관이 가능합니다. 스타트업이다 보니 비용 절감이 매우 중요했는데, 혜택을 주는 클라우드 서비스에 따라 언제든지 이관을 할 수 있어야 했습니다. (저희 회사는 Naver에서 투자를 받아 ncp에서 비용지원을 받아왔습니다). AWS SQS/SNS는 AWS에 종속적이다 보니 이관을 고려했을 때 좋지 않은 선택지였습니다.

러닝커브도 고려하지 않을 수 없었는데요. 지금까지 AI 변환 모델의 데이터파이프라인으로 Kafka를 사용하고 있었기 때문에 조금 더 쉽게 시작할 수 있다 판단했습니다.

물론 Kafka가 단점도 있습니다. 바로 Kafka에서 공식적으로 제공하는 Monitoring 시스템이 없다는 점인데요. Message Queue 서비스에서 메세지의 흐름을 모니터링하는 것은 매우 중요하다 생각합니다. 완벽하지 않지만 대안으로 Kafka UI를 사용해서 모니터링 하기로 결론내게 되었습니다.

> AWS **SQS/SNS**는 사용해보지 못했지만 결합했을 때 상당히 강력한 기능을 제공한다 생각합니다. **SNS**는 다수의 사용자에게 일시적인 메세지를 broadcast(push) 가능하고, **SQS**는 FIFO 방식의 queue를 통해 단일 사용자에게 메세지를 전달합니다. 즉, SNS가 다수의 SQS 서비스에 메세지를 broadcast하면 queue에 메세지를 담아 다른 서비스에 전달합니다. 기회가 되면 한번 살펴보면 좋을 것 같습니다 🙂

## 아키텍처

------

크롤러 아키텍처에서 가장 중요한 부분은 **확장성**과 **복구 가능한 구조**였습니다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d00b112b-58de-4ce3-85ce-f18a29e8b17f/Untitled.png)

전체 플로우는 다음과 같습니다.

1. key word 데이터베이스에서 이미지의 키워드와 옵션 파라미터를 받아옵니다
2. Crawler Controller는 키워드와 옵션을 조합하여 source url을 생성합니다
3. 생성한 source url을 카프카 브로커를 통해 각 Cralwer로 보냅니다
4. 크롤러는 source url로 메타데이터와 이미지 url을 받아옵니다
5. 크롤러는 실패 지점을 파악하기 위해 Progress DB에 현재 시점의 데이터를 입력하고 메타 DB에 메타데이터를 입력하게 됩니다.
6. 크롤러는 메세지 브로커에게 다운받아야할 이미지 url 리스트를 이벤트로 전달합니다.
7. 다운로더는 이미지 url을 받아 실제 이미지를 다운로드 받고 blob 스토리지 및 url 데이터베이스에 저장하게 됩니다.

이러한 구조는 다음과 같은 장점을 가지게 됩니다

첫번째, Crawler Controller, Crawler와 Downloader간 결합도가 낮아집니다. Crawler Controller는 어떤 Crawler를 사용하고 어떻게 구현되어 있는지 알필요가 없습니다. 단지 Message Broker로 이벤트만 던질 뿐 이벤트를 consume하는 곳에서 이벤트를 처리할 책임을 가집니다. Message Broker로 두 모듈간 의존성을 분리하므로 Crawler를 추가/제거하더라도 Crawler Controller에 변경이 발생하지 않습니다.

두번째, Crawler와 Downloader가 추상화됩니다. Crawler은 seed URL로 이미지 url을 받고 Downloader는 이미지 url로 이미지를 다운로드한다는 역할만 가지고 있습니다. 그래서 Crawler에 Instagram Crawler 혹은 Shuttorstock Crawler 등 구현체만 생성하면 언제든지 추가할 수 있습니다.

그럼 각각의 역할에 대해 살펴보겠습니다.

## 1. Crawler Controller

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/028e5950-4d9e-433e-bf58-9dfd44802ca8/Untitled.png)

Crawler Controller의 역할은 단순합니다.

각 사이트에서 keyword와 parameter를 저장한 DB에서 seed URL을 생성하기 위해 keyword와 parameter를 조합합니다. 예를들어 Shutterstock의 경우는 사람이라는 keyword에 sort, page, language, age 등 다양한 파라미터들을 가질 수 있습니다. 각 parameter도 옵션들을 가지는데 age의 경우 infants, children, teenagers, 20s, 30s, 40s, 50s, 60s, older 등 9개의 옵션들을 가집니다. Shutterstock의 경우 약 **4,320개**의 keyword와 parameter 조합이 나옵니다.

생성된 keyword와 parameter 조합으로 seed URL을 생성할 수 있습니다. 해당 조합을 Message Broker를 통해 Crawler로 전달 됩니다.

## 2. Crawler

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0cc0027c-97d4-487b-bbff-d674857376aa/Untitled.png)

Crawlers는 단순히

## 3. Downloader

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/47ccaf12-2b32-44d1-bf2b-f67c7601f3e3/Untitled.png)

Downloader는 단순히 Mesage Broker로부터 이미지 url 리스트를 받아 순수하게 다운로드만 수행하는 역할을 합니다.

이미지 url 리스트를 Thread Pool을 이용해 Download Worker에게 전달해 병렬적으로 다운로드처리하게 됩니다.

이미지를 받으면 S3에 이미지를 업로드하고 MySQL에 이미지 정보를 담게됩니다.

Downloader를 구현하면서 요구사항 중 하나는 **이미지의 중복을 허용하지 않는다** 였는데요

처음에는 단순히 Downloader가 가진 이미지 url을 Unique Key로 비교하는 것이었습니다.

문제는 인스타그램 같은 몇몇 웹사이트에서 **이미지 url을 일정 시간이 지나면 expired 시키고 새로운 url을 부여**합니다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/872dd749-03d3-4165-8eb8-04cc252d6f58/Untitled.png)

url을 Unique Key로 저장했을 때 url이 변경된다면 더 이상 데이터의 일관성을 보장하지 못하게 되는 문제가 발생합니다

중복 방지를 위해 re-crawling하게 되었을 때, 이미지 url이 모두 변경되었다면 똑같은 이미지를 중복으로 다운받게되는 불상사가 발생합니다.

이를 해결하기 위해 **image blob 자체를 hash algorithm을 통해 checksum을 구**하기로 했습니다.

이미지 url이 변경되어도 원본 이미지 자체는 변하지 않기 때문에 이미지의 hash값을 checksum으로 비교하는 것입니다.

Hash algorithm으로 MD5와 SHA-256을 고려하였습니다.

- MD5
- SHA-256

다음과 같은 이유로 **MD5**를 사용하기로 했습니다.

이유는 **가장 빠른 hash algorithm**이기 때문이었는데요. Downloader의 경우 다수의 이미지를 여러대의 Worker를 통해 해싱해야 되었기 때문에 hashing의 속도가 중요한 요소가 되었습니다. 통상적으로 SHA-256이 20% 정도 느리다고 알려져 있기 때문에 짧은 크롤링 기간을 고려한다면 처리량이 많은 MD5가 괜찮은 선택이 될 것이라 생각했습니다.

물론 collision이 발생할 확률이 높다는 단점도 있습니다. 만약 MVP 개발 이후 collision 발생 기대치보다 많다면 이후 실서비스 개발에서 SHA-256과 같은 collision 발생이 덜한 hash algorithm을 선택하기로 했습니다.

## 추가 개선 사항

------

짧은 기간동안 크롤러를 개발하면서 오버 엔지니어링을 피하기 위해 노력을 했습니다. 그렇다보니 아쉬운 부분들이 많았는데요. MVP 이후, 본격적으로 서비스를 시작한다면 고려해봐야할 부분들을 정리해보았습니다.

### 1. Crawler Trap

Crawler Trap은 **아키텍처 실수로 발생한 오염된 url을 크롤러가 사용하여 크롤러가 영원히 동작하지 않는 경우**를 말합니다. 크롤러 개발시 Cralwer Trap을 잘 설계해야 됩니다. 저희 서비스의 경우 유효하지 않은 응답을 받아오는 경우는 다음 크롤링 작업으로 넘겨버립니다. 추후에 다시 크롤링 할 예정이기 때문입니다.

### 2. DNS Resolver

크롤러에는 DNS Resolver를 보통 두는 편입니다. url로 dns 서버를 갔다오는 것은 시간이 꽤 소요되는 작업입니다. 그러므로 DNS 서버에서 ip캐싱해서 사용하면 더 빠른 성능을 가져올 수 있습니다.

### 3. Re-crawling

크롤러는 현재 저장되어있는 데이터를 업데이트, 검증 및 중복 방지를 위해서 재 크롤링 됩니다. 이때 우선순위가 중요한데 현재는 가장 오래된 데이터부터 받아오기 때문에 추후 재 크롤링 과정에서 오래된 데이터 순으로 조회하여 중복을 제거할 수 있습니다.

### 4. Consistent hashing

크롤러와 다운로더 인스턴스들은 유니크한 아이디를 가지고 있어야 합니다. 각 인스턴스를 올리고 내릴 때마다 새로운 아이디를 발급받는게 아니라 기존 아이디를 발급 받아야 됩니다. consistent hashing으로 각 노드들의 유니크한 아이디를 유지해야 될 것 같습니다.

## 정리

------

이번 크롤러 개발을 하면서 느낀 점은 **비즈니스 요구사항의 이해도**에 따라 개발의 효율이 높아질 수 있다는 것을 알게 되었습니다. 비즈니스 목표에 따라 리소스를 먼저 산정하니 필요한 비용이 얼마정도인지, 얼만큼의 인스턴스가 필요한지, 아키텍처에서 어떤 부분들이 부족했었는지를 알 수 있는 시간이었습니다.

또한 크롤러 디자인을 처음 해보다 보니 많은 시행착오들이 있었습니다. 개인적으로는 시스템 디자인을 어떻게 접근해야 되는지 이해하는 시간이였던 것 같습니다. educative나 [system-design-primer](https://github.com/donnemartin/system-design-primer) 등의 디자인 시스템 관련 사이트들을 참고하면서 좋은 리소스를 기반으로 학습하고 적용하는게 중요하다는 것을 또 다시 느끼게 되었습니다.

다만 짧은 기간동안 작업을 하면서 새로운 기술들을 익히는 시간이 부족한게 아쉬웠는데, 조금씩 학습해 나가면서 더 좋은 크롤러로 발전 시킬 수 있으면 좋겠습니다 🙂

## 참조

------

https://www.youtube.com/watch?v=oVyWe4VqydQ&ab_channel=NibirPaul

https://stackoverflow.com/questions/58970006/are-sqs-and-kafka-same

https://www.aspecto.io/blog/kafka-vs-rabbitmq-vs-aws-sns-sqs-which-broker-to-choose/

https://medium.com/double-pointer/kafka-vs-activemq-vs-rabbitmq-vs-amazon-sns-vs-amazon-sqs-vs-google-pub-sub-4b57976438db

https://www.interviewbit.com/blog/sql-vs-nosql

https://www.interviewbit.com/mongodb-interview-questions/

https://github.com/donnemartin/system-design-primer/blob/master/README.md#nosql

https://cdgibson.medium.com/difference-between-aws-dynamodb-vs-aws-documentdb-vs-mongodb-9cb026a94767

https://jane-aeiou.tistory.com/57

https://stackoverflow.com/questions/58970006/are-sqs-and-kafka-same

https://cloudinfrastructureservices.co.uk/kafka-vs-sqs-whats-the-difference/

https://infosecscout.com/md5-vs-sha256/

https://www.quora.com/Can-MD5-and-SHA-algorithms-be-used-to-hash-images-If-so-is-MD5-or-SHA-better-than-the-difference-hashing-algorithm-to-get-a-unique-hash-for-every-image
