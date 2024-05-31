package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberDetailInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberPrivacySettingInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Climber", description = "클라이머 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/climber")
@CrossOrigin
public class ClimberController {
    private final ClimberService climberService;

    @PostMapping("/login")
    @Operation(summary = "OAuth 2.0 소셜 로그인", description = "**Enum 설명**\n\n**ClimbingLevel** : BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT\n\n**DiscoveryChannel** : INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER\n\n**SocialType(provider에 입력)**: KAKAO, NAVER \n\n **로그인 시 climberRequestDto 빈 값으로 보내기, 회원가입 시에만 채워서 보내기!**")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT})
    public ResponseEntity<ClimberSimpleInfo> login(@RequestParam String provider, @RequestHeader("Authorization") String accessToken, @RequestBody(required = false)
    CreateClimberRequest climberRequestDto) throws FirebaseMessagingException {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        }
        ClimberSimpleInfo climberSimpleResponseDto = climberService.handleSocialLogin(provider, accessToken, climberRequestDto);

        return ResponseEntity.ok(climberSimpleResponseDto);

    }


    @GetMapping("/check-nickname/{nickName}")
    @Operation(summary = "클라이머 닉네임 중복 확인", description = "**이미 존재하는 닉네임** : false \n\n **사용 가능한 닉네임** : true")
    public ResponseEntity<Boolean> checkNickName(@PathVariable String nickName){
        boolean isDuplicated = !climberService.checkNicknameDuplication(nickName);
        return ResponseEntity.ok(isDuplicated);

    }

    @GetMapping("/search")
    @Operation(summary = "클라이머 검색 기능")
    public ResponseEntity<PageResponseDto<List<ClimberDetailInfo>>> getClimberSearchingList(@CurrentUser User currentUser, @RequestParam int page, @RequestParam int size, @RequestParam String climberName){
        return ResponseEntity.ok(climberService.searchClimber(currentUser, climberName, page, size));
    }

    @GetMapping("/privacy-setting")
    @Operation(summary = "클라이머 정보 공개 여부 조회(쇼츠, 홈짐, 평균완등률, 평균레벨)")
    public ResponseEntity<ClimberPrivacySettingInfo> getClimberPrivacyStatus(@CurrentUser User currentUser, @RequestParam long climberId){
        return ResponseEntity.ok(climberService.getClimberPrivacySetting(climberId));

    }

    @PatchMapping("/shorts-privacy-setting")
    @Operation(summary = "클라이머 쇼츠 공개 여부 변경")
    public ResponseEntity<String> updateShortsPrivacyStatus(@CurrentUser User user){
        climberService.updateShortsPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/homegym-privacy-setting")
    @Operation(summary = "클라이머 홈짐 공개 여부 변경")
    public ResponseEntity<String> updateHomeGymPrivacyStatus(@CurrentUser User user){
        climberService.updateHomeGymPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/averageCompletionRate-privacy-setting")
    @Operation(summary = "클라이머 평균완등률 공개 여부 변경")
    public ResponseEntity<String> updateAverageCompletionRatePrivacyStatus(@CurrentUser User user){
        climberService.updateAverageCompletionRatePrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/averageCompletionLevel-privacy-setting")
    @Operation(summary = "클라이머 평균완등레벨 공개 여부 변경")
    public ResponseEntity<String> updateAverageCompletionLevelPrivacyStatus(@CurrentUser User user){
        climberService.updateAverageCompletionLevelPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }





}
