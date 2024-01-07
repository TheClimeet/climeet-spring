package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordRepository;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClimbingRecordService {

    private final ClimbingRecordRepository climbingRecordRepository;
    private final ClimbingGymRepository gymRepository;
    private final RouteRecordService routeRecordService;


    @Transactional
    public ApiResponse<String> createClimbingRecord(ClimbingRecordRequestDto requestDto) {
        try {
            ClimbingGym climbingGym = gymRepository.findById(requestDto.getGymId()).orElseThrow();
            //먼저 암장기록 저장

            ClimbingRecord savedClimbingRecord = climbingRecordRepository
                .save(ClimbingRecord.toEntity(requestDto, climbingGym));

            List<RouteRecordRequestDto> routeRecords = requestDto.getRouteRecordRequestDtoList();
            // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장

            routeRecords.forEach(
                routeRecord -> routeRecordService.addRouteRecord(routeRecord, savedClimbingRecord));

            return ApiResponse.onSuccess("클라이밍 기록생성 성공");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    public List<ClimbingRecordSimpleInfo> getClimbingRecords() {
        try {
            List<ClimbingRecord> recordList = climbingRecordRepository.findAll();
            return recordList.stream()
                .map(ClimbingRecordSimpleInfo::new)
                .toList();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }


    public ClimbingRecordSimpleInfo getClimbingRecord(Long id) {
        try {
            return new ClimbingRecordSimpleInfo(climbingRecordRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST)));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<ClimbingRecordSimpleInfo> getClimbingRecordsBetweenLocalDates(LocalDate startDate,
        LocalDate endDate) {
        try {
            List<ClimbingRecord> climbingRecords = climbingRecordRepository.findByClimbingDateBetween(
                startDate, endDate);
            // Dto로 변환
            return climbingRecords.stream()
                .map(ClimbingRecordSimpleInfo::new)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}