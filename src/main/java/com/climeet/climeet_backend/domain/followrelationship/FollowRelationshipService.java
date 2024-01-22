package com.climeet.climeet_backend.domain.followrelationship;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.manager.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowRelationshipService {

    private final FollowRelationshipRepository followRelationshipRepository;
    public FollowRelationship createFollowRelationship(Manager follower, Climber followee) {
        FollowRelationship followRelationship = FollowRelationship.toEntity(follower, followee);
        return followRelationshipRepository.save(followRelationship);
    }

}
