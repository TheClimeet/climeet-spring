package com.climeet.climeet_backend.domain.followrelationship;


import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.fcmNotification.FcmNotificationService;
import com.climeet.climeet_backend.domain.fcmNotification.NotificationType;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowRelationshipService {

    private final FollowRelationshipRepository followRelationshipRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final FcmNotificationService fcmNotificationService;

    public User findByUserId(Long userId){
        return userRepository.findById(userId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
    }

    @Transactional
    public void createFollowRelationship(User follower, User following) {
        if(followRelationshipRepository.findByFollowerIdAndFollowingId(follower.getId(),
            following.getId()).isPresent()){
            throw new GeneralException(ErrorStatus._EXIST_FOLLOW_RELATIONSHIP);
        }
        if(following instanceof Manager){
            Manager manager = (Manager)following;
            manager.getClimbingGym().thisWeekFollowCountUp();
        }
        FollowRelationship followRelationship = FollowRelationship.toEntity(follower, following);
        followRelationshipRepository.save(followRelationship);
        follower.increaseFollowingCount();
        following.increaseFollowerCount();
        //fcmNotificationService.sendSingleUser(following.getId(), NotificationType.NEW_FOLLOWER.getTitle(follower.getProfileName()), NotificationType.NEW_FOLLOWER.getMessage(follower.getProfileName()) );

    }

    @Transactional
    public void deleteFollowRelationship(User following, User follower){
        FollowRelationship followRelationship = followRelationshipRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId())
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_FOLLOW_RELATIONSHIP));
        Optional<Manager> manager = managerRepository.findById(following.getId());
        manager.ifPresent(value -> value.getClimbingGym().thisWeekFollowCountDown());
        followRelationshipRepository.deleteById(followRelationship.getId());
        followRelationship.getFollower().decreaseFollowingCount();
        followRelationship.getFollowing().decreaseFollowerCount();
    }

    public User findManagerByGymID(Long gymId){
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));
        return climbingGym.getManager();
    }
}
