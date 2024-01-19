package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecordDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClimbingRecordService {

    private final ClimbingRecordRepository climbingRecordRepository;
    private final ClimbingGymRepository gymRepository;
    private final RouteRecordService routeRecordService;


    @Transactional
    public ResponseEntity<String> createClimbingRecord(CreateClimbingRecordDto requestDto) {
        ClimbingGym climbingGym = gymRepository.findById(requestDto.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        ClimbingRecord savedClimbingRecord = climbingRecordRepository
            .save(ClimbingRecord.toEntity(requestDto, climbingGym));

        //선택받았으니 하나 추가
        climbingGym.thisWeekSelectionCountUp();

        List<CreateRouteRecordDto> routeRecords = requestDto.getRouteRecordRequestDtoList();
        // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장

        routeRecords.forEach(
            routeRecord -> routeRecordService.addRouteRecord(routeRecord, savedClimbingRecord));

        return ResponseEntity.ok("클라이밍 기록생성 성공");
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
        return new ClimbingRecordSimpleInfo(climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD)));
    }

    public List<ClimbingRecordSimpleInfo> getClimbingRecordsBetweenLocalDates(LocalDate startDate,
        LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new GeneralException(ErrorStatus._INVALID_DATE_RANGE);
        }

        List<ClimbingRecord> climbingRecords = climbingRecordRepository.findByClimbingDateBetween(
            startDate, endDate);
        // Dto로 변환
        return climbingRecords.stream()
            .map(ClimbingRecordSimpleInfo::new)
            .collect(Collectors.toList());
    }


    @Transactional
    public ClimbingRecordSimpleInfo updateClimbingRecord(Long id,
        UpdateClimbingRecordDto requestDto) {

        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD));

        LocalDate oldDate = climbingRecord.getClimbingDate();
        LocalTime oldTime = climbingRecord.getClimbingTime();

        LocalDate newDate = climbingRecord.getClimbingDate();
        LocalTime newTime = climbingRecord.getClimbingTime();

        // updateClimbingRecordDto의 date가 null이 아니면 업데이트 수행
        if (newDate != null) {
            oldDate = newDate;
        }

        // updateClimbingRecordDto의 time이 null이 아니면 업데이트 수행
        if (newTime != null) {
            oldTime = newTime;
        }

        climbingRecord.update(oldDate, oldTime);

        return new ClimbingRecordSimpleInfo(climbingRecord);
    }

    @Transactional
    public ResponseEntity<String> deleteClimbingRecord(Long id) {
        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD));

        climbingRecordRepository.deleteById(id);

        climbingRecord.getGym().thisWeekSelectionCountDown();

        return ResponseEntity.ok("클라이밍기록이 삭제되었습니다.");
    }
}