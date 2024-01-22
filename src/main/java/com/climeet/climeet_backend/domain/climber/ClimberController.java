package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Climber Auth", description = "클라이머 인증 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("/climber")
@CrossOrigin
public class ClimberController {
    private final ClimberService climberService;

    @PostMapping("/login")
    @Operation(summary = "OAuth 2.0 소셜 로그인", description = "**Enum 설명**\n\n**ClimbingLevel** : BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT\n\n**DiscoveryChannel** : INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER\n\n**SocialType(provider에 입력)**: KAKAO, NAVER \n\n **로그인 시 climberRequestDto 빈 값으로 보내기, 회원가입 시에만 채워서 보내기!**")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT})
    public ResponseEntity<ClimberResponseDto> login(@RequestParam String provider, @RequestHeader("Authorization") String accessToken, @RequestBody(required = false)
    CreateClimberRequest climberRequestDto){
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        }
        climberService.isTokenExpired(accessToken, provider);
        ClimberResponseDto climberResponseDto = climberService.handleSocialLogin(provider, accessToken, climberRequestDto);

        return ResponseEntity.ok(climberResponseDto);

    }

    @PostMapping("/refreshToken")
    @Operation(summary = "access token 만료 시 재발급(Kakao, Naver Resource Server에서 발급 받아오는 Social Access Token)")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST})
    public ResponseEntity<String> refreshToken(@RequestParam String provider, @RequestBody String refreshToken){
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring("Bearer ".length());
        }
        String accessToken = null;
        if(provider.equals("KAKAO")){
            accessToken = climberService.refreshKakaoToken(refreshToken);
        }
        if(provider.equals("NAVER")){
            accessToken = climberService.refreshNaverToken(refreshToken);
        }
        if(accessToken==null){
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return ResponseEntity.ok(accessToken);



    }





}
