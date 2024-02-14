package com.climeet.climeet_backend.domain.notification;

import com.climeet.climeet_backend.domain.notification.dto.FCMNotificationRequestDto;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application-dev.yml")
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${firebase:send-api-url}")
    private String API_URL;

//    public void sendMessageTo(String targetToken, String title, String body){
//
//    }
//
//    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException{
//        FcmMessage fcmMessage = FcmMessage.builder()
//            .
//    }

//    public String sendNotificationByToken(FCMNotificationRequestDto requestDto){
//        User user = userRepository.findById(requestDto.getTargetUserId())
//            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_USER));
//
//    }

}
