package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.difficultymapping.enums.ClimeetDifficulty;
import com.climeet.climeet_backend.domain.difficultymapping.enums.GymDifficulty;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DifficultyMappingService {

    private final ManagerRepository managerRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;

    @Transactional
    public List<Long> createDifficultyMapping(User user,
        CreateDifficultyMappingRequest createDifficultyMappingRequest) {

        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        return createDifficultyMappingRequest.getGymClimeetDifficulty().entrySet().stream()
            .map((element) -> {
                DifficultyMapping difficultyMapping = difficultyMappingRepository.save(
                    DifficultyMapping.toEntity(
                        GymDifficulty.findByName(element.getKey()),
                        ClimeetDifficulty.findByString(element.getValue()),
                        manager.getClimbingGym()));
                return difficultyMapping.getId();
            })
            .toList();
    }

    public List<DifficultyMappingDetailResponse> getDifficultyMapping(Long gymId) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        List<DifficultyMapping> difficultyMappingList = difficultyMappingRepository.findByClimbingGymOrderByDifficultyAsc(
            climbingGym);

        if (difficultyMappingList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_DIFFICULTY_LIST);
        }

        return difficultyMappingList.stream().map(DifficultyMappingDetailResponse::toDto).toList();
    }

    public Map<String, String> getGymDifficultyColorList() {
        return Arrays.stream(GymDifficulty.values())
            .collect(Collectors.toMap(Enum::name, GymDifficulty::getColorCode,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}