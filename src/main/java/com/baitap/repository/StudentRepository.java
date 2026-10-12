package com.baitap.repository;

import com.baitap.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Kiểm tra mã sinh viên đã tồn tại chưa
    boolean existsByStudentCode(String studentCode);

    // Kiểm tra mã sinh viên đã tồn tại chưa (trừ ID hiện tại, dùng khi update)
    boolean existsByStudentCodeAndIdNot(String studentCode, Long id);

    // Tìm kiếm theo tên hoặc mã sinh viên (kết hợp điều kiện, hỗ trợ phân trang)
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:studentCode IS NULL OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :studentCode, '%')))")
    Page<Student> searchStudents(
            @Param("name") String name,
            @Param("studentCode") String studentCode,
            Pageable pageable
    );
}
