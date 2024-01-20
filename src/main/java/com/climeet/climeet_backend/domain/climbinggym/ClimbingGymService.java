package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClimbingGymService {

    private final ClimbingGymRepository climbingGymRepository;

    public List<ClimbingGymSimpleResponse> searchClimbingGym(String word) {
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContaining(word);

        return climbingGymList.stream()
            .map(ClimbingGymSimpleResponse::new)
            .collect(Collectors.toList());

    }

    public List<ClimbingGymSimpleResponse> searchAcceptedClimbingGym(String word) {
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContainingAndManagerIsNotNull(word);

        return climbingGymList.stream()
            .map(ClimbingGymSimpleResponse::new)
            .collect(Collectors.toList());

    }
}