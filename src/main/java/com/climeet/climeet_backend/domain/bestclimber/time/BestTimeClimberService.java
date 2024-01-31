package com.climeet.climeet_backend.domain.bestclimber.time;

import com.climeet.climeet_backend.domain.bestclimber.time.dto.BestTimeClimberResponseDto.BestTimeClimberDetailInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BestTimeClimberService {

    private final BestTimeClimberRepository bestTimeClimberRepository;

    public List<BestTimeClimberDetailInfo> getClimberRankingListOrderTime(){
        List<BestTimeClimber> ranking = bestTimeClimberRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(BestTimeClimberDetailInfo::toDTO)
            .collect(Collectors.toList());
    }
}
