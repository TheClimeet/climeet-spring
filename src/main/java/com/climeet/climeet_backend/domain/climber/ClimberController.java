package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberSignUpRequestDto;
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
    @PostMapping("/signup/{provider}/{accessToken}")
    @Operation(summary = "회원가입", description = "클라이머 OAuth 회원가입")
    public ResponseEntity<ClimberResponseDto> signUp(@PathVariable String provider, @PathVariable String accessToken, @RequestBody ClimberSignUpRequestDto climberSignUpRequestDto){
          ClimberResponseDto climberResponseDto = climberService.signUp(provider, accessToken, climberSignUpRequestDto);
        return ResponseEntity.ok(climberResponseDto);
    }

    /**
     * 만료된 refresh TOKEN 재발급 API
     */
//    @Operation(summary = "로", description = "클라이머 소셜로그인")
//    @GetMapping("/login/{provider}/{accessToken}")
//    public ResponseEntity<ClimberResponseDto> login(@PathVariable String provider, @PathVariable String accessToken){
//        ClimberResponseDto climberResponseDto = climberService.login(provider, accessToken);
//        return ResponseEntity.ok(climberResponseDto);
//    }

}
