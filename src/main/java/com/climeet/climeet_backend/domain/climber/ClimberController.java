package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberSignUpRequestDto;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/climber")
@CrossOrigin
public class ClimberController {
    private final ClimberService climberService;

  @GetMapping("/login/oauth/{provider}/{accessToken}")
  public ResponseEntity<ClimberResponseDto> login(@PathVariable String provider, @PathVariable String accessToken){
      ClimberResponseDto climberResponseDto = climberService.login(provider, accessToken);
      return ResponseEntity.ok(climberResponseDto);
  }
  @PostMapping("/signup/oauth/{provider}/{accessToken}")
    public ResponseEntity<ClimberResponseDto> signUp(@PathVariable String provider, @PathVariable String accessToken, @RequestBody ClimberSignUpRequestDto climberSignUpRequestDto){
      ClimberResponseDto climberResponseDto = climberService.signUp(provider, accessToken, climberSignUpRequestDto);
      return ResponseEntity.ok(climberResponseDto);
  }
}
