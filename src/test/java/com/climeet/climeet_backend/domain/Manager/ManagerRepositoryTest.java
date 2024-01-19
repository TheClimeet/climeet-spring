package com.climeet.climeet_backend.domain.Manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ManagerRepositoryTest {
    @Autowired
    private ManagerRepository managerRepository;

    @Test
    public void findByNameTest() {
        String gymName = "test-gym"; // 테스트를 위한 기존에 존재하는 gymName을 사용하세요.

        Optional<Manager> optionalManager = managerRepository.findByName(gymName);

        assertTrue(optionalManager.isPresent()); // gymName에 해당하는 Manager가 존재하는지 확인
        assertEquals(gymName, optionalManager.get().getGymName()); // 반환된 Manager의 gymName이 올바른지 확인
    }
}


