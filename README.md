# m1n67un's MCM Template

## Development Infomation
- Java 17+
- Spring boot 3.0.1
- Logback 1.4
- MariaDB 3.1
- Gradle 9.0

## Interface

//TODO: 추가 기술
### header 
- token : 갱신 access token

//TODO: 추가 기술
### body
```json
{
    "code": "S000", // 요청 처리의 상태 코드
    "message": "정상적으로 처리 되었습니다.", // 요청 처리의 상태 메세지
    "data": {} // 요청 처리의 결과 데이터 Object or Array
}
```

//TODO: 추가 기술
### Status Code
response status code 모두를 사용할 경우 관리가 어렵기 때문에, 아래와 같이 제한된 코드를 사용.
에러는 response의 status를 사용하고, response body에 error detail을 기술.

```
2xx : 요청을 성공적으로 처리
200 : 성공
4xx : 클라이언트 오류
400 : Bad Request - field validation 실패
401 : Unauthorized - 토큰 검증 실패 - 토큰이 없거나, 비정상 토큰 - 토큰 사용 시간 초과
403 : Forbidden - API 사용 권한 없음
404 : Not found - 해당 리소스 없음
5xx : 서버가 유효한 요청을 명백하게 수행하지 못했음
500 : Internal Server Error - 서버 내부 오류
503 : Service Unavailable - 서비스를 사용할 수 없음 (외부 api 호출에 대한 오류에 사용)
```

## [m1n67un Java Code Conventions]

## 목차
1. [파일 인코딩](#1-파일-인코딩)
2. [이름 규칙](#2-이름-규칙)
3. [식별자 이름](#3-식별자-이름)
4. [패키지 명명](#4-패키지-명명)
5. [클래스/인터페이스 명명](#5-클래스인터페이스-명명)
6. [메서드와 변수 명명](#6-메서드와-변수-명명)
7. [상수 명명](#7-상수-명명)
8. [임시 변수 사용](#8-임시-변수-사용)
9. [클래스 선언](#9-클래스-선언)
10. [클래스 구조](#10-클래스-구조)
11. [변수 선언](#11-변수-선언)
12. [들여쓰기](#12-들여쓰기)
13. [중괄호 스타일](#13-중괄호-스타일)
14. [조건문과 반복문](#14-조건문과-반복문)
15. [패키지 선언](#15-패키지-선언)
16. [메서드 간 간격](#16-메서드-간-간격)

### 1. 파일 인코딩
- UTF-8 방식을 사용한다.

### 2. 이름 규칙
- 식별자에는 영문/숫자/언더스코어만 허용한다.
- 변수명, 클래스명, 메서드명 등에는 영어와 숫자만을 사용한다.
- 상수에는 단어 사이의 구분을 위하여 언더바(_)를 사용한다.

### 3. 식별자 이름
- 식별자의 이름을 한국어 발음대로 영어로 옮겨 표기하지 않는다.
  - 나쁜 예: `gubun`
  - 좋은 예: `division`, `category`
- 임시 변수 외에 숫자 표기를 사용하지 않는다.

### 4. 패키지 명명
- 패키지 이름은 소문자로만 구성한다.
- 특수문자, 대문자, 언더바 사용을 금지한다.
  예: `com.mg`

### 5. 클래스/인터페이스 명명
- Upper CamelCase 표기법을 적용한다.
```java
  public class Mingyun {
      // ...
  }
```
### 6. 메서드와 변수 명명
- Lower Camel Case 표기법을 적용한다.
- 첫 번째 단어를 소문자로 작성하고, 이어지는 단어의 첫 글자를 대문자로 작성한다.
```java
  public void mingyun() throws MingyunException {
      // ...
  }
```
### 7. 상수 명명
- 상수의 경우, 대문자와 언더바(_)로만 구성한다.

예시:
```java
public static final int MIN_GYUN = 1;
```

### 8. 임시 변수 사용
- 임시 변수 외에 숫자 표기를 사용하지 않는다.

### 9. 클래스 선언
- 한 개의 Java 파일 내에 최상위 클래스는 1개만 선언한다.

### 10. 클래스 구조
클래스 선언 시 상위, 멤버 변수, 생성자, 메서드 순으로 작성한다.

예시:
```java
class Minhyun {
    public static final String MIN_GYUN = "Min gyun";

    public int a = 5;

    public Mingyun() {}

    public void work() {}
}
```

### 11. 변수 선언
한 줄에 여러 문장을 사용하지 않고, 하나의 변수만을 다룬다.

예시:
```java
// 잘못된 예
int min, gyun;
int min  = 0; int gyun = 0;

// 올바른 예
int min = 0;
int gyun = 0;
```

### 12. 들여쓰기
들여쓰기는 4개의 빈 칸에 들여쓰기 단위로 사용한다.

### 13. 중괄호 스타일
중괄호 선언은 K&R 스타일(Kernighan and Ritchie style)을 따른다.
- 줄의 마지막에서 시작 중괄호 `{`를 쓰고 열고 새 줄을 삽입한다. 블럭을 마친 후에는 새 줄 삽입 후 중괄호를 닫는다.

예시:
```java
public class Mingyun {
    public boolean isValid(String a) {
        if (a == null) {
            return false;
        }
        return true;
    }
}
```

### 14. 조건문과 반복문
조건/반복문에 중괄호를 필수로 사용한다.

예시:
```java
// 잘못된 예
if (val == null) return false;

// 올바른 예
if (val == null) {  
    return false;
}
```
### 15. 패키지 선언
package 선언 후 빈 줄을 추가한다.

예시:
```java
package mg;

import java.util.List;
```

### 16. 메서드 간 간격
메서드의 선언이 끝난 후 다음 메서드 선언이 시작되기 전에 빈줄을 삽입한다.

예시:
```java
public void setId(int id) {
    this.id = id;
}
 
public void setName(String name) {
    this.name = name;
}
```

## [소프트퍼즐 템플릿 작성 규칙]
- 작성 규칙은 스웨거 기준

### Controller
- SPRestController 어노테이션 선언
- Controller 클래스 선언 부, Tag 어노테이션 사용(name, description 작성)
- 각 메서드 별, Operation 어노테이션 사용(summary, description 작성)

```java
@SPRestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SAMPLE", description = "공통 샘플 요청을 처리한다.")
@RequestMapping("/sample")
public class SampleController {

    private final SampleService SampleService;

    @Operation(summary = "샘플", description = "(Sample) 로그인을 수행하고, 인증에 필요한 토큰을 발급받는다.")
    @PostMapping("/sample/login-request")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return sampleService.login(loginRequestDTO);
    }
}
```

#### URL 작성 규칙
- Mapping되는 URL에 _(언더바), 대문자(upper case), 한글, _를 제와한 특수문자 등을 사용하지 않는다.
- 케밥 케이스(Ke-bab Case)를 원칙으로 한다.

#### 메서드 네이밍 규칙
예시:
```cmd
orderList() – 목록 조회 유형의 메서드
orderDetail() – 단 건 상세 조회 유형의 controller 메서드
orderSave() – 등록/수정/삭제 가 동시에 일어나는 유형의 controller 메서드
orderAdd() – 등록만 하는 유형의 controller 메서드
orderModify() – 수정만 하는 유형의 controller 메서드
orderRemove() – 삭제만 하는 유형의 controller 메서드
```

### Service
#### 메서드 네이밍 규칙
예시:
```cmd
findOrder() - 조회 유형의 service 메서드
addOrder() - 등록 유형의 service 메서드
modifyOrder() - 변경 유형의 service 메서드
removeOrder() - 삭제 유형의 service 메서드
saveOrder() – 등록/수정/삭제 가 동시에 일어나는 유형의 service 메서드
```

### Mapper
#### 메서드 네이밍 규칙
예시:
```cmd
selectOrder(); - 조회 유형의 mapper 메서드
insertOrder(); - 등록 유형의 mapper 메서드
updateOrder(); – 변경 유형의 mapper 메서드
deleteOrder(); - 삭제 유형의 mapper 메서드
```

### DTO
#### 메서드 네이밍 규칙
예시:
```cmd
URL Mapping Method
- Request DTO(파일명) -> MingyunReqDTO
- Response DTO(파일명) -> MingyunResDTO

이 외에 별도의 메서드 안에서 작성할 DTO는 네이밍에 Req, Res를 붙이지 않는다.
Req, Res DTO는 Controller - Mapping URL 1개의 단위마다 각 1개씩 생성한다.

필드명의 경우, 카멜 케이스(Camel Case) 형태의 네이밍으로 한다.
* DB 테이블 및 필드 명은 스네이크 케이스를 작성 원칙으로 한다.
  - MapUnderscoreToCamelCase 기능 사용
```