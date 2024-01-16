package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Climber Auth", description = "클라이머 인증 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("/climber")
@CrossOrigin
public class ClimberController {
    private final ClimberService climberService;

    /**
     * OAuth2.0 로그인 API
     */
    @Operation(summary = "로그인", description = "클라이머 소셜로그인")
    @GetMapping("/login/{provider}/{accessToken}")
    public ResponseEntity<ClimberResponseDto> login(@PathVariable String provider, @PathVariable String accessToken){
        ClimberResponseDto climberResponseDto = climberService.login(provider, accessToken);
         return ResponseEntity.ok(climberResponseDto);
    }

    /**
     * OAuth2.0 회원가입 API
     */
    @PostMapping("/signup/{provider}")
    @Operation(summary = "회원가입", description = "클라이머 OAuth 회원가입\n\n**Enum 설명**\n\n**ClimbingLevel** : BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT\n\n**DiscoveryChannel** : INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER\n\n**SocialType**: KAKAO, NAVER")
    public ResponseEntity<ClimberResponseDto> signUp(@PathVariable String provider, @RequestHeader("Authorization") String accessToken, @RequestBody ClimberRequestDto climberRequestDto){
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        } else {
            throw new IllegalArgumentException("잘못된 토큰 형식입니다.");
        }
          ClimberResponseDto climberResponseDto = climberService.signUp(provider, accessToken,
              climberRequestDto);
        return ResponseEntity.ok(climberResponseDto);
    }

    /**
     * 만료된 refresh TOKEN 재발급 API
     */
//    @Operation(summary = "", description = "클라이머 소셜로그인")
//    @GetMapping("/login/{provider}/{accessToken}")
//    public ResponseEntity<ClimberResponseDto> login(@PathVariable String provider, @PathVariable String accessToken){
//        ClimberResponseDto climberResponseDto = climberService.login(provider, accessToken);
//        return ResponseEntity.ok(climberResponseDto);
//    }

}
