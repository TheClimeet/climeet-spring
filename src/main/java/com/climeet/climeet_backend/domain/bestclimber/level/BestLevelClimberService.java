package com.climeet.climeet_backend.domain.bestclimber.level;


import com.climeet.climeet_backend.domain.bestclimber.level.dto.BestLevelClimberResponseDto.BestLevelClimberDetailInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BestLevelClimberService {
    private final BestLevelClimberRepository bestLevelClimberRepository;
    public List<BestLevelClimberDetailInfo> getClimberRankingListOrderLevel() {
        List<BestLevelClimber> ranking = bestLevelClimberRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestLevelClimberDetailInfo::toDTO)
            .collect(Collectors.toList());
    }
}