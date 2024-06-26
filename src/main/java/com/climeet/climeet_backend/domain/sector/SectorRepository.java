package com.climeet.climeet_backend.domain.sector;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findSectorByClimbingGymId(Long gymId);

    List<Sector> findByIdIn(List<Long> sectorIdList);
}