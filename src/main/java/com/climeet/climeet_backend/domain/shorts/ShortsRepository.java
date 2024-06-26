package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

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

    Slice<Shorts> findByUserAndShortsVisibilityInOrderByCreatedAtDesc(User uploader, List<ShortsVisibility> publicAndFollowersOnlyList, Pageable pageable);

    Slice<Shorts> findByUserAndRankingNotAndShortsVisibilityInOrderByRankingAsc(User uploader, int rankingThreshold, List<ShortsVisibility> shortsVisibilities, Pageable pageable);

    Slice<Shorts> findByUserAndShortsVisibilityOrderByCreatedAtDesc(User uploader, ShortsVisibility shortsVisibility, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "JOIN ShortsLike sl "
        + "ON sl.shorts.id = s.id "
        + "WHERE sl.user.id = :userId "
        + "AND sl.isLike = true "
        + "AND s.shortsVisibility = 'PUBLIC'"
        + "ORDER BY sl.createdAt DESC")
    Slice<Shorts> findLikedShortsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT s "
        + "FROM Shorts s "
        + "JOIN ShortsBookmark sb "
        + "ON sb.shorts.id = s.id "
        + "WHERE sb.user.id = :userId "
        + "AND sb.isBookmarked = true "
        + "AND s.shortsVisibility = 'PUBLIC'"
        + "ORDER BY sb.createdAt DESC")
    Slice<Shorts> findBookmarkedShortsByUserId(@Param("userId") Long id, Pageable pageable);
}