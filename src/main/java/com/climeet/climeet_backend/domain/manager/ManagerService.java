package com.climeet.climeet_backend.domain.manager;


import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImage;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImageRepository;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateAccessTokenRequest;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.domain.manager.dto.ManagerResponseDto.ManagerSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Builder
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final ClimbingGymBackgroundImageRepository climbingGymBackgroundImageRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ManagerSimpleInfo login(@RequestBody CreateAccessTokenRequest createAccessTokenRequest){
        String loginId = createAccessTokenRequest.getLoginId();
        String password = createAccessTokenRequest.getPassword();
        Manager IdManager = managerRepository.findByLoginId(loginId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._WRONG_LOGINID));

        if(!IdManager.checkPassword(password, passwordEncoder)){
            throw new GeneralException(ErrorStatus._WRONG_PASSWORD);
        }
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(IdManager.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        IdManager.updateToken(accessToken, refreshToken);

        return new ManagerSimpleInfo(IdManager);

    }
    @Transactional
    public boolean checkManagerRegistration(String gymName){
        ClimbingGym gym = climbingGymRepository.findByName(gymName)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        return managerRepository.existsByClimbingGym(gym);
    }


    @Transactional
    public Manager signUp(@RequestBody CreateManagerRequest createManagerRequest){
       ClimbingGym gym = climbingGymRepository.findByName(createManagerRequest.getGymName())
           .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

       //todo : 관리자 중복 매핑 예외 처리

        if(managerRepository.findByLoginId(createManagerRequest.getLoginId()).isPresent()){
            throw new GeneralException(ErrorStatus._DUPLICATE_LOGINID);
        }

        Manager manager = Manager.toEntity(createManagerRequest, gym);
        manager.hashPassword(passwordEncoder);
        managerRepository.save(manager);

        //배경사진 추가
        saveClimbingGymBackgroundImage(createManagerRequest, manager.getClimbingGym());
        //관리자 등록
        manager.updateClimbingGym(gym);
        manager.updateNotification(createManagerRequest.getIsAllowFollowNotification(), createManagerRequest.getIsAllowLikeNotification(), createManagerRequest.getIsAllowCommentNotification(), createManagerRequest.getIsAllowAdNotification());
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(manager.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        manager.updateToken(accessToken, refreshToken);

        return manager;
    }
    private void saveClimbingGymBackgroundImage(CreateManagerRequest createManagerRequest, ClimbingGym gym){
        ClimbingGymBackgroundImage climbingGymBackgroundImage = ClimbingGymBackgroundImage.builder()
            .climbingGym(gym)
            .imgUrl(createManagerRequest.getBackGroundImageUri())
            .build();

        climbingGymBackgroundImageRepository.save(climbingGymBackgroundImage);
    }

    @Transactional
    public boolean checkLoginDuplication(String loginId){
        return managerRepository.findByLoginId(loginId).isPresent();
    }





}
