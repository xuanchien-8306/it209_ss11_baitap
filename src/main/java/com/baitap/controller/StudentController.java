package com.baitap.controller;

import com.baitap.dto.PageResponse;
import com.baitap.dto.StudentRequest;
import com.baitap.dto.StudentResponse;
import com.baitap.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * POST /api/students
     * Thêm sinh viên mới
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/students
     * Lấy danh sách sinh viên, hỗ trợ tìm kiếm, sắp xếp và phân trang
     * Ví dụ: GET /api/students?name=Nguyen&studentCode=SV001&page=0&size=10&sortBy=gpa&direction=desc
     */
    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        PageResponse<StudentResponse> response = studentService.getStudents(
                name, studentCode, page, size, sortBy, direction
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/students/{id}
     * Lấy chi tiết sinh viên theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        StudentResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/students/{id}
     * Cập nhật thông tin sinh viên
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request
    ) {
        StudentResponse response = studentService.updateStudent(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/students/{id}
     * Xóa sinh viên
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
