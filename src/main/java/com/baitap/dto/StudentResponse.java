package com.baitap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long id;
    private String studentCode;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private BigDecimal gpa;
}
