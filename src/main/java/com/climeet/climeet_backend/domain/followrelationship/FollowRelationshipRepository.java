package com.climeet.climeet_backend.domain.followrelationship;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {

    Optional<FollowRelationship> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteById(Long followRelationshipId);
}
