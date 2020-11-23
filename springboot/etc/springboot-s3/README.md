# Spring Boot로 S3에 파일 옮기기

### 1. 설정
**application.yml**
```.env
cloud:
  aws:
    credentials:
      accessKey: ${ACCESS_KEY}
      secretKey: ${SECRET_KEY}
    region:
      static: ap-northeast-2
      auto: false
    bucket: ${BUCKET_NAME}
    stack:
      auto: false
```