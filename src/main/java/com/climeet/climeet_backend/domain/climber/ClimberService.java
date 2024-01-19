package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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
            String socialId = climber.getSocialId();

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
    public ClimberResponseDto signUp(String socialType, String userToken, @RequestBody ClimberRequestDto climberRequestDto){
        Climber climber = getClimberProfileByToken(socialType, userToken);
        assert climber != null;
        String socialId = climber.getSocialId();
        //System.out.println(socialId);
        Climber resultClimber = climberRepository.findBySocialId(socialId);
        if(resultClimber!=null) {
            throw new GeneralException(ErrorStatus._DUPLICATE_SIGN_IN);
        }
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(climber.getSocialId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        climber.updateToken(accessToken, refreshToken);
        climber.setNickName(climberRequestDto.getNickName());
        climber.setClimbingLevel(climberRequestDto.getClimbingLevel());
        climber.setDiscoveryChannel(climberRequestDto.getDiscoveryChannel());
        climber.setSocialType(SocialType.valueOf(socialType));
        if(!Objects.equals(climberRequestDto.getProfileImgUrl(), "")){
            climber.setProfileImageUrl(climberRequestDto.getProfileImgUrl());
        }


        Climber savedClimber = climberRepository.save(climber);

        return new ClimberResponseDto(savedClimber);
    }

    private Map<String, Object> getClimberKaKaoAttributesByToken(String accessToken){
        return WebClient.create()
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers(httpHeaders -> httpHeaders.setBearerAuth(String.valueOf(accessToken)))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
    }

    private Map<String, Object> getClimberNaverAttributesByToken(String accessToken){
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalArgumentException("인증 토큰이 없거나 비어있습니다.");
        }
        return WebClient.create()
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
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
        String social_id = null;
        String profile_img = null;
        if (providerName.equals("KAKAO")) {
            Map<String, Object> userAttributesByToken = getClimberKaKaoAttributesByToken(userToken);
            KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userAttributesByToken);
            social_id = Long.toString(kaKaoUserInfo.getID());
            profile_img = kaKaoUserInfo.getProfileImg();

        } else if (providerName.equals("NAVER")) {
            Map<String, Object> userAttributesByToken = getClimberNaverAttributesByToken(userToken);
            NaverUserInfo naverUserInfo = new NaverUserInfo(userAttributesByToken);
            social_id = naverUserInfo.getId();
            profile_img = naverUserInfo.getProfileImg();
        }
        //로그인 case
        if (climberRepository.findBySocialId(social_id)!=null) {
            return climberRepository.findBySocialId(social_id);
        }else{  //회원가입 case
            return Climber.builder()
                .socialId(social_id)
                .profileImageUrl(profile_img).build();
        }
    }
    @Transactional
    public void updateClimberInfo(Long id, ClimberRequestDto requestDto){
        Climber climber = climberRepository.findById(id)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_MEMBER));
        climber.update(requestDto.getNickName(), requestDto.getClimbingLevel(), requestDto.getDiscoveryChannel());
    }



}
