package com.climeet.climeet_backend.domain.followrelationship;


import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowRelationshipService {

    private final FollowRelationshipRepository followRelationshipRepository;
    private final UserRepository userRepository;

    public User findByUserId(Long userId){
        return userRepository.findById(userId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
    }
    public void createFollowRelationship(User follower, User following) {
        if(followRelationshipRepository.findByFollowerIdAndFollowingId(follower.getId(),
            following.getId()).isPresent()){
            throw new GeneralException(ErrorStatus._EXIST_FOLLOW_RELATIONSHIP);
        }
        FollowRelationship followRelationship = FollowRelationship.toEntity(follower, following);
        followRelationshipRepository.save(followRelationship);
        follower.increaseFollwerCount();
    }

    public void deleteFollowRelationship(User following, User follower){
        FollowRelationship followRelationship = followRelationshipRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId())
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_FOLLOW_RELATIONSHIP));
        followRelationshipRepository.deleteById(followRelationship.getId());
        followRelationship.getFollower().decreaseFollwerCount();
    }
}
