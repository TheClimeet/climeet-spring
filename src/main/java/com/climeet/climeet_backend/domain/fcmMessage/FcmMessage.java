package com.climeet.climeet_backend.domain.fcmMessage;

import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    public static class Message{
        private Notification notification;
        private String token;
    }



}
