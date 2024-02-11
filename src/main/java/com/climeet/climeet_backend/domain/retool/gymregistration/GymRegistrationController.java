package com.climeet.climeet_backend.domain.retool.gymregistration;

import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsDetailInfo;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsSimpleInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GymRegistrationController {

    private final GymRegistrationService gymRegistrationService;

    @GetMapping("/retool/gymRegistrations")
    public ResponseEntity<List<GetGymRegistrationsSimpleInfo>> getGymRegistrations() {
        return ResponseEntity.ok(gymRegistrationService.getGymRegistrations());
    }

    @GetMapping("/retool/gymRegistrations/{gymRegistrationId}")
    public ResponseEntity<GetGymRegistrationsDetailInfo> getGymRegistration(
        @PathVariable Long gymRegistrationId) {
        return ResponseEntity.ok(gymRegistrationService.getGymRegistration(gymRegistrationId));
    }
}