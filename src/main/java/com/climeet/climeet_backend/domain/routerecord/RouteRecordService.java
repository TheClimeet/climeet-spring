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
        Long oldRouteId = routeRecord.getRoute().getId();
        int oldAttemptTime = routeRecord.getAttemptCount();
        Boolean oldIsCompleted = routeRecord.getIsCompleted();

        //각 필드의 새값들
        Long newRouteId = updateRouteRecord.getRouteId();
        Integer newAttemptTime = updateRouteRecord.getAttemptCount();
        Boolean newIsComplete = updateRouteRecord.getIsComplete();

        if (newRouteId != null) {
            oldRouteId = newRouteId;
        }

        if (newAttemptTime != null) {
            climbingRecord.setAttemptCount(newAttemptTime - oldAttemptTime);
            oldAttemptTime = newAttemptTime;
        }

        if (newIsComplete != null && newIsComplete != oldIsCompleted) {
            int difficulty = routeRecord.getRoute().getDifficultyMapping().getDifficulty();
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
        Route route = routeRepository.findById(oldRouteId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        climbingRecord.attemptRouteCountUp();

        routeRecord.update(oldAttemptTime, oldIsCompleted, route);

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

        int difficulty = routeRecord.getRoute().getDifficultyMapping().getDifficulty();

        if(climbingRecord.getHighDifficulty() < difficulty){
            climbingRecord.setHighDifficulty(difficulty);
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