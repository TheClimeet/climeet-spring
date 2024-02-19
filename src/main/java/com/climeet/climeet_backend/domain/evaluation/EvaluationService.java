package com.climeet.climeet_backend.domain.evaluation;

import com.climeet.climeet_backend.domain.evaluation.dto.EvaluationRequestDto.CreateEvaluation;
import com.climeet.climeet_backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public ResponseEntity<String> createEvaluation(User user, CreateEvaluation requestDto){
        evaluationRepository.save(Evaluation.toEntity(user, requestDto));
        return ResponseEntity.ok("소중한 리뷰 감사합니다! \n더 좋은 서비스 만들겠습니다:)");
    }
}
