package com.climeet.climeet_backend.domain.user;


import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationship;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.dto.UserRequestDto.UpdateUserAllowNotificationRequest;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserAccountDetailInfo;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.routeversion.RouteVersionService;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowSimpleInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserHomeGymDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserHomeGymSimpleInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserProfileDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FollowRelationshipRepository followRelationshipRepository;
    private final ClimberRepository climberRepository;
    private final ManagerRepository managerRepository;
    private final RouteVersionService routeVersionService;

    @Transactional
    public User updateNotification(User user, boolean isAllowFollowNotification,
        boolean isAllowLikeNotification, boolean isAllowCommentNotification,
        boolean isAllowAdNotification) {
        if (user == null) {
            throw new GeneralException(ErrorStatus._EMPTY_MEMBER);
        }
        user.updateNotification(isAllowFollowNotification, isAllowLikeNotification,
            isAllowCommentNotification, isAllowAdNotification);
        return userRepository.save(user);

    }

    @Transactional
    public UserTokenSimpleInfo updateUserToken(String refreshToken) {
        Long userId = Long.valueOf(jwtTokenProvider.getPayload(refreshToken));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_JWT));
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        String newAccesstoken = jwtTokenProvider.refreshAccessToken(refreshToken);
        user.updateToken(newRefreshToken, newAccesstoken);

        return new UserTokenSimpleInfo(user);

    }

    public List<UserFollowDetailInfo> getFollower(Long targetUserId, User currentUser,
        String userCategory) {
        userRepository.findById(targetUserId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        List<FollowRelationship> targetUserFollowerList = followRelationshipRepository.findByFollowingId(
            targetUserId);
        if (!userCategory.equals("Climber") && !userCategory.equals("Manager")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        List<UserFollowDetailInfo> userFollowDetailResponseList = null;
        if (userCategory.equals("Climber")) {
            userFollowDetailResponseList = targetUserFollowerList.stream()
                .filter(followRelationship -> followRelationship.getFollower() instanceof Climber)
                .map(followRelationship -> {
                    Boolean currentUserRelation = false;
                    if (followRelationshipRepository.findByFollowerIdAndFollowingId(
                        currentUser.getId(),
                        followRelationship.getFollower().getId()).isPresent()) {
                        currentUserRelation = true;
                    }
                    return UserFollowDetailInfo.toDTO(followRelationship.getFollower(),
                        currentUserRelation);
                }).toList();

        }
        if (userCategory.equals("Manager")) {
            userFollowDetailResponseList = targetUserFollowerList.stream()
                .filter(followRelationship -> followRelationship.getFollower() instanceof Manager)
                .map(followRelationship -> {
                    Boolean currentUserRelation = false;
                    if (followRelationshipRepository.findByFollowerIdAndFollowingId(
                        currentUser.getId(),
                        followRelationship.getFollower().getId()).isPresent()) {
                        currentUserRelation = true;
                    }
                    return UserFollowDetailInfo.toDTO(followRelationship.getFollower(),
                        currentUserRelation);
                }).toList();
        }

        return userFollowDetailResponseList;
    }

    public List<UserFollowDetailInfo> getFollowing(Long targetUserId, User currentUser,
        String userCategory) {
        userRepository.findById(targetUserId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        List<FollowRelationship> targetUserFollowerList = followRelationshipRepository.findByFollowerId(
            targetUserId);
        if (!userCategory.equals("Climber") && !userCategory.equals("Manager")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        List<UserFollowDetailInfo> userFollowDetailResponseList = null;
        if (userCategory.equals("Climber")) {
            userFollowDetailResponseList = targetUserFollowerList.stream()
                .filter(followRelationship -> followRelationship.getFollowing() instanceof Climber)
                .map(followRelationship -> {
                    Boolean currentUserRelation = false;
                    if (followRelationshipRepository.findByFollowerIdAndFollowingId(
                        currentUser.getId(),
                        followRelationship.getFollowing().getId()).isPresent()) {
                        currentUserRelation = true;
                    }
                    return UserFollowDetailInfo.toDTO(followRelationship.getFollowing(),
                        currentUserRelation);
                }).toList();

        }
        if (userCategory.equals("Manager")) {
            userFollowDetailResponseList = targetUserFollowerList.stream()
                .filter(followRelationship -> followRelationship.getFollowing() instanceof Manager)
                .map(followRelationship -> {
                    Boolean currentUserRelation = false;
                    if (followRelationshipRepository.findByFollowerIdAndFollowingId(
                        currentUser.getId(),
                        followRelationship.getFollowing().getId()).isPresent()) {
                        currentUserRelation = true;
                    }
                    return UserFollowDetailInfo.toDTO(followRelationship.getFollowing(),
                        currentUserRelation);
                }).toList();
        }

        return userFollowDetailResponseList;
    }

    public List<UserHomeGymSimpleInfo> getHomeGyms(User currentUser) {
        List<FollowRelationship> followRelationships = followRelationshipRepository.findByFollowerId(
            currentUser.getId());

        return followRelationships.stream()
            .filter(followRelationship -> followRelationship.getFollowing() instanceof Manager)
            .map(followRelationship -> {
                ClimbingGym climbingGym = ((Manager) followRelationship.getFollowing()).getClimbingGym();
                return UserHomeGymSimpleInfo.toDTO(climbingGym);
            }).toList();

    }


    public UserAccountDetailInfo getLoginUserProfiles(User currentUser) {
        Optional<Manager> manager = managerRepository.findById(currentUser.getId());
        Optional<Climber> climber = climberRepository.findById(currentUser.getId());
        boolean isManager = true;
        SocialType socialType = null;

        if (manager.isEmpty() && climber.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_USER);
        }
        if (!climber.isEmpty()) {
            isManager = false;
            socialType = climber.get().getSocialType();
        } else {
            isManager = true;
        }

        return UserAccountDetailInfo.toDTO(currentUser, isManager, socialType);
    }



    @Transactional
    public String updateUserNotification(User currentUser,
        UpdateUserAllowNotificationRequest request) {

        boolean isAllowFollowNotification = currentUser.getIsAllowFollowNotification();
        boolean isAllowLikeNotification = currentUser.getIsAllowLikeNotification();
        boolean isAllowCommentNotification = currentUser.getIsAllowCommentNotification();
        boolean isAllowAdNotification = currentUser.getIsAllowAdNotification();


        if (request.getIsAllowFollowNotification() != null) {
            isAllowFollowNotification = request.getIsAllowFollowNotification();
        }
        if (request.getIsAllowLikeNotification() != null) {
            isAllowLikeNotification = request.getIsAllowLikeNotification();
        }
        if (request.getIsAllowCommentNotification() != null) {
            isAllowCommentNotification = request.getIsAllowCommentNotification();
        }
        if (request.getIsAllowAdNotification() != null) {
            isAllowAdNotification = request.getIsAllowAdNotification();
        }

        currentUser.updateNotification(isAllowFollowNotification, isAllowLikeNotification,
            isAllowCommentNotification, isAllowAdNotification);

        return "업데이트 완료";
    }

    public List<UserFollowSimpleInfo> getClimberFollowing(User currentUser){
        List<FollowRelationship> followingClimbers = followRelationshipRepository.findByFollowerId(
            currentUser.getId());

        return followingClimbers.stream()
            .filter(followingClimber -> followingClimber.getFollowing() instanceof Climber)
            .map(followRelationship -> {
                User climber = followRelationship.getFollowing();
                return UserFollowSimpleInfo.toDTO(climber);
            }).toList();
    }

    public List<UserHomeGymDetailInfo> getGymsFollowing(User currentUser){
        List<FollowRelationship> followingClimbers = followRelationshipRepository.findByFollowerId(
            currentUser.getId());

        return followingClimbers.stream()
            .filter(followingClimber -> followingClimber.getFollowing() instanceof Manager)
            .map(followRelationship -> {
                ClimbingGym climbingGym = ((Manager) followRelationship.getFollowing()).getClimbingGym();
                List<Route> gymRouteList = routeVersionService.getRouteVersionRouteList(climbingGym.getId());
                return UserHomeGymDetailInfo.toDTO(climbingGym, gymRouteList);
            }).toList();
    }

    public UserProfileDetailInfo getUserMyPageProfile(User currentUser){
        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));

        boolean status = user instanceof Manager;

        return UserProfileDetailInfo.toDTO(user, status);

    }

}
