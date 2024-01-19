package com.climeet.climeet_backend.domain.bestrecordgym;

import com.climeet.climeet_backend.domain.bestrecordgym.dto.BestRecordGymResponseDto.BestRecordGymSimpleDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestRecordGymService {

    private final BestRecordGymRepository bestRecordGymRepository;

    public List<BestRecordGymSimpleDto> findGymRankingOrderSelectionCount() {
        List<BestRecordGym> ranking = bestRecordGymRepository.findAllByOrderByRankingAsc();
        return ranking.stream()
            .map(bestRecordGym -> new BestRecordGymSimpleDto(bestRecordGym))
            .collect(Collectors.toList());
    }
}
