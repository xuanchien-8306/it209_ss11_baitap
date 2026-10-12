package com.baitap.service;

import com.baitap.dto.StudentRequest;
import com.baitap.dto.StudentResponse;
import com.baitap.entity.Student;
import com.baitap.exception.DuplicateStudentCodeException;
import com.baitap.exception.StudentNotFoundException;
import com.baitap.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequest request;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .studentCode("SV001")
                .name("Nguyen Van An")
                .dateOfBirth(LocalDate.of(2002, 5, 15))
                .gender("Nam")
                .email("an@example.com")
                .gpa(new BigDecimal("3.50"))
                .build();

        request = new StudentRequest();
        request.setStudentCode("SV001");
        request.setName("Nguyen Van An");
        request.setDateOfBirth(LocalDate.of(2002, 5, 15));
        request.setGender("Nam");
        request.setEmail("an@example.com");
        request.setGpa(new BigDecimal("3.50"));
    }

    @Test
    void createStudent_Success() {
        when(studentRepository.existsByStudentCode("SV001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentService.createStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getStudentCode()).isEqualTo("SV001");
        assertThat(response.getName()).isEqualTo("Nguyen Van An");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void createStudent_DuplicateCode_ThrowsException() {
        when(studentRepository.existsByStudentCode("SV001")).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(request))
                .isInstanceOf(DuplicateStudentCodeException.class)
                .hasMessageContaining("SV001");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void getStudentById_Found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getStudentById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Nguyen Van An");
    }

    @Test
    void getStudentById_NotFound_ThrowsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteStudent_NotFound_ThrowsException() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository, never()).deleteById(any());
    }
}
