package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberSignUpRequestDto;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
public class ClimberService {

    private static final String BEARER_TYPE = "Bearer";
    private final ClimberRepository climberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ClimberResponseDto login(String socialType, String userToken) {
        try {
            Climber climber = getClimberProfileByToken(socialType, userToken);
            Long socialId = climber.getSocialId();

            Climber resultClimber = climberRepository.findBySocialId(socialId);

            if(resultClimber==null) {
                throw new GeneralException(ErrorStatus._EMPTY_MEMBER);
            } else {
                String accessToken = resultClimber.getAccessToken();
                return new ClimberResponseDto(resultClimber);
                //서버 accessToken이 유효한지 확인
//                if(jwtTokenProvider.validateToken(accessToken)){
//                    // DB에 존재하는 Climber를 반환합니다.
//                    System.out.println("여기인가?2");
//                    return new ClimberResponseDto(resultClimber);
//                } else{
//                    System.out.println("여기인가?3");
//                    throw new GeneralException(ErrorStatus._EXPIRED_JWT);
//                }

            }

        } catch (GeneralException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }




    @Transactional
    public ClimberResponseDto signUp(String socialType, String userToken, @RequestBody ClimberSignUpRequestDto climberSignUpRequestDto){
        Climber climber = getClimberProfileByToken(socialType, userToken);
        assert climber != null;
        Long socialId = climber.getSocialId();
        System.out.println(socialId);
        Climber resultClimber = climberRepository.findBySocialId(socialId);
        if(resultClimber!=null) {
            throw new GeneralException(ErrorStatus._DUPLICATE_SIGN_IN);
        }
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(climber.getSocialId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        climber.setToken(accessToken, refreshToken);
        climber.setNickName(climberSignUpRequestDto.getNickName());
        climber.setClimbingLevel(climberSignUpRequestDto.getClimbingLevel());
        climber.setDiscoveryChannel(climberSignUpRequestDto.getDiscoveryChannel());
        climber.setSocialType(SocialType.valueOf(socialType));

        Climber savedClimber = climberRepository.save(climber);

        return new ClimberResponseDto(savedClimber);
    }


    private Map<String, Object> getClimberAttributesByToken(String accessToken){
        return WebClient.create()
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers(httpHeaders -> httpHeaders.setBearerAuth(String.valueOf(accessToken)))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
    }

    private Climber getClimberProfileByToken(String providerName, String userToken)
        throws RuntimeException {
        if (!providerName.equals("KAKAO") && !providerName.equals("NAVER")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        Map<String, Object> userAttributesByToken = getClimberAttributesByToken(userToken);
        if (providerName.equals("KAKAO")) {
            KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userAttributesByToken);
            Long social_id = kaKaoUserInfo.getID();
            String profile_img = kaKaoUserInfo.getProfileImg();
            //System.out.println(social_id + " " + profile_img);
            //로그인 case
            if (climberRepository.findById(social_id).isPresent()) {
                return climberRepository.findBySocialId(social_id);
            }else{  //회원가입 case
                return Climber.builder()
                    .socialId(social_id)
                    .profileImageUrl(profile_img).build();
            }
        } else if (providerName.equals("NAVER")) {
            //네이버 로직 구현 후 추가 예정
            return null;
        } else{
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }
    @Transactional
    public void updateClimberInfo(Long id, ClimberSignUpRequestDto requestDto){
        Climber climber = climberRepository.findById(id)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_MEMBER));
        climber.update(requestDto.getNickName(), requestDto.getClimbingLevel(), requestDto.getDiscoveryChannel());
    }



}
