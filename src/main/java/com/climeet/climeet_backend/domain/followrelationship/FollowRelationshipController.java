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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="FollowRelationship")
@RequiredArgsConstructor
@RestController
public class FollowRelationshipController {
    private final FollowRelationshipService followRelationshipService;
    private final UserRepository userRepository;

    @PostMapping("/follow/{userId}")
    @Operation(summary = "유저 팔로우 API")
    @SwaggerApiError({ErrorStatus._EMPTY_USER})
    public ResponseEntity<String> followUser(@RequestParam Long followerUserID, @CurrentUser User currentUser){
        User followerUser = userRepository.findById(followerUserID)
                .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
        followRelationshipService.createFollowRelationship(currentUser, followerUser);
        return ResponseEntity.ok("팔로우 완료");
    }


}
