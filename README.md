# Shortener

_"긴 URL을 짧은 URL로 단축시켜주는 애플리케이션"_

### 기술 스택

- Java 17
- Spring Boot 
- Spring Data JPA
- Spring REST Docs
- MySQL

## 주요기능

### 단축 URL 생성

- 원본 URL을 DB에 저장하고, PK를 기준으로 BASE62 인코딩을 통해
  단축 URL 생성

### 단축 URL에 대한 통계 제공


- 전체 조회수, 최근 7일간의 일자별 조회수, HTTP Referer 별 조회수 조회 기능 제공

## 설계

### ERD
![erdshortener.png](src%2Fmain%2Fresources%2Ferdshortener.png)
- URL
  - 원본 URL, 단축된 URL, 유효기간 등을 기록
- URL_CALL
  - 단축 URL이 생성될 때마다 Row가 추가되며, 호출 시간과 HTTP Referer를 기록

# Shortener
