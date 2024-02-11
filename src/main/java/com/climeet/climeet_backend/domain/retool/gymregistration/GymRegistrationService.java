package com.climeet.climeet_backend.domain.retool.gymregistration;

import com.climeet.climeet_backend.domain.climbinggym.BitmaskConverter;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsDetailInfo;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymRegistrationService {

    private final GymRegistrationRepository gymRegistrationRepository;
    private final BitmaskConverter bitmaskConverter;

    public List<GetGymRegistrationsSimpleInfo> getGymRegistrations() {
        List<GymRegistration> gymRegistrations = gymRegistrationRepository.findAll();

        return gymRegistrations.stream().map(
            gymRegistration -> GetGymRegistrationsSimpleInfo.toDTO(gymRegistration,
                gymRegistration.getClimbingGym(), gymRegistration.getManager()))
            .toList();
    }

    public GetGymRegistrationsDetailInfo getGymRegistration(Long gymRegistrationId) {
        GymRegistration gymRegistration = gymRegistrationRepository.findById(gymRegistrationId)
            .orElseThrow(() ->new GeneralException(ErrorStatus._EMPTY_GYM_REGISTRATION));

        return GetGymRegistrationsDetailInfo.toDTO(gymRegistration, gymRegistration.getClimbingGym(), gymRegistration.getManager());
    }
}