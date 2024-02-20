package com.climeet.climeet_backend.domain.followrelationship;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
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

    @PostMapping("/follow-relationship")
    @Operation(summary = "유저 팔로우")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> followUser(@RequestParam Long followingUserId, @CurrentUser User currentUser){
        User following = followRelationshipService.findByUserId(followingUserId);
        followRelationshipService.createFollowRelationship(currentUser, following);
        return ResponseEntity.ok("팔로우 완료");
    }

    @DeleteMapping("/follow-relationship")
    @Operation(summary = "유저 팔로우 취소")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> unfollowUser(@RequestParam Long followingUserId, @CurrentUser User currentUser){
        User following = followRelationshipService.findByUserId(followingUserId);
        followRelationshipService.deleteFollowRelationship(following, currentUser);
        return ResponseEntity.ok("팔로우 취소 완료");
    }

    @PostMapping("/follow-relationship/gym")
    @Operation(summary = "암장 팔로우")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> followGym(@RequestParam Long gymId, @CurrentUser User currentUser){
        User following = followRelationshipService.findManagerByGymID(gymId);
        followRelationshipService.createFollowRelationship(currentUser, following);
        return ResponseEntity.ok("팔로우 완료");
    }

    @DeleteMapping("/follow-relationship/gym")
    @Operation(summary = "암장 팔로우 취소")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._EXIST_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> unfollowGym(@RequestParam Long gymId, @CurrentUser User currentUser){
        User following = followRelationshipService.findManagerByGymID(gymId);
        followRelationshipService.deleteFollowRelationship(following, currentUser);
        return ResponseEntity.ok("팔로우 취소 완료");
    }


}
