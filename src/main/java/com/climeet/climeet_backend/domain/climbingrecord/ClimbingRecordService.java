package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
import com.climeet.climeet_backend.global.response.ApiResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.List;
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
            ClimbingGym climbingGym = gymRepository.findByName(requestDto.getClimbingGymName());
            //먼저 암장기록 저장
            ClimbingRecord climbingRecord = climbingRecordRepository
                .save(ClimbingRecord.toEntity(requestDto, climbingGym));

            // 루트기록 리퀘스트 돌면서 루트 리퀘스트 저장
            requestDto.getRouteRecordRequestDtoList().stream()
                .map(dto -> routeRecordService.addRouteRecord(dto, climbingRecord));

            return ApiResponse.onSuccess("클라이밍 기록생성 성공");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    public List<ClimbingRecordSimpleInfo> getClimbingRecordSimpleInfoList() {
        try {
            List<ClimbingRecord> recordList = climbingRecordRepository.findAll();
            return recordList.stream()
                .map(ClimbingRecordSimpleInfo::new)
                .toList();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    // TODO: 2024/01/04 운동기록 id로 조회(하나) & date로 찾기(여러 개)
    public ClimbingRecordSimpleInfo getClimbingRecord(Long id) {
        return new ClimbingRecordSimpleInfo();
    }
}