package com.climeet.climeet_backend.domain.climber;


import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.ClimberTokenRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.ClimberTokenRevokeRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberDetailInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberPrivacySettingInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.KakaoTokenResponse;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.LoginSimpleInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.NaverTokenResponse;
import com.climeet.climeet_backend.domain.climber.enums.ResponseType;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipService;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.redis.RedisService;
import com.climeet.climeet_backend.domain.review.Review;
import com.climeet.climeet_backend.domain.review.ReviewRepository;
import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.shortscomment.ShortsCommentRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.domain.user.UserService;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClimberService {

    private final ClimberRepository climberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FollowRelationshipRepository followRelationshipRepository;
    private final UserService userService;
    private final ClimbingGymRepository climbingGymRepository;
    private final ManagerRepository managerRepository;
    private final FollowRelationshipService followRelationshipService;
    private final RedisService redisService;
    private final ShortsCommentRepository shortsCommentRepository;
    private final ReviewRepository reviewRepository;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${cloud.aws.s3.public-uri}")
    private String s3Uri;

    private static final String DEFAULT_PROFILE_ENDPOINT = "default/profile.jpg";

    @Transactional
    public LoginSimpleInfo login(String socialType,
        @RequestBody ClimberTokenRequest climberTokenRequest) {
        HashMap<String, String> userInfo = getClimberProfileByToken(socialType,
            climberTokenRequest.getAccessToken());
        String socialId = userInfo.get("socialId");
        String profileImg = userInfo.get("profileImg");
        Optional<Climber> optionalClimber = climberRepository.findBySocialIdAndSocialType(socialId,
            SocialType.valueOf(socialType));
        //login
        String accessToken;
        String refreshToken;
        if (optionalClimber.isPresent()) {
            Climber climber = optionalClimber.get();
            if(!climber.getStatus()){
                climber.updateStatus();
            }
            accessToken = jwtTokenProvider.createAccessToken(climber.getPayload());
            refreshToken = jwtTokenProvider.createRefreshToken(climber.getId());
            climber.setLastLogin(LocalDateTime.now());
            return LoginSimpleInfo.toDTO(socialType, accessToken, refreshToken,
                ResponseType.SIGN_IN);
        }
        String payload = socialId + "+" + profileImg;
        accessToken = jwtTokenProvider.generateTempToken(payload);
        refreshToken = null;
        return LoginSimpleInfo.toDTO(socialType, accessToken, refreshToken, ResponseType.SIGN_UP);


    }

    @Transactional
    public LoginSimpleInfo signUp(CreateClimberRequest createClimberRequest)  {
        String payload = jwtTokenProvider.validateTempTokenAndGetSocialId(
            createClimberRequest.getToken());
        Map<String, String> userInfo = getUserInfoInPayload(payload);
        String socialId = userInfo.get("socialId");
        String profileImg = userInfo.get("profileImg");

        if(profileImg==null){
            profileImg = s3Uri + DEFAULT_PROFILE_ENDPOINT;
        }
        SocialType socialType = createClimberRequest.getSocialType();
        if (climberRepository.findBySocialIdAndSocialType(socialId, socialType).isPresent())
            throw new GeneralException(ErrorStatus._EXIST_USER);
        Climber climber = Climber.toEntity(socialId, socialType, profileImg);
        climber.setStatus(true);
        climberRepository.save(climber);
        String accessToken = jwtTokenProvider.createAccessToken(climber.getPayload());
        String refreshToken = jwtTokenProvider.createRefreshToken(climber.getId());
        updateClimber(climber, accessToken, refreshToken, createClimberRequest);
        updateFollowList(climber, createClimberRequest.getGymFollowList());
        climber.setLastLogin(LocalDateTime.now());
        return LoginSimpleInfo.toDTO(socialType.toString(), climber.getAccessToken(), climber.getRefreshToken(), ResponseType.SIGN_UP);


    }

    @Transactional
    public void updateFollowList(Climber climber,List<Long> gymFollowList)  {
        for (Long gymId : gymFollowList) {
            ClimbingGym optionalGym = climbingGymRepository.findById(gymId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

            Manager manager = managerRepository.findByClimbingGym(optionalGym)
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM));
            followRelationshipService.createFollowRelationship(climber, manager);
            manager.increaseFollowerCount();
            climber.increaseFollowingCount();

        }
    }


    public Map<String, String> getUserInfoInPayload (String payload){
        String[] parts = payload.split("\\+");
        Map<String, String> map = new HashMap<>();

            String socialId = parts[0];
            String profileImg = parts[1];

            map.put("socialId", socialId);
            map.put("profileImg", profileImg);

        return map;
    }


    @Transactional
    public void updateClimber (Climber climber, String accessToken, String refreshToken,
        CreateClimberRequest createClimberRequest){
        climber.updateToken(accessToken, refreshToken);
        climber.updateProfileName(createClimberRequest.getNickName());
        climber.updateClimbingLevel(createClimberRequest.getClimbingLevel());
        climber.updateDiscoveryChannel(createClimberRequest.getDiscoveryChannel());
        if (!Objects.equals(createClimberRequest.getProfileImgUrl(), "")) {
            climber.updateProfileImageUrl(createClimberRequest.getProfileImgUrl());
        }
        userService.updateNotification(climber, createClimberRequest.getIsAllowFollowNotification(),createClimberRequest.getIsAllowLikeNotification(),createClimberRequest.getIsAllowCommentNotification(), createClimberRequest.getIsAllowAdNotification() );
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
            .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
    }

    private Mono<Void> disconnectSocialServer(String socialType, String accessToken)
        throws UnsupportedEncodingException {
        if(socialType.equals(SocialType.KAKAO.toString())) {
            return WebClient.create()
                .post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Void.class);
        }
        if(socialType.equals(SocialType.NAVER.toString())){
            String urlEncodeToken = URLEncoder.encode(accessToken, "UTF-8");
            String uri = String.format("https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=NAVER",
                naverClientId, naverClientSecret, urlEncodeToken);
            return WebClient.create()
                .post()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class);

        }
        if(socialType.equals(SocialType.APPLE.toString())){
            /*
            todo : apple token revoke 로직 추가
             */
        }
        return null;
    }

    public Object refreshSocialToken(String socialType, String refreshToken){
        if(socialType.equals(SocialType.KAKAO.toString())) {
            MultiValueMap<String, String> request = new LinkedMultiValueMap<>() {
            };
                request.add("grant_type", "refresh_token");
                request.add("client_id", kakaoClientId);
                request.add("refresh_token", refreshToken);
                request.add("client_secret", kakaoClientSecret);


            try {
                return WebClient.create()
                    .post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("charset", "utf-8")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
            } catch (WebClientResponseException e ){
                log.error(e.toString());
            }
        }

        if(socialType.equals(SocialType.NAVER.toString())){
            String uri = String.format("https://nid.naver.com/oauth2.0/token?grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s",
                naverClientId, naverClientSecret, refreshToken);
            try {
                return WebClient.create()
                    .post()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(NaverTokenResponse.class)
                    .block();
            } catch (WebClientResponseException e ){
                log.error(e.toString());
            }
        }
        if(socialType.equals(SocialType.APPLE.toString())){
            /*
            todo : apple token revoke 로직 추가
             */
        }
            return null;
    }

    public String getAccessToken(String socialType, String refreshToken){
        if(socialType.equals("KAKAO")){
            KakaoTokenResponse tokenResponse = (KakaoTokenResponse) refreshSocialToken(socialType, refreshToken);
            return tokenResponse.accessToken();
        }
        if(socialType.equals("NAVER")){
            NaverTokenResponse tokenResponse = (NaverTokenResponse) refreshSocialToken(socialType, refreshToken);
            return tokenResponse.accessToken();
        }
        if(socialType.equals("APPLE")){
            /*
            todo : apple 로직 추가
             */
        }
        throw new GeneralException(ErrorStatus._BAD_REQUEST);
    }

    HashMap<String, String> getClimberProfileByToken(String providerName, String userToken)
        throws RuntimeException {
        if (!providerName.equals(SocialType.KAKAO.toString()) && !providerName.equals(SocialType.NAVER.toString())) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        String socialId = null;
        String profileImg = null;
        if (providerName.equals(SocialType.KAKAO.toString())) {
            Map<String, Object> userAttributesByToken = getClimberKaKaoAttributesByToken(userToken);
            KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userAttributesByToken);
            socialId = Long.toString(kaKaoUserInfo.getID());
            profileImg = kaKaoUserInfo.getProfileImg();

        }
        if (providerName.equals(SocialType.NAVER.toString())) {
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


    @Transactional
    public boolean checkNicknameDuplication(String nickName) {
        return userRepository.findByprofileName(nickName).isPresent();
    }


    public PageResponseDto<List<ClimberDetailInfo>> searchClimber(User currentUser,
        String climberName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Climber> climberSlice = climberRepository.findByProfileNameContaining(climberName,
            pageable);

        List<ClimberDetailInfo> climberDetailInfoList = climberSlice.stream()
            .map(climber -> {
                boolean status = false;
                if (followRelationshipRepository.findByFollowerIdAndFollowingId(currentUser.getId(),
                    climber.getId()).isPresent()) {
                    status = true;
                }
                return ClimberDetailInfo.toDTO(climber, status);

            }).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), climberSlice.hasNext(),
            climberDetailInfoList);

    }

    public ClimberPrivacySettingInfo getClimberPrivacySetting(long climberId){
        Climber climber = climberRepository.findById(climberId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._INVALID_MEMBER));
        return ClimberPrivacySettingInfo.toDTO(climber);
    }

    @Transactional
    public void updateShortsPrivacySetting(User user){
        Climber climber = (Climber) user;
        climber.updateIsShortsPublic();
    }

    @Transactional
    public void updateHomeGymPrivacySetting(User user){
        Climber climber = (Climber) user;
        climber.updateIsHomeGymPublic();
    }

    @Transactional
    public void updateAverageCompletionRatePrivacySetting(User user){
        Climber climber = (Climber) user;
        climber.updateIsAverageCompletionRatePublic();
    }

    @Transactional
    public void updateAverageCompletionLevelPrivacySetting(User user){
        Climber climber = (Climber) user;
        climber.updateIsAverageCompletionLevelPublic();
    }
    @Transactional
    public void deleteShortsCommentByUser(List<ShortsComment> list){
        for(ShortsComment comment : list){
            comment.setUser(null);
            shortsCommentRepository.save(comment);
        }
    }

    public void deleteReviewByUser(List<Review> list){
        for(Review review : list){
            review.setClimber(null);
            reviewRepository.save(review);
        }
    }

    @Transactional
    public void deleteClimberAccount(User user, ClimberTokenRevokeRequest climberTokenRequest){
        if(!(user instanceof Climber))
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        user.updateStatus();
        user.setLastLogin(LocalDateTime.now());
        redisService.setValueWithExpiration(user.getId().toString(), climberTokenRequest.getRefreshToken());
    }


        /*
        todo : spring batch로 scheduling 로직 구현
         */
//    @Transactional
//    public void hardDeleteClimberAccount(User user) throws UnsupportedEncodingException {
//        Climber climber = (Climber)user;
//        String socialType = climber.getSocialType().toString();
//        String refreshToken = redisService.getValue(user.getId().toString());
//
//        //refreshToken으로 새로운 social access token 발급
//        String newAccessToken = getAccessToken(socialType, refreshToken);
//        //resource server에 토큰 disconnect 요청
//        disconnectSocialServer(socialType,newAccessToken);
//
//        //쇼츠 댓글, 암장 리뷰 유저 Null 처리
//        List<ShortsComment> shortsCommentList = shortsCommentRepository.findByUser(user);
//        List<Review> reviewList  = reviewRepository.findByClimber(climber);
//        deleteShortsCommentByUser(shortsCommentList);
//        deleteReviewByUser(reviewList);
//
//        userRepository.delete(user);
//    }
    @Transactional
    public void deleteClimber(User user){
            Climber climber = (Climber)user;
            List<ShortsComment> shortsCommentList = shortsCommentRepository.findByUser(user);
            List<Review> reviewList  = reviewRepository.findByClimber(climber);
            deleteShortsCommentByUser(shortsCommentList);
            deleteReviewByUser(reviewList);

            userRepository.delete(user);
    }


}
