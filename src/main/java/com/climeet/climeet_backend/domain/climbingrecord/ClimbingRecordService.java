package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordRepository;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecordDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
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
    private final RouteRecordRepository routeRecordRepository;

    public static final int START_DAY_OF_MONTH = 1;


    /**
     * 클라이밍기록 생성(루트기록생성포함)
     */
    @Transactional
    public ResponseEntity<String> createClimbingRecord(CreateClimbingRecordDto requestDto) {
        ClimbingGym climbingGym = gymRepository.findById(requestDto.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

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

    /**
     * 클라이밍 간편기록 전체 조회
     */
    public List<ClimbingRecordSimpleInfo> getClimbingRecords() {
        List<ClimbingRecord> recordList = climbingRecordRepository.findAll();
        if(recordList.isEmpty()){
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }
        return recordList.stream()
            .map(record -> ClimbingRecordSimpleInfo.toDTO(record))
            .toList();
    }


    /**
     * 클라이밍 상세기록 Id로 조회
     */
    public ClimbingRecordDetailInfo getClimbingRecordById(Long climbingRecordId) {
        ClimbingRecord climbingRecord = climbingRecordRepository.findById(climbingRecordId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        List<RouteRecord> routeRecordList = routeRecordRepository.findAllByClimbingRecordId(
            climbingRecordId);

        List<RouteRecordSimpleInfo> routeRecordSimpleInfoList =
            routeRecordList.stream()
                .map(RouteRecordSimpleInfo::new)
                .toList();

        return ClimbingRecordDetailInfo.toDTO(climbingRecord, routeRecordSimpleInfoList);
    }

    /**
     * 클라이밍 간편기록 날짜범위조회
     */
    public List<ClimbingRecordSimpleInfo> getClimbingRecordsBetweenLocalDates(LocalDate startDate,
        LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new GeneralException(ErrorStatus._INVALID_DATE_RANGE);
        }

        List<ClimbingRecord> climbingRecordList = climbingRecordRepository.findByClimbingDateBetween(
            startDate, endDate);

        if(climbingRecordList.isEmpty()){
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }

        // Dto로 변환
        return climbingRecordList.stream()
            .map(record -> ClimbingRecordSimpleInfo.toDTO(record))
            .collect(Collectors.toList());
    }


    /**
     * 클라이밍 기록 수정
     */
    // TODO: 2024/01/21 업데이트 될 때 routeRecordDate도 업데이트
    @Transactional
    public ClimbingRecordSimpleInfo updateClimbingRecord(Long id,
        UpdateClimbingRecordDto requestDto) {

        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        LocalDate oldDate = climbingRecord.getClimbingDate();
        LocalTime oldTime = climbingRecord.getClimbingTime();

        LocalDate newDate = requestDto.getDate();
        LocalTime newTime = requestDto.getTime();

        // updateClimbingRecordDto의 date가 null이 아니면 업데이트 수행
        if (newDate != null) {
            oldDate = newDate;
        }

        // updateClimbingRecordDto의 time이 null이 아니면 업데이트 수행
        if (newTime != null) {
            oldTime = newTime;
        }

        climbingRecord.update(oldDate, oldTime);

        return ClimbingRecordSimpleInfo.toDTO(climbingRecord);
    }

    @Transactional
    public ResponseEntity<String> deleteClimbingRecord(Long id) {
        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        climbingRecordRepository.deleteById(id);

        climbingRecord.getGym().thisWeekSelectionCountDown();

        return ResponseEntity.ok("클라이밍기록이 삭제되었습니다.");
    }

    public ClimbingRecordStatisticsInfo getClimbingRecordStatistics(int year, int month) {

        LocalDate startDate = LocalDate.of(year, month, START_DAY_OF_MONTH);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        Tuple crTuple = climbingRecordRepository.getStatisticsInfoBetween(startDate, endDate);

        if (crTuple.get("totalTime") == null) {
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }
        Double totalTime = (Double) crTuple.get("totalTime");
        LocalTime time = convertDoubleToTime(totalTime);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        List<Map<Long, Long>> difficultyList = routeRecordRepository
            .getRouteRecordDifficultyBetween(
            startDate,
            endDate
            );

        return ClimbingRecordStatisticsInfo.toDTO(
            time,
            totalCompletedCount,
            attemptRouteCount,
            difficultyList
        );
    }

// TODO: 2024/01/21 24시간을 초과했을 때 에러처리
    public static LocalTime convertDoubleToTime(double totalSeconds) {
        int seconds = (int) totalSeconds;
        int hours = (seconds / 3600) % 24;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return LocalTime.of(hours, minutes, remainingSeconds);
    }

}