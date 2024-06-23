package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.ClimberTokenRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberDetailInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberPrivacySettingInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.LoginSimpleInfo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="1300 - Climber", description = "클라이머 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/climber")
@CrossOrigin
public class ClimberController {
    private final ClimberService climberService;

    @PostMapping("/login")
    @Operation(summary = "OAuth 2.0 소셜 로그인 - 1301 [미리]", description = "**Enum 설명** : SocialType(provider에 입력)**: KAKAO, NAVER, APPLE \n\n responseType : SIGN_IN - 로그인 성공, SIGN_UP : 회원가입 필요, accessToken 필드의 토큰을 추가정보 입력 api에 넣어서 회원가입을 진행합니다.")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT})
    public ResponseEntity<LoginSimpleInfo> login(@RequestParam String provider, @RequestBody
        ClimberTokenRequest climberTokenRequest) {
        LoginSimpleInfo climberSimpleResponseDto = climberService.login(provider, climberTokenRequest);

        return ResponseEntity.ok(climberSimpleResponseDto);

    }

    @PostMapping("/signup/extra")
    @Operation(summary = "회원가입 추가 정보 입력 api - 1302 [미리]", description = "\n\n**ClimbingLevel** : BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT\n\n**DiscoveryChannel** : INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER**")
    public ResponseEntity<LoginSimpleInfo> signUp(@RequestBody CreateClimberRequest createClimberRequest) {
        return ResponseEntity.ok(climberService.signUp(createClimberRequest));
    }

    @GetMapping("/check-nickname/{nickName}")
    @Operation(summary = "클라이머 닉네임 중복 확인 - 1303 [미리]", description = "**이미 존재하는 닉네임** : false \n\n **사용 가능한 닉네임** : true")
    public ResponseEntity<Boolean> checkNickName(@PathVariable String nickName){
        boolean isDuplicated = !climberService.checkNicknameDuplication(nickName);
        return ResponseEntity.ok(isDuplicated);

    }

    @GetMapping("/search")
    @Operation(summary = "클라이머 검색 기능 - 1304 [미리]")
    public ResponseEntity<PageResponseDto<List<ClimberDetailInfo>>> getClimberSearchingList(@CurrentUser User currentUser, @RequestParam int page, @RequestParam int size, @RequestParam String climberName){
        return ResponseEntity.ok(climberService.searchClimber(currentUser, climberName, page, size));
    }

    @GetMapping("/privacy-setting")
    @Operation(summary = "클라이머 정보 공개 여부 조회(쇼츠, 홈짐, 평균완등률, 평균레벨) - 1305 [미리]")
    public ResponseEntity<ClimberPrivacySettingInfo> getClimberPrivacyStatus(@CurrentUser User currentUser, @RequestParam long climberId){
        return ResponseEntity.ok(climberService.getClimberPrivacySetting(climberId));

    }

    @PatchMapping("/shorts-privacy-setting")
    @Operation(summary = "클라이머 쇼츠 공개 여부 변경 - 1306 [미리]")
    public ResponseEntity<String> updateShortsPrivacyStatus(@CurrentUser User user){
        climberService.updateShortsPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/homegym-privacy-setting")
    @Operation(summary = "클라이머 홈짐 공개 여부 변경 - 1307 [미리]")
    public ResponseEntity<String> updateHomeGymPrivacyStatus(@CurrentUser User user){
        climberService.updateHomeGymPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/averageCompletionRate-privacy-setting")
    @Operation(summary = "클라이머 평균완등률 공개 여부 변경 - 1308 [미리]")
    public ResponseEntity<String> updateAverageCompletionRatePrivacyStatus(@CurrentUser User user){
        climberService.updateAverageCompletionRatePrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }

    @PatchMapping("/averageCompletionLevel-privacy-setting")
    @Operation(summary = "클라이머 평균완등레벨 공개 여부 변경 - 1309 [미리]")
    public ResponseEntity<String> updateAverageCompletionLevelPrivacyStatus(@CurrentUser User user){
        climberService.updateAverageCompletionLevelPrivacySetting(user);
        return ResponseEntity.ok("공개 여부 변경 완료");
    }





}
