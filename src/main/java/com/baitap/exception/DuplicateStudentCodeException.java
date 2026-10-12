package com.baitap.exception;

public class DuplicateStudentCodeException extends RuntimeException {

    public DuplicateStudentCodeException(String studentCode) {
        super("Mã sinh viên '" + studentCode + "' đã tồn tại trong hệ thống");
    }
}
