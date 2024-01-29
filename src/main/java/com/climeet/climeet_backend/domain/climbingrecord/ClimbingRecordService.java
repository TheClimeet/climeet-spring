package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestClearUserSimple;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestLevelUserSimple;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestTimeUserSimple;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsSimpleInfo;
import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordRepository;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecordDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

@RequiredArgsConstructor
@Service
public class ClimbingRecordService {

    private final ClimbingRecordRepository climbingRecordRepository;
    private final ClimbingGymRepository gymRepository;
    private final RouteRecordService routeRecordService;
    private final RouteRecordRepository routeRecordRepository;

    public static final int START_DAY_OF_MONTH = 1;
    public static final int MONDAY = 0;
    public static final int SUNDAY = 1;
    public static final int RANKING_USER = 0;
    public static final int RANKING_CONDITION = 1;


    /**
     * 클라이밍기록 생성(루트기록생성포함)
     */
    @Transactional
    public ResponseEntity<String> createClimbingRecord(User user,
        CreateClimbingRecordDto requestDto) {
        ClimbingGym climbingGym = gymRepository.findById(requestDto.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        ClimbingRecord savedClimbingRecord = climbingRecordRepository
            .save(ClimbingRecord.toEntity(user, requestDto, climbingGym));

        //선택받았으니 하나 추가
        climbingGym.thisWeekSelectionCountUp();
        Long totalTime = requestDto.getTime().toNanoOfDay() / 1000000000;
        user.thisWeekTotalClimbingTimeUp(totalTime);

        List<CreateRouteRecordDto> routeRecords = requestDto.getRouteRecordRequestDtoList();
        // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장

        routeRecords.forEach(
            routeRecord -> routeRecordService.addRouteRecord(user, routeRecord,
                savedClimbingRecord));

        return ResponseEntity.ok("클라이밍 기록생성 성공");
    }

    /**
     * 클라이밍 간편기록 전체 조회 - ID 상관없이
     */
    public List<ClimbingRecordSimpleInfo> getClimbingRecords() {
        List<ClimbingRecord> recordList = climbingRecordRepository.findAll();
        if (recordList.isEmpty()) {
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
    public List<ClimbingRecordSimpleInfo> getClimbingRecordsBetweenLocalDates(User user,
        LocalDate startDate,
        LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new GeneralException(ErrorStatus._INVALID_DATE_RANGE);
        }

        List<ClimbingRecord> climbingRecordList = climbingRecordRepository.findByClimbingDateBetweenAndUser(
            startDate, endDate, user);

        if (climbingRecordList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }

        return climbingRecordList.stream()
            .map(record -> ClimbingRecordSimpleInfo.toDTO(record))
            .collect(Collectors.toList());
    }


    /**
     * 클라이밍 기록 수정
     */
    @Transactional
    public ClimbingRecordSimpleInfo updateClimbingRecord(User user, Long id,
        UpdateClimbingRecordDto requestDto) {

        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        if (!user.getId().equals(climbingRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }

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
    public ResponseEntity<String> deleteClimbingRecord(User user, Long id) {

        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        if (!user.getId().equals(climbingRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }

        climbingRecordRepository.deleteById(id);

        climbingRecord.getGym().thisWeekSelectionCountDown();

        return ResponseEntity.ok("클라이밍기록이 삭제되었습니다.");
    }

    public ClimbingRecordStatisticsInfo getClimbingRecordStatistics(User user, int year,
        int month) {

        LocalDate startDate = LocalDate.of(year, month, START_DAY_OF_MONTH);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        Tuple crTuple = climbingRecordRepository.getStatisticsInfoBetweenDaysAndUser(user,
            startDate, endDate);

        if (crTuple.get("totalTime") == null) {
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }
        Double totalTime = (Double) crTuple.get("totalTime");
        LocalTime time = convertDoubleToTime(totalTime);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        List<Map<Long, Long>> difficultyList = routeRecordRepository
            .getRouteRecordDifficultyBetween(
                user,
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

    public ClimbingRecordStatisticsSimpleInfo findClimbingRecordStatisticsAndGym(Long gymId) {

        Object[] lastWeek = startDayAndLastDayOfLastWeek();


        LocalDate startDate = (LocalDate) lastWeek[MONDAY];
        LocalDate endDate = (LocalDate) lastWeek[SUNDAY];

        ClimbingGym climbingGym = gymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        List<Map<Long, Long>> difficultyList = routeRecordRepository
            .getRouteRecordDifficultyBetweenDaysAndGym(
                climbingGym,
                startDate,
                endDate
            );

        return ClimbingRecordStatisticsSimpleInfo.toDTO(
            difficultyList
        );
    }

    public List<BestClearUserSimple> findBestClearUserRanking(Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByClearRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        int[] rank = {1};
        List<BestClearUserSimple> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                Long totalCount = (Long) userRankMap[RANKING_CONDITION];
                return BestClearUserSimple.toDTO(user, rank[0]++, totalCount);
            })
            .collect(Collectors.toList());

        return ranking;
    }

    public List<BestTimeUserSimple> findBestTimeUserRanking(Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByTimeRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        int[] rank = {1};
        List<BestTimeUserSimple> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                LocalTime totalTime = convertDoubleToTime((Double) userRankMap[RANKING_CONDITION]);
                return BestTimeUserSimple.toDTO(user, rank[0]++, totalTime);
            })
            .collect(Collectors.toList());

        return ranking;
    }

    public List<BestLevelUserSimple> findBestLevelUserRanking(Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByLevelRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        int[] rank = {1};
        List<BestLevelUserSimple> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                int highDifficulty = (int) userRankMap[RANKING_CONDITION];
                return BestLevelUserSimple.toDTO(user, rank[0]++, highDifficulty);
            })
            .collect(Collectors.toList());

        return ranking;
    }


    // TODO: 2024/01/21 24시간을 초과했을 때 에러처리
    public static LocalTime convertDoubleToTime(double totalSeconds) {
        int seconds = (int) totalSeconds;
        int hours = (seconds / 3600) % 24;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return LocalTime.of(hours, minutes, remainingSeconds);
    }

    public static Object[] startDayAndLastDayOfLastWeek() {
        // 현재 시점의 날짜 정보 가져오기
        LocalDate today = LocalDate.now();

        // 현재 주의 월요일
        LocalDate startOfThisWeek = today.with(
            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 지난 주의 월요일
        LocalDate startOfLastWeek = startOfThisWeek.minusDays(7);
        LocalDate endOfLastWeek = startOfThisWeek.minusDays(1);

        Object[] lastWeek = new Object[2];

        lastWeek[MONDAY] = startOfLastWeek;
        lastWeek[SUNDAY] = endOfLastWeek;

        return lastWeek;
    }

}