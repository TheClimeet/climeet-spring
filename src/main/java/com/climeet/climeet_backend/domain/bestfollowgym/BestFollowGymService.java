package com.climeet.climeet_backend.domain.bestfollowgym;

import com.climeet.climeet_backend.domain.bestfollowgym.dto.BestFollowGymResponseDto.BestFollowGymSimpleDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestFollowGymService {

    private final BestFollowGymRepository bestFollowGymRepository;

    public List<BestFollowGymSimpleDto> findGymRankingOrderFollowCount() {
        List<BestFollowGym> ranking = bestFollowGymRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(bestFollowGym -> new BestFollowGymSimpleDto(bestFollowGym,
                bestFollowGym.getClimbingGym()))
            .collect(Collectors.toList());
    }

}
