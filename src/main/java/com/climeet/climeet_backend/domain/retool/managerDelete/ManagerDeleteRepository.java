package com.climeet.climeet_backend.domain.retool.managerDelete;

import com.climeet.climeet_backend.domain.manager.Manager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerDeleteRepository extends JpaRepository<ManagerDelete, Object> {

    Optional<ManagerDelete> findByManager(Manager manager);

}
