package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.UpdateRouteRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RouteRecordService {

    private final RouteRecordRepository routeRecordRepository;
    private final RouteRepository routeRepository;

    /**
     * 루트 기록 생성
     */
    @Transactional
    public ResponseEntity<String> addRouteRecord(User user, CreateRouteRecord requestDto,
        ClimbingRecord climbingRecord) {

        Route route = routeRepository.findById(requestDto.getRouteId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        routeRecordRepository.save(RouteRecord.toEntity(user, requestDto, climbingRecord, route));

        int count = requestDto.getAttemptCount();

        climbingRecord.setAttemptCount(count);
        climbingRecord.attemptRouteCountUp();
        route.thisWeekSelectionCountUp();

        if (requestDto.getIsCompleted()) {
            climbingRecord.totalCompletedCountUp();
        }

        climbingRecord.setHighDifficulty(Math.max(route.getDifficultyMapping().getDifficulty(),
            climbingRecord.getHighDifficulty()));

        return ResponseEntity.ok("루트 기록 성공");

    }

    /**
     * 루트 간편기록 전체 조회
     */
    public List<RouteRecordSimpleInfo> getRouteRecordList(User user) {

        List<RouteRecord> recordList = routeRecordRepository.findAllByUser(user);

        if (recordList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_RECORD);
        }

        return recordList.stream()
            .map(RouteRecordSimpleInfo::new)
            .toList();

    }

    /**
     * 루트 간편기록 id로 조회
     */
    public RouteRecordSimpleInfo getRouteRecord(User user, Long id) {

        RouteRecord routeRecord = routeRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._ROUTE_RECORD_NOT_FOUND));

        if (!user.getId().equals(routeRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }
        return new RouteRecordSimpleInfo(routeRecord);
    }


    /**
     * 루트기록 수정
     */
    @Transactional
    public RouteRecordSimpleInfo updateRouteRecord(User user, Long id,
        UpdateRouteRecord updateRouteRecord) {

        RouteRecord routeRecord = routeRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._ROUTE_RECORD_NOT_FOUND));

        if (!user.getId().equals(routeRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }

        ClimbingRecord climbingRecord = routeRecord.getClimbingRecord();

        //각 필드의 기존값들
        int oldAttemptCount = routeRecord.getAttemptCount();
        Boolean oldIsCompleted = routeRecord.getIsCompleted();

        //각 필드의 새값들
        Integer attemptCount = updateRouteRecord.getAttemptCount();
        Boolean newIsComplete = updateRouteRecord.getIsComplete();

        if (attemptCount != null) {
            climbingRecord.setAttemptCount(attemptCount - oldAttemptCount);
            oldAttemptCount = attemptCount;
        }

        if (newIsComplete != null && newIsComplete != oldIsCompleted) {
            int difficulty = routeRecord.getDifficulty();
            int oldAvgDifficulty = climbingRecord.getAvgDifficulty();
            int oldCount = climbingRecord.getTotalCompletedCount();

            if (newIsComplete) {
                climbingRecord.setAvgDifficulty(
                    newAvgDifficulty(difficulty, oldAvgDifficulty, oldCount, true));
                climbingRecord.totalCompletedCountDown();
            } else {
                climbingRecord.setAvgDifficulty(
                    newAvgDifficulty(difficulty, oldAvgDifficulty, oldCount, false));
                climbingRecord.totalCompletedCountUp();
            }
            oldIsCompleted = newIsComplete;
        }

        climbingRecord.attemptRouteCountUp();

        routeRecord.update(oldAttemptCount, oldIsCompleted);

        return new RouteRecordSimpleInfo(routeRecord);
    }

    /**
     * 루트기록 삭제
     */
    @Transactional
    public ResponseEntity<String> deleteRouteRecord(User user, Long id) {
        RouteRecord routeRecord = routeRecordRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus._ROUTE_RECORD_NOT_FOUND));

        if (!user.getId().equals(routeRecord.getUser().getId())) {
            throw new GeneralException(ErrorStatus._INVALID_MEMBER);
        }

        ClimbingRecord climbingRecord = routeRecord.getClimbingRecord();

        int difficulty = routeRecord.getDifficulty();

        List<RouteRecord> routeRecordList = routeRecordRepository.findAllByClimbingRecordId(
            climbingRecord.getId());


        List<Integer> difficulties = routeRecordList.stream()
            .filter(record -> !record.getRoute().getId()
                .equals(routeRecord.getRoute().getId()))
            .map(RouteRecord::getDifficulty)
            .toList();

        // difficulties 리스트에 difficulty 이상인 값이 있는지 확인
        Optional<Integer> largerOrEqual = difficulties.stream()
            .filter(d -> d >= difficulty)
            .findFirst();

        // difficulty 이상인 값이 없는 경우, 있는 경우라면 climbingRecord를 업데이트 하지 않아도 됨.
        if (largerOrEqual.isEmpty()) {
            // difficulties 리스트가 비어있지 않은 경우라면 climbingRecord에 남은 routeRecord 기록들이 있다는 뜻.
            if (!difficulties.isEmpty()) {
                climbingRecord.setHighDifficulty(difficulties.get(difficulties.size() - 1));
            }
        }

        //climbingRecord의 평균 업데이트
        climbingRecord.setAvgDifficulty(
            newAvgDifficulty(difficulty, climbingRecord.getAvgDifficulty(),
                climbingRecord.getTotalCompletedCount(), false));

        //climbingRecord의 총 완등 횟수 업데이트
        climbingRecord.totalCompletedCountDown();

        //climbingRecord의 시도 횟수 업데이트
        climbingRecord.setAttemptCount(-routeRecord.getAttemptCount());

        routeRecord.getRoute().thisWeekSelectionCountDown();

        climbingRecord.attemptRouteCountDown();

        routeRecordRepository.delete(routeRecord);
        return ResponseEntity.ok("루트기록이 삭제되었습니다.");
    }

    private int newAvgDifficulty(int routeDifficulty, int oldAvgDifficulty, int oldCount,
        boolean isPlus) {
        if (isPlus) {
            return (int) (((oldCount * oldAvgDifficulty) + routeDifficulty) / (oldCount + 1));
        } else {
            if (oldCount <= 1) {
                return 0;
            } else {
                return (int) (((oldCount * oldAvgDifficulty) - routeDifficulty) / (oldCount - 1));
            }
        }
    }
}