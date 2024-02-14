package com.climeet.climeet_backend.domain.climbingrecord;

import static com.climeet.climeet_backend.global.utils.DateTimeConverter.convertDoubleToStringTime;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecord;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecord;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestClearUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestLevelUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestTimeUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfoByGym;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordUserAndGymStatisticsDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordUserStatisticsSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.GymDifficultyMappingInfo;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.difficultymapping.enums.ClimeetDifficulty;
import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordRepository;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
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
    private final UserRepository userRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;

    public static final int START_DAY_OF_MONTH = 1;
    public static final int MONDAY = 0;
    public static final int SUNDAY = 1;
    public static final int RANKING_USER = 0;
    public static final int RANKING_CONDITION = 1;
    public static final int RANKING_COUNT = 2;

    public static final int CLIMEET_LEVEL = 0;
    public static final int LEVEL_COUNT = 1;

    public static final int NANO_TO_SEC = 1000000000;


    /**
     * 클라이밍기록 생성(루트기록생성포함)
     */
    @Transactional
    public ResponseEntity<String> createClimbingRecord(User user,
        CreateClimbingRecord requestDto) {
        ClimbingGym climbingGym = gymRepository.findById(requestDto.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        ClimbingRecord savedClimbingRecord = climbingRecordRepository
            .save(ClimbingRecord.toEntity(user, requestDto, climbingGym));

        //선택받았으니 하나 추가
        climbingGym.thisWeekSelectionCountUp();
        Long totalTime = requestDto.getTime().toNanoOfDay() / NANO_TO_SEC;
        user.thisWeekTotalClimbingTimeUp(totalTime);

        List<CreateRouteRecord> routeRecords = requestDto.getRouteRecordRequestDtoList();
        // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장

        routeRecords.forEach(
            routeRecord -> routeRecordService.addRouteRecord(user, routeRecord,
                savedClimbingRecord));

        return ResponseEntity.ok("클라이밍 기록생성 성공");
    }

    /**
     * 클라이밍 간편기록 전체 조회 - ID 상관없이
     */
    public List<ClimbingRecordSimpleInfo> getClimbingRecordList() {
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
    public List<ClimbingRecordSimpleInfo> getClimbingRecordListBetweenDates(User user,
        LocalDate startDate,
        LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new GeneralException(ErrorStatus._INVALID_DATE_RANGE);
        }

        List<ClimbingRecord> climbingRecordList = climbingRecordRepository.findByClimbingDateBetweenAndUser(
            startDate, endDate, user);

        return climbingRecordList.stream()
            .map(record -> ClimbingRecordSimpleInfo.toDTO(record))
            .collect(Collectors.toList());
    }


    /**
     * 클라이밍 기록 수정
     */
    @Transactional
    public ClimbingRecordSimpleInfo updateClimbingRecord(User user, Long id,
        UpdateClimbingRecord requestDto) {

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

        Long updateTime = 0L;
        // updateClimbingRecordDto의 time이 null이 아니면 업데이트 수행
        if (newTime != null) {
            updateTime = newTime.toNanoOfDay()/NANO_TO_SEC - oldTime.toNanoOfDay()/NANO_TO_SEC;
            System.out.println("oldTIme = " + oldTime);
            System.out.println("newTime = " + newTime);
            System.out.println("updateTime = " + updateTime);
            oldTime = newTime;
        }

        climbingRecord.update(oldDate, oldTime);

        user.thisWeekTotalClimbingTimeUp(updateTime);

        return ClimbingRecordSimpleInfo.toDTO(climbingRecord);
    }

    @Transactional
    public ResponseEntity<String> deleteClimbingRecord(User user, Long id) {

        ClimbingRecord climbingRecord = climbingRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._CLIMBING_RECORD_NOT_FOUND));

        if (!user.getId().equals(climbingRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }

        Long totalTime = climbingRecord.getClimbingTime().toNanoOfDay() / NANO_TO_SEC;
        user.thisWeekTotalClimbingTimeDown(totalTime);
        climbingRecord.getGym().thisWeekSelectionCountDown();
        climbingRecordRepository.deleteById(id);

        return ResponseEntity.ok("클라이밍기록이 삭제되었습니다.");
    }

    /**
     * 내 월별 통계 return 완등시간 & 완등률(시도한 루트와 성공한 루트의 비율) & 레벨당 완등한 횟수 클밋 기준임.
     */
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
        String time = convertDoubleToStringTime(totalTime);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        //유저가 기록한 레벨 리스트를 뽑아 온다.
        //여기는 기록한 레벨과 그에 매칭되는 횟수가 나온다.
        List<Object[]> difficulties = routeRecordRepository
            .getRouteRecordDifficultyBetween(
                user,
                startDate,
                endDate
            );

        Map<String, Long> difficultyList;
        difficultyList = difficulties.stream()
            .collect(Collectors.toMap(
                arr -> ClimeetDifficulty.findByInt(((int) arr[CLIMEET_LEVEL]))
                    .getStringValue(),
                arr -> (Long) arr[LEVEL_COUNT]
            ));

        return ClimbingRecordStatisticsInfo.toDTO(
            time,
            totalCompletedCount,
            attemptRouteCount,
            difficultyList
        );
    }

    /**
     * 나의 월별 그리고 암장별 통계기록
     */
    public ClimbingRecordStatisticsInfoByGym getClimbingRecordStatisticsByGymId(User user,
        Long gymId,
        int year,
        int month) {

        LocalDate startDate = LocalDate.of(year, month, START_DAY_OF_MONTH);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        ClimbingGym gym = gymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Tuple crTuple = climbingRecordRepository.getStatisticsInfoBetweenDaysAndUserAndGym(user,
            gym,
            startDate, endDate
        );

        if (crTuple.get("totalTime") == null) {
            throw new GeneralException(ErrorStatus._EMPTY_CLIMBING_RECORD);
        }

        Double totalTime = (Double) crTuple.get("totalTime");

        String time = convertDoubleToStringTime(totalTime);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        //유저가 기록한 레벨 리스트를 뽑아 온다.
        //여기는 기록한 레벨과 그에 매칭되는 횟수가 나온다.
        List<Object[]> difficulties = routeRecordRepository
            .getRouteRecordDifficultyBetweenDatesAndGym(
                user,
                gym,
                startDate,
                endDate
            );

        List<GymDifficultyMappingInfo> difficultyList = difficulties.stream()
            .map(arr -> {
                DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndDifficulty(
                    gym, ((int) arr[CLIMEET_LEVEL]));
                Long levelCount = (Long) arr[LEVEL_COUNT];

                return GymDifficultyMappingInfo.toDTO(difficultyMapping, levelCount);
            })
            .collect(Collectors.toList());

        return ClimbingRecordStatisticsInfoByGym.toDTO(
            time,
            totalCompletedCount,
            attemptRouteCount,
            difficultyList
        );
    }

    public ClimbingRecordStatisticsSimpleInfo getGymStatisticsWeekly(Long gymId) {

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        LocalDate startDate = (LocalDate) lastWeek[MONDAY];
        LocalDate endDate = (LocalDate) lastWeek[SUNDAY];

        ClimbingGym gym = gymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        List<Object[]> difficulties = routeRecordRepository
            .getRouteRecordDifficultyBetweenDaysAndGym(
                gym,
                startDate,
                endDate
            );

        List<GymDifficultyMappingInfo> difficultyList = difficulties.stream()
            .map(arr -> {
                DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndDifficulty(
                    gym, ((int) arr[CLIMEET_LEVEL]));
                Long levelCount = (Long) arr[LEVEL_COUNT];

                return GymDifficultyMappingInfo.toDTO(difficultyMapping, levelCount);
            })
            .collect(Collectors.toList());

        return ClimbingRecordStatisticsSimpleInfo.toDTO(difficultyList);
    }

    public List<BestClearUserSimpleInfo> getClimberRankingListOrderClearCountByGym(
        Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByClearRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        int[] rank = {1};
        List<BestClearUserSimpleInfo> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                Long totalCount = (Long) userRankMap[RANKING_CONDITION];
                return BestClearUserSimpleInfo.toDTO(user, rank[0]++, totalCount);
            })
            .collect(Collectors.toList());

        return ranking;
    }

    public List<BestTimeUserSimpleInfo> getClimberRankingListOrderTimeByGym(Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByTimeRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        int[] rank = {1};
        List<BestTimeUserSimpleInfo> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                String totalTime = convertDoubleToStringTime(
                    (Double) userRankMap[RANKING_CONDITION]);
                return BestTimeUserSimpleInfo.toDTO(user, rank[0]++, totalTime);
            })
            .collect(Collectors.toList());

        return ranking;
    }

    public List<BestLevelUserSimpleInfo> getClimberRankingListOrderLevelByGym(Long climbingGymId) {
        ClimbingGym climbingGym = gymRepository.findById(climbingGymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Object[] lastWeek = startDayAndLastDayOfLastWeek();

        List<Object[]> bestUserRanking = climbingRecordRepository.findByLevelRankingClimbingDateBetweenAndClimbingGym(
            (LocalDate) lastWeek[MONDAY], (LocalDate) lastWeek[SUNDAY], climbingGym);

        List<DifficultyMapping> difficultyMappingList
            = difficultyMappingRepository.findByClimbingGymOrderByDifficultyAsc(climbingGym);

        int[] rank = {1};
        List<BestLevelUserSimpleInfo> ranking = bestUserRanking.stream()
            .map(userRankMap -> {
                User user = (User) userRankMap[RANKING_USER];
                int highDifficulty = (int) userRankMap[RANKING_CONDITION];
                Number count = (Number) userRankMap[RANKING_COUNT];
                int highDifficultyCount = count.intValue();

                DifficultyMapping difficultyMapping = difficultyMappingList.stream()
                    .filter(iter -> iter.getDifficulty() == highDifficulty)
                    .findAny().get();

                String climeetDifficultyName = difficultyMapping.getClimeetDifficultyName();
                String gymDifficultyName = difficultyMapping.getGymDifficultyName();
                String gymDifficultyColor = difficultyMapping.getGymDifficultyColor();

                return BestLevelUserSimpleInfo.toDTO(user, rank[0]++, highDifficulty,
                    highDifficultyCount, climeetDifficultyName, gymDifficultyName,
                    gymDifficultyColor);
            })
            .collect(Collectors.toList());

        return ranking;
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

    // TODO: 2024/01/29 기록 생성할 때 유저의 누적 통계도 바뀌어야 한다능..
    public ClimbingRecordUserStatisticsSimpleInfo getUserStatistics(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_MEMBER));

        //완등률
        Tuple crTuple = climbingRecordRepository.findAllClearRateAndUser(user);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        //기록
        List<Object[]> difficulties = routeRecordRepository
            .findAllRouteRecordDifficultyAndUser(user);

        Map<String, Long> difficultyList = difficulties.stream()
            .collect(Collectors.toMap(
                arr -> ClimeetDifficulty.findByInt(((int) arr[CLIMEET_LEVEL]))
                    .getStringValue(),
                arr -> (Long) arr[LEVEL_COUNT]
            ));

        return ClimbingRecordUserStatisticsSimpleInfo.toDTO(
            userId,
            totalCompletedCount,
            attemptRouteCount,
            difficultyList
        );
    }


    public ClimbingRecordUserAndGymStatisticsDetailInfo getUserStatisticsByGym(Long userId,
        Long gymId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_MEMBER));

        ClimbingGym gym = gymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        //완등률
        Tuple crTuple = climbingRecordRepository.findAllClearRateAndUserAndGym(user, gym);

        Long totalCompletedCount = (Long) crTuple.get("totalCompletedCount");

        Long attemptRouteCount = (Long) crTuple.get("attemptRouteCount");

        //기록
        List<Object[]> difficulties = routeRecordRepository
            .findAllRouteRecordDifficultyAndUserAndGym(user, gym);

        List<GymDifficultyMappingInfo> difficultyList = difficulties.stream()
            .map(arr -> {
                DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndDifficulty(
                    gym, ((int) arr[CLIMEET_LEVEL]));
                Long levelCount = (Long) arr[LEVEL_COUNT];

                return GymDifficultyMappingInfo.toDTO(difficultyMapping, levelCount);
            })
            .collect(Collectors.toList());

        return ClimbingRecordUserAndGymStatisticsDetailInfo.toDTO(
            userId,
            totalCompletedCount,
            attemptRouteCount,
            difficultyList
        );
    }
}