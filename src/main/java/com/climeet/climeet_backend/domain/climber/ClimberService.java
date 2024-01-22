package com.climeet.climeet_backend.domain.climber;


import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberTokenRefreshResponse;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipService;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserService;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-dev.yml")
public class ClimberService {

    private final ClimberRepository climberRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ManagerRepository managerRepository;
    private final FollowRelationshipService followRelationshipService;
    private final UserService userService;

    @Transactional
    public ClimberResponseDto handleSocialLogin(String socialType, String accessToken,
        @RequestBody CreateClimberRequest climberRequestDto) {
        HashMap<String, String> userInfo;
        userInfo = getClimberProfileByToken(socialType, accessToken);
        String socialId = userInfo.get("socialId");
        String profileImg = userInfo.get("profileImg");
        Optional<Climber> optionalClimber = climberRepository.findBySocialIdAndSocialType(socialId,
            SocialType.valueOf(socialType));
        Climber resultClimber = null;
        //signUp
        if (optionalClimber.isEmpty()) {
            resultClimber = signUp(socialType, climberRequestDto, socialId, profileImg);
        }
        //login
        if (optionalClimber.isPresent()) {
            resultClimber = login(optionalClimber.get());
        }
        if (resultClimber == null) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return new ClimberResponseDto(resultClimber);


    }

    @Transactional
    public Climber login(Climber climber) {
        try {
            String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(climber.getId()));
            String refreshToken = jwtTokenProvider.createRefreshToken();

            climber.updateToken(accessToken, refreshToken);
            return climber;

        } catch (GeneralException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }


    @Transactional
    public Climber signUp(String socialType, @RequestBody CreateClimberRequest climberRequestDto,
        String socialId, String profileImg) {


        if (climberRequestDto == null) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        Climber climber = Climber.builder()
            .socialId(socialId)
            .socialType(SocialType.valueOf(socialType))
            .profileImageUrl(profileImg).build();
        climberRepository.save(climber);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(climber.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        //추가적으로 사진 url을 입력받으면 입력 받은 url로 변경, null이면 소셜 프로필 사진으로 유지
        if (!Objects.equals(climberRequestDto.getProfileImgUrl(), "")) {
            climber.updateProfileImageUrl(climberRequestDto.getProfileImgUrl());
        }

        userService.updateNotification(climber, climberRequestDto.getIsAllowFollowNotification(), climberRequestDto.getIsAllowLikeNotification(), climberRequestDto.getIsAllowCommentNotification(), climberRequestDto.getIsAllowAdNotification());
        updateClimber(climber, accessToken, refreshToken, climberRequestDto);


        //가입 시 암장 팔로우
        List<String> gymFollowList = climberRequestDto.getGymFollowList();
        for(String gymName : gymFollowList){
            ClimbingGym optionalGym = climbingGymRepository.findByName(gymName)
                .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

            Manager manager = managerRepository.findByClimbingGym(optionalGym)
                .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM));
            followRelationshipService.createFollowRelationship(manager, climber);

        }

        Climber savedClimber = climberRepository.save(climber);

        return savedClimber;
    }

    public void updateClimber(Climber climber, String accessToken, String refreshToken,
        CreateClimberRequest climberRequestDto) {
        climber.updateToken(accessToken, refreshToken);
        climber.updateProfileName(climberRequestDto.getNickName());
        climber.updateClimbingLevel(climberRequestDto.getClimbingLevel());
        climber.updateDiscoveryChannel(climberRequestDto.getDiscoveryChannel());
        if (!Objects.equals(climberRequestDto.getProfileImgUrl(), "")) {
            climber.updateProfileImageUrl(climberRequestDto.getProfileImgUrl());
        }
    }


    private Map<String, Object> getClimberKaKaoAttributesByToken(String accessToken) {
        return WebClient.create()
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers(httpHeaders -> httpHeaders.setBearerAuth(String.valueOf(accessToken)))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
    }

    private Map<String, Object> getClimberNaverAttributesByToken(String accessToken) {
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

    }

    public Map<String ,Object> isTokenExpired(String accessToken, String provider){
        if(!provider.equals("KAKAO") && !provider.equals("NAVER")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        if(provider.equals("KAKAO")){
            return isKakaoTokenExpired(accessToken);

        }
            return isNaverTokenExpired(accessToken);

    }

    public Map<String, Object> isKakaoTokenExpired(String accessToken){
        return WebClient.create()
            .get()
            .uri("https://kapi.kakao.com/v1/user/access_token_info")
            .headers(httpHeaders -> httpHeaders.setBearerAuth(String.valueOf(accessToken)))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                Mono.error(new GeneralException(ErrorStatus._EXPIRED_JWT)))
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();

    }

    public Map<String, Object> isNaverTokenExpired(String accessToken){
        return WebClient.create()
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
            .headers(httpHeaders -> httpHeaders.setBearerAuth(String.valueOf(accessToken)))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                Mono.error(new GeneralException(ErrorStatus._EXPIRED_JWT)))
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();

    }


    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
    public String  refreshKakaoToken(String refreshToken){
        WebClient webClient = WebClient.builder().build();

        Mono<ClimberTokenRefreshResponse> mono = webClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
            .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                .with("client_id", clientId )
                .with("refresh_token", refreshToken)
                .with("client_secret", clientSecret))
            .retrieve()
            .bodyToMono(ClimberTokenRefreshResponse.class);

        ClimberTokenRefreshResponse response = mono.block();

        return response!=null ? response.getAccessToken() : null;

    }



}
