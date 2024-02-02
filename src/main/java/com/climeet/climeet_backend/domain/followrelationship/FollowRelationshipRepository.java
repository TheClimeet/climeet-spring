package com.climeet.climeet_backend.domain.followrelationship;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    List<FollowRelationship> findByFollowerId(Long followerId);

}
