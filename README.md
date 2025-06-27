# 📘 기술 설계서 - 영화 리뷰 및 사진 관리 시스템

---

## 1. 🧾 프로젝트 개요

* **프로젝트 명**: 영화 리뷰 및 사진 관리 시스템
* **개발 언어**: Java 17
* **프레임워크**: Spring Boot
* **주요 목적**:

  * 영화 정보 CRUD
  * 리뷰 작성 및 관리
  * 이미지 업로드 처리
  * 회원 인증 및 권한 관리

---

## 2. 🔧 시스템 아키텍처

```
[Client] ──▶ [Controller] ──▶ [Service] ──▶ [Repository] ──▶ [Database]
                        │             │
                 [DTO ↔ Entity Mapper]│
                               └──▶ [File System (Image Upload)]
```

---

## 3. 📌 주요 기능 및 API 명세

### 3.1 영화 관리

| 기능    | 메서드    | 경로                 | 설명          |
| ----- | ------ | ------------------ | ----------- |
| 영화 등록 | POST   | `/api/movies`      | 영화 데이터 등록   |
| 영화 목록 | GET    | `/api/movies`      | 영화 목록 + 페이징 |
| 영화 상세 | GET    | `/api/movies/{id}` | 특정 영화 조회    |
| 영화 수정 | PUT    | `/api/movies/{id}` | 영화 정보 업데이트  |
| 영화 삭제 | DELETE | `/api/movies/{id}` | 영화 삭제       |

---

### 3.2 리뷰 관리

| 기능    | 메서드    | 경로                         | 설명           |
| ----- | ------ | -------------------------- | ------------ |
| 리뷰 등록 | POST   | `/api/reviews`             | 영화 리뷰 등록     |
| 리뷰 목록 | GET    | `/api/reviews/movie/{mno}` | 특정 영화의 리뷰 조회 |
| 리뷰 삭제 | DELETE | `/api/reviews/{id}`        | 리뷰 삭제        |

---

### 3.3 사진 업로드

| 기능     | 메서드  | 경로                | 설명               |
| ------ | ---- | ----------------- | ---------------- |
| 사진 업로드 | POST | `/api/upload`     | 이미지 파일 업로드 처리    |
| 응답 DTO | -    | `UploadResultDTO` | UUID, 경로 등 정보 포함 |

---

### 3.4 인증 및 회원 관리

* `ClubMember.java`: 사용자 계정 정보
* `ClubMemberRole.java`: 사용자 권한 Enum (`USER`, `MANAGER`, `ADMIN`)
* `AuthController.java`: 로그인, 사용자 권한 설정

---

## 4. 🗃️ 데이터 모델 (Entity)

### 4.1 Movies

```java
class Movies {
  Long mno;
  String title;
  String director;
  List<Photos> photos;
  List<Reviews> reviews;
}
```

---

### 4.2 Reviews

```java
class Reviews {
  Long reviewnum;
  String text;
  int grade;
  ClubMember member;
  Movies movie;
}
```

---

### 4.3 Photos

```java
class Photos {
  String uuid;
  String fileName;
  boolean img;
  Movies movie;
}
```

---

## 5. 🧩 DTO 및 응답 구조

* `MoviesDTO`, `ReviewsDTO`, `PhotosDTO` 등은 Entity의 정보를 클라이언트에 전달하기 위한 전용 객체입니다.
* `PageResultDTO<DTO, Entity>`: 페이징 응답 템플릿
* `UploadResultDTO`: 파일 업로드 후 UUID, 파일명, 경로 반환

---

## 6. 🧑‍💻 인증 및 권한

| 권한        | 설명            |
| --------- | ------------- |
| `USER`    | 기본 리뷰 작성 가능   |
| `MANAGER` | 리뷰 및 영화 수정 가능 |
| `ADMIN`   | 전체 관리자 권한     |

`ClubMemberRole` Enum 기반으로 `@PreAuthorize` 또는 `SecurityConfig`에서 접근 제어 구현 가능

---

## 7. 📂 파일 업로드 처리

* 저장 구조: `/연도/월/일/UUID_파일명`
* `UploadController.java`에서 MultipartFile 처리 및 `UploadResultDTO` 반환
* 이미지 여부 판별: `img` boolean 필드

---

## 8. 🧱 공통 엔티티

### BasicEntity

```java
@MappedSuperclass
abstract class BasicEntity {
  LocalDateTime regDate;
  LocalDateTime modDate;
}
```

모든 엔티티의 생성일/수정일 자동 관리

---

## 9. 🔮 확장 계획

| 항목         | 설명                              |
| ---------- | ------------------------------- |
| JWT 인증 도입  | Spring Security + JWT로 인증 개선 예정 |
| Swagger 적용 | API 문서 자동화                      |
| 추천 기능      | 영화 및 리뷰 추천 알고리즘 추가 예정           |

---

## 10. 📌 기타 참고

* `SampleController`는 테스트용 또는 개발 초기 확인용 컨트롤러입니다.
* DTO ↔ Entity 매핑은 명확한 분리 원칙을 따릅니다.

