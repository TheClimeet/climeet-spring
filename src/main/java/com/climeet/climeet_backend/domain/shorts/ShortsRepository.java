package com.climeet.climeet_backend.domain.shorts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    Slice<Shorts> findAllByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT s FROM Shorts s WHERE s.ranking <> 0 AND s.isPublic = true ORDER BY s.ranking ASC, s.createdAt DESC")
    Slice<Shorts> findAllByIsPublicTrueANDByRankingNotZeroOrderByRankingAscCreatedAtDesc(Pageable pageable);

}