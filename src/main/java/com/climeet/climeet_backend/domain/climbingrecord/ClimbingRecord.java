package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
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
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("0")
    private Integer totalAttemptCount;

    //완등횟수
    @ColumnDefault("0")
    private Integer totalCompletedCount;

    //평균레벨
    @ColumnDefault("0")
    private Integer avgDifficulty;

    public static ClimbingRecord toEntity(ClimbingRecordRequestDto requestDto,
        ClimbingGym climbingGym) {
        return ClimbingRecord.builder()
            .climbingDate(requestDto.getDate())
            .climbingTime(requestDto.getTime())
            .gym(climbingGym)
            .build();
    }


    public void attemptCountUp(int count) {
        this.totalAttemptCount += count;
    }

    public void totalCompletedCountUp() {
        this.totalCompletedCount++;
    }
}
