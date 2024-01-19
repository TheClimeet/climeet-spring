package com.climeet.climeet_backend.domain.climber;


import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
public class ClimberService {

    private final ClimberRepository climberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ClimberResponseDto handleSocialLogin(String socialType, String accessToken, @RequestBody CreateClimberRequest climberRequestDto){
        HashMap<String, String> userInfo;
        userInfo = getClimberProfileByToken(socialType, accessToken);
        String socialId = userInfo.get("socialId");
        String profileImg = userInfo.get("profileImg");
        Optional<Climber> optionalClimber = climberRepository.findBySocialIdAndSocialType(socialId, SocialType.valueOf(socialType));
        Climber resultClimber = null;
        //signUp
        if(optionalClimber.isEmpty()){
            resultClimber = signUp(socialType, climberRequestDto, socialId, profileImg);
        }
        //login
        if(optionalClimber.isPresent()){
            resultClimber = login(optionalClimber.get());
        }
        if(resultClimber==null){
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return new ClimberResponseDto(resultClimber);


    }

    @Transactional
    public Climber login(Climber climber) {
        try {
            String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(climber.getId()));
            String refreshToken = jwtTokenProvider.createRefreshToken();

            climber.updateToken(accessToken, refreshToken);
            return climber;

        } catch (GeneralException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }


    @Transactional
    public Climber signUp(String socialType, @RequestBody CreateClimberRequest climberRequestDto, String socialId, String profileImg){

        if(climberRequestDto==null){
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        Climber climber = Climber.builder()
            .socialId(socialId)
            .socialType(SocialType.valueOf(socialType))
            .profileImageUrl(profileImg).build();
        climberRepository.save(climber);


        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(climber.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        if(!Objects.equals(climberRequestDto.getProfileImgUrl(), "")){
            climber.setProfileImageUrl(climberRequestDto.getProfileImgUrl());
        }
        updateClimber(climber, accessToken, refreshToken, climberRequestDto);

        Climber savedClimber = climberRepository.save(climber);

        return savedClimber;
    }
    public void updateClimber(Climber climber, String accessToken, String refreshToken, CreateClimberRequest climberRequestDto) {
        climber.updateToken(accessToken, refreshToken);
        climber.updateNickName(climberRequestDto.getNickName());
        climber.updateClimbingLevel(climberRequestDto.getClimbingLevel());
        climber.updateDiscoveryChannel(climberRequestDto.getDiscoveryChannel());
        if(!Objects.equals(climberRequestDto.getProfileImgUrl(), "")){
            climber.updateProfileImageUrl(climberRequestDto.getProfileImgUrl());
        }
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
            throw new GeneralException(ErrorStatus._INVALID_JWT);
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
    HashMap<String, String> getClimberProfileByToken(String providerName, String userToken)
        throws RuntimeException {
        if (!providerName.equals("KAKAO") && !providerName.equals("NAVER")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        String socialId = null;
        String profileImg = null;
        if (providerName.equals("KAKAO")) {
            Map<String, Object> userAttributesByToken = getClimberKaKaoAttributesByToken(userToken);
            KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userAttributesByToken);
            socialId = Long.toString(kaKaoUserInfo.getID());
            profileImg = kaKaoUserInfo.getProfileImg();

        }
        if (providerName.equals("NAVER")) {
            Map<String, Object> userAttributesByToken = getClimberNaverAttributesByToken(userToken);
            NaverUserInfo naverUserInfo = new NaverUserInfo(userAttributesByToken);
            socialId = naverUserInfo.getId();
            profileImg = naverUserInfo.getProfileImg();
        }
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("socialId", socialId);
        userInfo.put("profileImg", profileImg);
        return userInfo;
        //Optional<Climber> optionalClimber = climberRepository.findBySocialIdAndSocialType(socialId, SocialType.valueOf(providerName));
//        if(optionalClimber.isEmpty()){
//            return null;
//        }
        //return optionalClimber.get();
        //로그인 case
//        if (optionalClimber.isPresent()) {
//            return optionalClimber.get();
//        }else{  //회원가입 case
//            return Climber.builder()
//                .socialId(socialId)
//                .socialType(SocialType.valueOf(providerName))
//                .profileImageUrl(profileImg).build();
//        }
    }

//    Climber getClimberProfileByToken(String providerName, String userToken)
//        throws RuntimeException {
//        if (!providerName.equals("KAKAO") && !providerName.equals("NAVER")) {
//            throw new GeneralException(ErrorStatus._BAD_REQUEST);
//        }
//        String socialId = null;
//        String profileImg = null;
//        if (providerName.equals("KAKAO")) {
//            Map<String, Object> userAttributesByToken = getClimberKaKaoAttributesByToken(userToken);
//            KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userAttributesByToken);
//            socialId = Long.toString(kaKaoUserInfo.getID());
//            profileImg = kaKaoUserInfo.getProfileImg();
//
//        }
//        if (providerName.equals("NAVER")) {
//            Map<String, Object> userAttributesByToken = getClimberNaverAttributesByToken(userToken);
//            NaverUserInfo naverUserInfo = new NaverUserInfo(userAttributesByToken);
//            socialId = naverUserInfo.getId();
//            profileImg = naverUserInfo.getProfileImg();
//        }
//        Optional<Climber> optionalClimber = climberRepository.findBySocialIdAndSocialType(socialId, SocialType.valueOf(providerName));
////        if(optionalClimber.isEmpty()){
////            return null;
////        }
//        //return optionalClimber.get();
//        //로그인 case
////        if (optionalClimber.isPresent()) {
////            return optionalClimber.get();
////        }else{  //회원가입 case
////            return Climber.builder()
////                .socialId(socialId)
////                .socialType(SocialType.valueOf(providerName))
////                .profileImageUrl(profileImg).build();
////        }
//    }


}
