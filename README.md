##특정 고객 거래내역 조회 서비스

###기능요구사항
* 2018, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출 (단, 취소여부가 'Y' 거래는 취소된 거래, 합계 금액은 거래금액에서 수수료를 차감한 금액)
* 2018년 또는 2019년에 거래가 없는 고객을 추출 (취소여부가 'Y' 거래는 취소된 거래)
* 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력 (취소여부가 'Y' 거래는 취소된 거래)
* 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면 해당지점의 거래금액 합계를 출력 (취소여부가 'Y' 거래는 취소된 거래)

###개발 프레임워크
* SpringBoot 2.4.5
* Embedded WAS Tomcat
* Java 8
* JPA
* Gradle 6.8.3
* H2 Database
* QueryDsl
* JUnit 5

###문제해결방법
* 연도별, 관리점별을 위해서는 GROUP BY가 필요한데 Spring Data JPA에는 지원해주지 않기 때문에 Querydsl 연동
* Exception 을 컨트롤러 단에서 감지하고 에러코드와 메시지를 노출시켜주기 위해 @ControllerAdvice, @ExceptionHandler로 커스텀 예외처리 수행

###빌드 및 실행방법
1. ./gradlew clean build
2. cd build/libs
3. java -jar transaction-0.0.1-SNAPSHOT.jar

###기본 제약사항
* API 기능명세서에서 기술된 API를 모두 개발하세요.
* Spring boot 기반의 프레임웍을 사용하세요.
* 단위 테스트(Unit Test) 코드를 개발하여 각 기능을 검증하세요.(필수사항)
* 모든 입/출력은 JSON형태로 주고 받습니다.
* 단 , 각 API에 HTTP Method들(GET|POST|PUT|DEL)은 자유롭게 선택하세요.
* README.md 파일을 추가하여, 개발 프레임웍크, 문제해결 방법, 빌드 및 실행방법을 기술하세요.