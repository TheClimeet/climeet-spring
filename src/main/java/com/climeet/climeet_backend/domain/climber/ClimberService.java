package com.climeet.climeet_backend.domain.climber;


import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.ClimberTokenRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberRequestDto.CreateClimberRequest;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberDetailInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.ClimberPrivacySettingInfo;
import com.climeet.climeet_backend.domain.climber.dto.ClimberResponseDto.LoginSimpleInfo;
import com.climeet.climeet_backend.domain.climber.enums.ResponseType;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipService;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.domain.user.UserService;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;


@Service
@RequiredArgsConstructor
public class ClimberService {

    private final ClimberRepository climberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FollowRelationshipRepository followRelationshipRepository;
    private final UserService userService;
    private final ClimbingGymRepository climbingGymRepository;
    private final ManagerRepository managerRepository;
    private final FollowRelationshipService followRelationshipService;

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
            accessToken = jwtTokenProvider.createAccessToken(optionalClimber.get().getPayload());
            refreshToken = jwtTokenProvider.createRefreshToken(optionalClimber.get().getId());
            optionalClimber.get().setLastLogin(LocalDateTime.now());
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
        SocialType socialType = createClimberRequest.getSocialType();
        if (climberRepository.findBySocialIdAndSocialType(socialId, socialType).isPresent())
            throw new GeneralException(ErrorStatus._EXIST_USER);
        Climber climber = Climber.toEntity(socialId, socialType, profileImg);
        climberRepository.save(climber);
        String accessToken = jwtTokenProvider.createAccessToken(climber.getPayload());
        String refreshToken = jwtTokenProvider.createRefreshToken(climber.getId());
        updateClimber(climber, accessToken, refreshToken, createClimberRequest);
        updateFollowList(climber, createClimberRequest.getGymFollowList());
        return LoginSimpleInfo.toDTO(socialType.toString(), climber.getAccessToken(), climber.getRefreshToken(), ResponseType.SIGN_UP);


    }

    @Transactional
    public void updateFollowList(Climber climber,List<Long> gymFollowList)  {
        for (Long gymId : gymFollowList) {
            ClimbingGym optionalGym = climbingGymRepository.findById(gymId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

            Manager manager = managerRepository.findByClimbingGym(optionalGym)
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM));
            followRelationshipService.createFollowRelationship(manager, climber);
            manager.increaseFollwerCount();
            climber.increaseFollwingCount();

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


}
