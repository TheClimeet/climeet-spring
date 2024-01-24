package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClimbingGymService {

    private final ClimbingGymRepository climbingGymRepository;

    public List<ClimbingGymSimpleResponse> searchClimbingGym(String gymName) {
        Pageable pageable = PageRequest.of(0, 20);
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContaining(gymName, pageable);

        return climbingGymList.stream()
            .map(climbingGym -> ClimbingGymSimpleResponse.toDTO(climbingGym))
            .toList();

    }

    public List<AcceptedClimbingGymSimpleResponse> searchAcceptedClimbingGym(String gymName) {
        Pageable pageable = PageRequest.of(0, 20);
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContainingAndManagerIsNotNull(
            gymName, pageable);

        return climbingGymList.stream()
            .map(climbingGym -> AcceptedClimbingGymSimpleResponse.toDTO(climbingGym))
            .toList();


    }
}