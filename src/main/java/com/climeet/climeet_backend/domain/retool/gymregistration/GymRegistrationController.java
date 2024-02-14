package com.climeet.climeet_backend.domain.retool.gymregistration;

import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationRequest.PatchGymRegistrationReq;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsDetailInfo;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsSimpleInfo;
import jakarta.mail.MessagingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/retool/gymRegistrations/{gymRegistrationId}")
    public ResponseEntity<String> changeGymRegistrations(@PathVariable Long gymRegistrationId,
        @RequestBody PatchGymRegistrationReq patchGymRegistrationReq
    ) throws MessagingException {
        gymRegistrationService.changeGymRegistrations(gymRegistrationId, patchGymRegistrationReq);
        return ResponseEntity.ok("승인 완료되었습니다.");
    }
}