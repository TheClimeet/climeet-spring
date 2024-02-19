package com.climeet.climeet_backend.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByprofileName(String name);
    Optional<User> findById(Long userId);

}