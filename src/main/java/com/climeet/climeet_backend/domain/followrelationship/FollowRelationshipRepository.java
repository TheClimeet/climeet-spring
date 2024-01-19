package com.climeet.climeet_backend.domain.followrelationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {

}
