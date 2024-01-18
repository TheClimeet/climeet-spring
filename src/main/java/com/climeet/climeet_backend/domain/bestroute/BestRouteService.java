package com.climeet.climeet_backend.domain.bestroute;

import com.climeet.climeet_backend.domain.bestroute.dto.BestRouteResponseDto.BestRouteSimpleDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestRouteService {
    private final BestRouteRepository bestRouteRepository;

    public List<BestRouteSimpleDto> findBestRouteList(){
        List<BestRoute> routeList = bestRouteRepository.findAllByOrderByRankingAsc();
        return routeList.stream()
            .map(BestRouteSimpleDto::new)
            .collect(Collectors.toList());
    }
}
