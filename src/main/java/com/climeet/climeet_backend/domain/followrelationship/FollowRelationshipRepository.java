package com.climeet.climeet_backend.domain.followrelationship;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    List<FollowRelationship> findByFollowerId(Long followerId);
    List<FollowRelationship> findByFollowingId(Long followingId);

    Optional<FollowRelationship> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteById(Long followRelationshipId);
}
