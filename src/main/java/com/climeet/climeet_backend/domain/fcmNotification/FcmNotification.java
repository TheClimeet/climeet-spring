package com.climeet.climeet_backend.domain.fcmNotification;

import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmNotification {
    private boolean validateOnly;
    private Message message;

    public static class Message{
        private Notification notification;
        private String token;
    }



}
