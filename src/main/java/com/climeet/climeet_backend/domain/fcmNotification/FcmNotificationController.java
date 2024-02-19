package com.climeet.climeet_backend.domain.fcmNotification;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.climeet.climeet_backend.domain.fcmNotification.dto.FcmNotificationRequestDto.CreatePushNotificationRequest;
import static com.climeet.climeet_backend.domain.fcmNotification.dto.FcmNotificationRequestDto.CreateMultiplePushNotificationRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messaging")
public class FcmNotificationController {
    private final FcmNotificationService fcmNotificationService;

    @PostMapping("/push-notification-singleuser")
    @Operation(summary = "Firebase Cloud로 메시지 요청(single user)")
    public ResponseEntity<String> pushNotification(@RequestBody CreatePushNotificationRequest createPushNotificationRequest)
        throws ExecutionException, FirebaseMessagingException, InterruptedException {
        fcmNotificationService.sendSingleUser(createPushNotificationRequest);
        return ResponseEntity.ok("전송 성공");
    }

    @PostMapping("/push-notification-multipleuser")
    @Operation(summary = "Firebase Cloud로 메시지 요청(multiple user)")
    public ResponseEntity<String> pushNotificationToMultipleUser(@RequestBody CreateMultiplePushNotificationRequest createMultiplePushNotificationRequest)
        throws FirebaseMessagingException {
        fcmNotificationService.sendMultipleUser(createMultiplePushNotificationRequest);
        return ResponseEntity.ok("전송 성공");
    }

}
