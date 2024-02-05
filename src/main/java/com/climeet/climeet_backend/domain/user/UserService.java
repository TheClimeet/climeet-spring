package com.climeet.climeet_backend.domain.user;

import static java.util.stream.Collectors.toList;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationship;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FollowRelationshipRepository followRelationshipRepository;

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

    public List<UserFollowDetailInfo> getFollowing(Long targetUserId, User currentUser, String userCategory){
        userRepository.findById(targetUserId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
        List<FollowRelationship> targetUserFollowerList = followRelationshipRepository.findByFollowerId(targetUserId);
        if(!userCategory.equals("Climber")&& !userCategory.equals("Manager")){
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        List<UserFollowDetailInfo> userFollowDetailResponseList = null;
        if(userCategory.equals("Climber")) {
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
        if(userCategory.equals("Manager")){
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




}
