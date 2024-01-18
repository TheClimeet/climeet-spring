package com.climeet.climeet_backend.domain.manager;


import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImage;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImageRepository;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Builder
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final ClimbingGymBackgroundImageRepository climbingGymBackgroundImageRepository;

    @Transactional
    public boolean checkManagerRegistration(String gymName){
        ClimbingGym gym = climbingGymRepository.findByName(gymName)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        return managerRepository.existsByClimbingGym(gym);
    }


    @Transactional
    public Manager signUp(@RequestBody CreateManagerRequest createManagerRequest){
        Manager manager = toEntity(createManagerRequest);
        managerRepository.save(manager);

        //배경사진 추가
        saveClimbingGymBackgroundImage(createManagerRequest, manager.getClimbingGym());
        //관리자 등록
        setManagerToClimbingGym(manager);

        return manager;
    }
    private void saveClimbingGymBackgroundImage(CreateManagerRequest createManagerRequest, ClimbingGym gym){
        ClimbingGymBackgroundImage climbingGymBackgroundImage = ClimbingGymBackgroundImage.builder()
            .climbingGym(gym)
            .imgUrl(createManagerRequest.getBackGroundImageUri())
            .build();

        climbingGymBackgroundImageRepository.save(climbingGymBackgroundImage);
    }

    private void setManagerToClimbingGym(Manager manager){
        ClimbingGym gym = manager.getClimbingGym();
        gym.setManager(manager);
        climbingGymRepository.save(gym);
    }

    @Transactional
    public boolean checkLoginDuplication(String loginId){
        return managerRepository.findByLoginId(loginId).isPresent();
    }

    public Manager toEntity(CreateManagerRequest createManagerRequest){
        ClimbingGym gym = climbingGymRepository.findByName(createManagerRequest.getGymName())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        return Manager.builder()
            .loginId(createManagerRequest.getLoginId())
            .password(createManagerRequest.getPassword())
            .name(createManagerRequest.getName())
            .phoneNumber(createManagerRequest.getPhoneNumber())
            .email(createManagerRequest.getEmail())
            .isRegistered(true)
            .climbingGym(gym)
            .build();
    }



}
