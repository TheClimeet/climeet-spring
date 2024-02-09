package com.climeet.climeet_backend.domain.shorts;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Slice<Shorts> findAllByShortsVisibilityInOrderByCreatedAtDesc(List<ShortsVisibility> shortsVisibility, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "WHERE s.ranking <> 0 "
        + "AND s.shortsVisibility = 'PUBLIC' "
        + "ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByShortsVisibilityPublicANDByRankingNotZeroOrderByRankingAscCreatedAtDesc(Pageable pageable);

}