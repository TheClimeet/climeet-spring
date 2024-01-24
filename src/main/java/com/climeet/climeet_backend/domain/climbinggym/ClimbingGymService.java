package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClimbingGymService {

    private final ClimbingGymRepository climbingGymRepository;

    public List<ClimbingGymSimpleResponse> searchClimbingGym(String gymName) {
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContaining(gymName);

        return climbingGymList.stream()
            .map(climbingGym -> ClimbingGymSimpleResponse.toDTO(climbingGym))
            .toList();

    }

    public List<AcceptedClimbingGymSimpleResponse> searchAcceptedClimbingGym(String gymName) {
        List<ClimbingGym> climbingGymList = climbingGymRepository.findByNameContainingAndManagerIsNotNull(
            gymName);

        return climbingGymList.stream()
            .map(climbingGym -> AcceptedClimbingGymSimpleResponse.toDTO(climbingGym))
            .toList();


    }
}