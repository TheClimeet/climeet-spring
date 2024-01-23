package com.climeet.climeet_backend.domain.bestclimber.time;

import com.climeet.climeet_backend.domain.bestclimber.time.dto.BestTimeClimberResponseDto.BestTimeClimberSimpleResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BestTimeClimberService {

    private final BestTimeClimberRepository bestTimeClimberRepository;

    public List<BestTimeClimberSimpleResponse> findClimberRankingOrderTime(){
        List<BestTimeClimber> ranking = bestTimeClimberRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestTimeClimberSimpleResponse::toDTO)
            .collect(Collectors.toList());
    }
}
