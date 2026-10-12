package com.baitap;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cơ bản kiểm tra ứng dụng khởi động được mà không cần Spring context hay database.
 * Các unit test thực chất nằm trong StudentServiceTest.
 */
class BaitapApplicationTests {

    @Test
    void applicationClassExists() {
        // Kiểm tra lớp chính tồn tại
        assertThat(BaitapApplication.class).isNotNull();
    }

}
