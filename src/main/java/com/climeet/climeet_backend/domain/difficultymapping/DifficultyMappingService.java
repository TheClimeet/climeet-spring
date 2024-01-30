package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.difficultymapping.dto.difficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.difficultymapping.enums.ClimeetDifficulty;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DifficultyMappingService {

    private final ManagerRepository managerRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;

    @Transactional
    public void createDifficultyMapping(User user, CreateDifficultyMappingRequest createDifficultyMappingRequest) {

        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        ClimeetDifficulty climeetDifficulty = ClimeetDifficulty.findByString(createDifficultyMappingRequest.getClimeetDifficulty());

        difficultyMappingRepository.save(
            DifficultyMapping.toEntity(createDifficultyMappingRequest, manager.getClimbingGym(), climeetDifficulty));
    }
}