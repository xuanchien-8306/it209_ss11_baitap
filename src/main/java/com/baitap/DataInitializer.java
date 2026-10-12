package com.baitap;

import com.baitap.entity.Student;
import com.baitap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Khởi tạo dữ liệu mẫu khi ứng dụng chạy lần đầu.
 * Chỉ thêm dữ liệu nếu bảng đang trống.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            log.info("Khởi tạo dữ liệu mẫu sinh viên...");

            List<Student> students = List.of(
                Student.builder()
                    .studentCode("SV001")
                    .name("Nguyen Van An")
                    .dateOfBirth(LocalDate.of(2002, 5, 15))
                    .gender("Nam")
                    .email("an.nguyen@example.com")
                    .gpa(new BigDecimal("3.50"))
                    .build(),
                Student.builder()
                    .studentCode("SV002")
                    .name("Tran Thi Bich")
                    .dateOfBirth(LocalDate.of(2003, 8, 22))
                    .gender("Nu")
                    .email("bich.tran@example.com")
                    .gpa(new BigDecimal("3.75"))
                    .build(),
                Student.builder()
                    .studentCode("SV003")
                    .name("Le Van Cuong")
                    .dateOfBirth(LocalDate.of(2002, 12, 1))
                    .gender("Nam")
                    .email("cuong.le@example.com")
                    .gpa(new BigDecimal("2.80"))
                    .build(),
                Student.builder()
                    .studentCode("SV004")
                    .name("Pham Thi Dung")
                    .dateOfBirth(LocalDate.of(2001, 3, 10))
                    .gender("Nu")
                    .email("dung.pham@example.com")
                    .gpa(new BigDecimal("3.20"))
                    .build(),
                Student.builder()
                    .studentCode("SV005")
                    .name("Hoang Van Em")
                    .dateOfBirth(LocalDate.of(2003, 7, 30))
                    .gender("Nam")
                    .email("em.hoang@example.com")
                    .gpa(new BigDecimal("3.90"))
                    .build()
            );

            studentRepository.saveAll(students);
            log.info("Đã khởi tạo {} sinh viên mẫu.", students.size());
        } else {
            log.info("Dữ liệu đã tồn tại, bỏ qua khởi tạo dữ liệu mẫu.");
        }
    }
}
