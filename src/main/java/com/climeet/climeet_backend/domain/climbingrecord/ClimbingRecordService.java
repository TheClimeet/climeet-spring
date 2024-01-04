package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordRepository;
import com.climeet.climeet_backend.global.response.ApiResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.climeet.climeet_backend.domain.routerecord.RouteRecord.toEntity;

@RequiredArgsConstructor
@Service
public class ClimbingRecordService {

    private final ClimbingRecordRepository climbingRecordRepository;
    private final ClimbingGymRepository gymRepository;
    private final RouteRecordRepository routeRecordRepository;
    private final RouteRepository routeRepository;

    public ApiResponse<String> createClimbingRecord(ClimbingRecordRequestDto requestDto) {
        try {
            ClimbingGym climbingGym = gymRepository.findByName(requestDto.getClimbingGymName());
            //먼저 암장기록 저장
            ClimbingRecord climbingRecord = climbingRecordRepository
                .save(ClimbingRecord.toEntity(requestDto, climbingGym));

            // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장
            List<RouteRecord> routeRecords = requestDto.getRouteRecordRequestDtoList().stream()
                .map(dto -> toEntity(dto, climbingRecord,
                    routeRepository.findById(dto.getRouteId()).get()))
                .collect(Collectors.toList());
            routeRecordRepository.saveAll(routeRecords);
            return ApiResponse.onSuccess("클라이밍 기록생성 성공");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

    }
}