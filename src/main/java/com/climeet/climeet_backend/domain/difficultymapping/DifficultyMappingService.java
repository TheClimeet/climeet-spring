package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.difficultymapping.enums.GymDifficulty;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DifficultyMappingService {
    private final ClimbingGymRepository climbingGymRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;

    public List<DifficultyMappingDetailResponse> getDifficultyMapping(Long gymId) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        List<DifficultyMapping> difficultyMappingList = difficultyMappingRepository.findByClimbingGymOrderByDifficultyAsc(
            climbingGym);

        if (difficultyMappingList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_DIFFICULTY_LIST);
        }

        return difficultyMappingList.stream().map(DifficultyMappingDetailResponse::toDTO).toList();
    }

    public Map<String, String> getGymDifficultyColorList() {
        return Arrays.stream(GymDifficulty.values())
            .collect(Collectors.toMap(Enum::name, GymDifficulty::getColorCode,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}