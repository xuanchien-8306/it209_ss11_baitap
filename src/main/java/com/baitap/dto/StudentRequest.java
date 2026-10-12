package com.baitap.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StudentRequest {

    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 20, message = "Mã sinh viên không được vượt quá 20 ký tự")
    private String studentCode;

    @NotBlank(message = "Tên sinh viên không được để trống")
    @Size(max = 100, message = "Tên sinh viên không được vượt quá 100 ký tự")
    private String name;

    private LocalDate dateOfBirth;

    @Size(max = 10, message = "Giới tính không được vượt quá 10 ký tự")
    private String gender;

    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @DecimalMin(value = "0.0", message = "GPA không được nhỏ hơn 0")
    @DecimalMax(value = "4.0", message = "GPA không được lớn hơn 4.0")
    @Digits(integer = 1, fraction = 2, message = "GPA không hợp lệ")
    private BigDecimal gpa;
}
