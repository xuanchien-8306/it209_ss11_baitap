# Student Management API

Dự án Spring Boot REST API quản lý thông tin sinh viên, sử dụng MySQL chạy trong Docker.

---

## Công nghệ sử dụng

| Công nghệ | Phiên bản |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.3 |
| Spring Data JPA | (kèm theo Spring Boot) |
| MySQL | 8.0 |
| Gradle | 8.x |
| Docker & Docker Compose | - |
| Lombok | (kèm theo Spring Boot) |

---

## Cấu trúc thư mục

```
baitap/
├── src/
│   ├── main/
│   │   ├── java/com/baitap/
│   │   │   ├── BaitapApplication.java       # Lớp khởi chạy ứng dụng
│   │   │   ├── DataInitializer.java         # Tạo dữ liệu mẫu khi khởi động
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java   # REST API endpoints
│   │   │   ├── service/
│   │   │   │   └── StudentService.java      # Xử lý nghiệp vụ
│   │   │   ├── repository/
│   │   │   │   └── StudentRepository.java   # Truy vấn database
│   │   │   ├── entity/
│   │   │   │   └── Student.java             # JPA Entity
│   │   │   ├── dto/
│   │   │   │   ├── StudentRequest.java      # Nhận dữ liệu đầu vào
│   │   │   │   ├── StudentResponse.java     # Trả dữ liệu ra
│   │   │   │   └── PageResponse.java        # Wrapper kết quả phân trang
│   │   │   └── exception/
│   │   │       ├── StudentNotFoundException.java
│   │   │       ├── DuplicateStudentCodeException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties       # Cấu hình ứng dụng
│   └── test/
│       └── java/com/baitap/service/
│           └── StudentServiceTest.java      # Unit test
├── .env                                     # Biến môi trường (không commit lên Git)
├── .env.example                             # Mẫu file .env
├── Dockerfile                               # Build image Spring Boot
├── docker-compose.yml                       # Khởi chạy MySQL + Spring Boot
├── build.gradle                             # Cấu hình Gradle
└── README.md
```

---

## Cấu hình `.env`

Sao chép file mẫu và chỉnh sửa nếu cần:

```bash
cp .env.example .env
```

Nội dung file `.env`:

```env
# MySQL Container
MYSQL_DATABASE=studentdb
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_USER=student_user
MYSQL_PASSWORD=student_pass

# Spring Boot kết nối tới MySQL (trong Docker network)
DB_HOST=mysql
DB_PORT=3306
DB_NAME=studentdb
DB_USERNAME=student_user
DB_PASSWORD=student_pass

# Port ứng dụng
APP_PORT=8080
```

> **Lưu ý:** `DB_HOST=mysql` là tên dịch vụ trong Docker Compose, **không phải** `localhost`.

---

## Khởi chạy bằng Docker Compose

```bash
docker compose up --build -d
```

Lệnh này sẽ:
1. Build image Spring Boot từ `Dockerfile`.
2. Khởi chạy container MySQL (chờ healthy).
3. Khởi chạy container ứng dụng Spring Boot.

---

## Kiểm tra container và log

```bash
# Xem trạng thái các container
docker compose ps

# Xem log của ứng dụng Spring Boot
docker compose logs app

# Xem log theo thời gian thực
docker compose logs -f app

# Xem log MySQL
docker compose logs mysql
```

---

## Các API và ví dụ request

Base URL: `http://localhost:8080`

### 1. Thêm sinh viên

```http
POST /api/students
Content-Type: application/json

{
  "studentCode": "SV006",
  "name": "Nguyen Van F",
  "dateOfBirth": "2003-01-15",
  "gender": "Nam",
  "email": "f@example.com",
  "gpa": 3.50
}
```

### 2. Lấy danh sách sinh viên (có phân trang, tìm kiếm, sắp xếp)

```http
GET /api/students
GET /api/students?page=0&size=10
GET /api/students?name=Nguyen&page=0&size=5
GET /api/students?studentCode=SV001
GET /api/students?sortBy=gpa&direction=desc
GET /api/students?name=Nguyen&sortBy=gpa&direction=desc&page=0&size=10
```

Tham số hỗ trợ:

| Tham số | Mô tả | Mặc định |
|---|---|---|
| `name` | Tìm theo tên (không phân biệt hoa thường) | - |
| `studentCode` | Tìm theo mã sinh viên | - |
| `page` | Trang hiện tại (bắt đầu từ 0) | `0` |
| `size` | Số bản ghi mỗi trang (1–100) | `10` |
| `sortBy` | Sắp xếp theo: `name`, `gpa`, `dateOfBirth` | `name` |
| `direction` | Chiều sắp xếp: `asc` hoặc `desc` | `asc` |

Ví dụ response:

```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 5,
  "totalPages": 1,
  "last": true
}
```

### 3. Lấy chi tiết sinh viên

```http
GET /api/students/1
```

### 4. Cập nhật sinh viên

```http
PUT /api/students/1
Content-Type: application/json

{
  "studentCode": "SV001",
  "name": "Nguyen Van An Updated",
  "dateOfBirth": "2002-05-15",
  "gender": "Nam",
  "email": "an.updated@example.com",
  "gpa": 3.80
}
```

### 5. Xóa sinh viên

```http
DELETE /api/students/1
```

---

## Dừng hệ thống

```bash
# Dừng và xóa container (giữ lại dữ liệu trong volume)
docker compose down

# Dừng và xóa cả dữ liệu (xóa volume)
docker compose down -v
```

---

## Xử lý lỗi

| HTTP Status | Tình huống |
|---|---|
| `201 Created` | Tạo sinh viên thành công |
| `200 OK` | Thao tác thành công |
| `204 No Content` | Xóa thành công |
| `400 Bad Request` | Dữ liệu đầu vào không hợp lệ |
| `404 Not Found` | Không tìm thấy sinh viên |
| `409 Conflict` | Mã sinh viên đã tồn tại |
| `500 Internal Server Error` | Lỗi hệ thống |
