package com.climeet.climeet_backend.domain.evaluation;

import com.climeet.climeet_backend.domain.evaluation.dto.EvaluationRequestDto.CreateEvaluation;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Evaluation", description = "클밋 운영진에게 전달하는 평가 및 리뷰")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    @Operation(summary = "801 [훈]")
    public ResponseEntity<String> createEvaluation(@CurrentUser User user, @RequestBody
    CreateEvaluation requestDto) {
        return evaluationService.createEvaluation(user, requestDto);
    }


}
