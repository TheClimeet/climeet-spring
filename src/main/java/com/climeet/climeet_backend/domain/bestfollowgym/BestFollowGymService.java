package com.climeet.climeet_backend.domain.bestfollowgym;

import com.climeet.climeet_backend.domain.bestfollowgym.dto.BestFollowGymResponseDto.BestFollowGymDetailInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestFollowGymService {

    private final BestFollowGymRepository bestFollowGymRepository;

    public List<BestFollowGymDetailInfo> getGymRankingListOrderFollowCount() {
        List<BestFollowGym> ranking = bestFollowGymRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestFollowGymDetailInfo::toDTO)
            .collect(Collectors.toList());
    }

}
