package com.climeet.climeet_backend.domain.retool.gymduplicationrequest;

import com.climeet.climeet_backend.domain.retool.gymduplicationrequest.dto.GymDuplicationRequestResponse.GymDuplicationRequestSimpleInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GymDuplicationRequestController {

    private final GymDuplicationRequestService gymDuplicationRequestService;

    @GetMapping("/retool/gym-duplication-requests")
    public ResponseEntity<List<GymDuplicationRequestSimpleInfo>> getGymDuplicationRequests() {
        return ResponseEntity.ok(gymDuplicationRequestService.getGymDuplicationRequests());
    }
}