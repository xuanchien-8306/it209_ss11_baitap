package com.baitap.service;

import com.baitap.dto.PageResponse;
import com.baitap.dto.StudentRequest;
import com.baitap.dto.StudentResponse;
import com.baitap.entity.Student;
import com.baitap.exception.DuplicateStudentCodeException;
import com.baitap.exception.StudentNotFoundException;
import com.baitap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // Danh sách các trường được phép sắp xếp (tránh SQL injection qua sortBy)
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "gpa", "dateOfBirth");

    /**
     * Thêm sinh viên mới
     */
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        // Kiểm tra mã sinh viên có bị trùng không
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new DuplicateStudentCodeException(request.getStudentCode());
        }

        Student student = Student.builder()
                .studentCode(request.getStudentCode())
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .gpa(request.getGpa())
                .build();

        Student saved = studentRepository.save(student);
        return mapToResponse(saved);
    }

    /**
     * Lấy danh sách sinh viên với tìm kiếm, sắp xếp và phân trang
     */
    public PageResponse<StudentResponse> getStudents(
            String name,
            String studentCode,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        // Kiểm tra tham số phân trang hợp lệ
        if (page < 0) {
            throw new IllegalArgumentException("Tham số 'page' không được nhỏ hơn 0");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Tham số 'size' phải từ 1 đến 100");
        }

        // Kiểm tra trường sắp xếp có hợp lệ không
        String sortField = "name"; // mặc định
        if (sortBy != null && !sortBy.isBlank()) {
            if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
                throw new IllegalArgumentException(
                        "Trường sắp xếp không hợp lệ. Chỉ chấp nhận: " + ALLOWED_SORT_FIELDS
                );
            }
            sortField = sortBy;
        }

        // Xác định chiều sắp xếp
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(direction)) {
            sortDirection = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        // Chuyển chuỗi tìm kiếm rỗng thành null để query JPQL xử lý đúng
        String nameParam = (name != null && !name.isBlank()) ? name : null;
        String codeParam = (studentCode != null && !studentCode.isBlank()) ? studentCode : null;

        Page<Student> studentPage = studentRepository.searchStudents(nameParam, codeParam, pageable);

        List<StudentResponse> content = studentPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PageResponse.<StudentResponse>builder()
                .content(content)
                .page(studentPage.getNumber())
                .size(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .last(studentPage.isLast())
                .build();
    }

    /**
     * Lấy chi tiết một sinh viên theo ID
     */
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return mapToResponse(student);
    }

    /**
     * Cập nhật thông tin sinh viên
     */
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        // Kiểm tra mã sinh viên mới có trùng với sinh viên khác không
        if (studentRepository.existsByStudentCodeAndIdNot(request.getStudentCode(), id)) {
            throw new DuplicateStudentCodeException(request.getStudentCode());
        }

        student.setStudentCode(request.getStudentCode());
        student.setName(request.getName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setEmail(request.getEmail());
        student.setGpa(request.getGpa());

        Student updated = studentRepository.save(student);
        return mapToResponse(updated);
    }

    /**
     * Xóa sinh viên theo ID
     */
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Chuyển đổi entity Student sang StudentResponse DTO
     */
    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .name(student.getName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .email(student.getEmail())
                .gpa(student.getGpa())
                .build();
    }
}
