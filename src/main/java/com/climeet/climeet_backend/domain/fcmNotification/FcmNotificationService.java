package com.climeet.climeet_backend.domain.fcmNotification;

import com.climeet.climeet_backend.domain.fcmNotification.dto.FcmNotificationRequestDto.CreateMultiplePushNotificationRequest;
import com.climeet.climeet_backend.domain.fcmNotification.dto.FcmNotificationRequestDto.CreatePushNotificationRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application-dev.yml")
public class FcmNotificationService {
    private final UserRepository userRepository;

    @Value("${firebase:send}")
    private String API_URL;


    public void sendSingleUser(CreatePushNotificationRequest createPushNotificationRequest)
        throws FirebaseMessagingException{
        User user = userRepository.findById(createPushNotificationRequest.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        Message message = Message.builder()
            .setToken(user.getFcmToken())
            .putData("title", createPushNotificationRequest.getTitle())
            .putData("body", createPushNotificationRequest.getMessage())
            .build();
        String response = FirebaseMessaging.getInstance().send(message);
    }

    public void sendMultipleUser(CreateMultiplePushNotificationRequest createMultiplePushNotificationRequest)
        throws FirebaseMessagingException {
        List<String> registrationToken = createMultiplePushNotificationRequest.getUserIdList().stream()
            .filter(userId-> userRepository.findById(userId).isPresent())
            .map(userId -> userRepository.findById(userId).get().getFcmToken()).toList();

        MulticastMessage message = MulticastMessage.builder()
            .putData("title" ,createMultiplePushNotificationRequest.getTitle())
            .putData("body", createMultiplePushNotificationRequest.getMessage())
            .addAllTokens(registrationToken)
            .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
    }




}
