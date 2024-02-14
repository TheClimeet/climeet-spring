package com.climeet.climeet_backend.domain.shorts;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Slice<Shorts> findAllByShortsVisibilityInOrderByCreatedAtDesc(List<ShortsVisibility> shortsVisibilities, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "WHERE s.ranking <> 0 "
        + "AND s.shortsVisibility = 'PUBLIC' "
        + "ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByShortsVisibilityPublicANDByRankingNotZeroOrderByRankingAscCreatedAtDesc(Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "WHERE s.ranking <> 0 "
        + "AND s.climbingGym.id = :gymId "
        + "AND s.shortsVisibility = 'PUBLIC' "
        + "ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByShortsVisibilityPublicANDByRankingNotZeroAndClimbingGymIdOrderByRankingAscCreatedAtDesc(@Param("gymId") Long gymId, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "WHERE s.ranking <> 0 "
        + "AND s.sector.id = :sectorId "
        + "AND s.shortsVisibility = 'PUBLIC' "
        + "ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByShortsVisibilityPublicANDByRankingNotZeroAndSectorIdOrderByRankingAscCreatedAtDesc(@Param("sectorId") Long sectorId, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "WHERE s.ranking <> 0 "
        + "AND s.route.id = :routeId "
        + "AND s.shortsVisibility = 'PUBLIC' "
        + "ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByShortsVisibilityPublicANDByRankingNotZeroAndRouteIdOrderByRankingAscCreatedAtDesc(@Param("routeId") Long routeId, Pageable pageable);

    Slice<Shorts> findAllByShortsVisibilityInAndClimbingGymIdOrderByCreatedAtDesc(List<ShortsVisibility> shortsVisibilities, Long climbingGymId, Pageable pageable);

    Slice<Shorts> findAllByShortsVisibilityInAndSectorIdOrderByCreatedAtDesc(List<ShortsVisibility> shortsVisibilities, Long gymId, Pageable pageable);

    Slice<Shorts> findAllByShortsVisibilityInAndRouteIdOrderByCreatedAtDesc(List<ShortsVisibility> shortsVisibilities, Long routeId, Pageable pageable);


    List<Shorts> findByCreatedAtBefore(LocalDateTime dateTime);

}