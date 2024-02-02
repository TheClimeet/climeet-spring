package com.climeet.climeet_backend.domain.bestclimber.clear;

import com.climeet.climeet_backend.domain.bestclimber.clear.dto.BestClearClimberResponseDto.BestClearClimberDetailInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestClearClimberService {
    private final BestClearClimberRepository bestClearClimberRepository;

    public List<BestClearClimberDetailInfo> getClimberRankingListOrderClearCount() {
        List<BestClearClimber> ranking = bestClearClimberRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestClearClimberDetailInfo::toDTO)
            .collect(Collectors.toList());
    }
}
