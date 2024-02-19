package com.climeet.climeet_backend.domain.FcmMessage;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.google.firebase.messaging.Message;
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
