package com.baitap.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Không tìm thấy sinh viên với ID: " + id);
    }
}
