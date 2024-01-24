package com.climeet.climeet_backend.domain.bestclimber.level;


import com.climeet.climeet_backend.domain.bestclimber.level.dto.BestLevelClimberResponseDto.BestLevelClimberSimpleResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BestLevelClimberService {
    private final BestLevelClimberRepository bestLevelClimberRepository;
    public List<BestLevelClimberSimpleResponse> findClimberRankingOrderLevel() {
        List<BestLevelClimber> ranking = bestLevelClimberRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestLevelClimberSimpleResponse::toDTO)
            .collect(Collectors.toList());
    }
}