package com.climeet.climeet_backend.domain.evaluation;

import com.climeet.climeet_backend.domain.evaluation.dto.EvaluationRequestDto.CreateEvaluation;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Evaluation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @NotNull
    @Column(length = 1000)
    private String content;

    @NotNull
    private Float rating;

    public static Evaluation toEntity(User user, CreateEvaluation requestDto){
        return Evaluation.builder()
            .user(user)
            .content(requestDto.getContent())
            .rating(requestDto.getRating())
            .build();
    }


}
