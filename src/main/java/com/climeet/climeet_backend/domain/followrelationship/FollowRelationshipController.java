package com.climeet.climeet_backend.domain.followrelationship;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="FollowRelationship")
@RequiredArgsConstructor
@RestController
public class FollowRelationshipController {
    private final FollowRelationshipService followRelationshipService;
    private final UserRepository userRepository;
    private final FollowRelationshipRepository followRelationshipRepository;

    @PostMapping("/follow/{userId}")
    @Operation(summary = "유저 팔로우")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> followUser(@RequestParam Long followingUserId, @CurrentUser User currentUser){
        User followingUser = userRepository.findById(followingUserId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
        if(followRelationshipRepository.findByFollowerIdAndFollowingId(currentUser.getId(), followingUserId).isPresent()) {
            throw new GeneralException(ErrorStatus._EXIST_FOLLOW_RELATIONSHIP);
        }
        followRelationshipService.createFollowRelationship(currentUser, followingUser);
        return ResponseEntity.ok("팔로우 완료");
    }

    @DeleteMapping("/unfollow/{userId}")
    @Operation(summary = "유저 팔로우 취소")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> unfollowUser(@RequestParam Long followingUserId, @CurrentUser User currentUser){
        userRepository.findById(followingUserId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
        FollowRelationship followRelationship = followRelationshipRepository.findByFollowerIdAndFollowingId(currentUser.getId(), followingUserId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_FOLLOW_RELATIONSHIP));
        followRelationshipService.deleteFollowRelationship(followRelationship);
        return ResponseEntity.ok("팔로우 취소 완료");
    }


}
