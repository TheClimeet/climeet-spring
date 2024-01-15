package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.PostClimbingRecordDto;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ClimbingRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym gym;

    private LocalDate climbingDate;

    private LocalTime climbingTime;

    //도전횟수
    private int totalAttemptCount;

    //완등횟수
    private int totalCompletedCount;

    //평균레벨
    private int avgDifficulty;

    public static ClimbingRecord toEntity(PostClimbingRecordDto requestDto,
        ClimbingGym climbingGym) {
        return ClimbingRecord.builder()
            .climbingDate(requestDto.getDate())
            .climbingTime(requestDto.getTime())
            .gym(climbingGym)
            .totalAttemptCount(0)
            .totalCompletedCount(0)
            .avgDifficulty(requestDto.getAvgDifficulty())
            .build();
    }


    public void update(LocalDate climbingDate, LocalTime climbingTime){
        this.climbingDate = climbingDate;
        this.climbingTime = climbingTime;
    }


    public void setAttemptCount(int count) {
        this.totalAttemptCount += count;
    }

    public void setAvgDifficulty(int avgDifficulty){
        this.avgDifficulty = avgDifficulty;
    }

    public void totalCompletedCountUp() {
        this.totalCompletedCount++;
    }

    public void totalCompletedCountDown() {
        this.totalCompletedCount--;
    }
}
