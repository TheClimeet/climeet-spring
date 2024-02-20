package com.climeet.climeet_backend.domain.fcmNotification;

import lombok.Getter;

public enum NotificationType {

    UPDATE_GYM_SETTING("의 세팅이 업데이트 되었어요!", "섹터 루트 세팅 완료!"),
    UPLOAD_NEW_SHORTS("님이 새로운 숏츠를 업로드 했어요", "지금 바로 확인해보세요!"),
    END_OF_MONTH_REPORY("이번 달 운동 리포르를 확인해보세요!", "이번 달에 완등한 루트는?👀"),
    NOT_RECORD_CLIMBING_THREE_DAYS("님 혹시 오늘 안클 행클 하셨어요?", "클밋에서 기록하면 내 실력 수직 상승!"),
    NOT_RECORD_CLIMBING_SEVEN_DAYS("님 클라이밍 우리 다시 시작해보지 않으실래요?", "클밋에서 다시 만나요!"),
    NOT_RECORD_CLIMBING_FOURTHTEEN_DAYS("아, 그립다 OO님과 행클하던 그 시절", "우리 클밋에서 다시 만나요!"),
    HEARTS_MY_SHORTS("님 외 n명이 내 숏츠에 좋아요를 눌렀어요", "다른 클라이머들의 반응을 확인해보세요"),
    PARENT_COMMENT_MY_SHORTS("님이 내 숏츠에 댓글을 남겼어요", ""),
    CHILD_COMMENT_MY_SHORTS("님이 내 숏츠에 답글을 남겼어요", ""),
    BEST_CLIMBER_UPDATE("BEST 순위가 업데이트 되었어요!", "지금 바로 순위를 확인해보세요!");


    @Getter
    private String title;
    @Getter
    private String message;

    NotificationType(String title, String message){
        this.title = title;
        this.message = message;
    }

//    public NotificationType updateNotificationUpdateGymSetting(String gymName, String sectorName){
//        //NotificationType notificationType = NotificationType(gymName+NotificationType.g)
//    }
}
