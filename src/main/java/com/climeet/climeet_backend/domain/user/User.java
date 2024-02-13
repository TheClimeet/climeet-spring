package com.climeet.climeet_backend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    protected String profileName;

    protected String profileImageUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isAllowFollowNotification;

    private Boolean isAllowLikeNotification;

    private Boolean isAllowCommentNotification;

    private Boolean isAllowAdNotification;

    @ColumnDefault("0L")
    private Long followerCount = 0L;

    @ColumnDefault("0L")
    private Long followingCount = 0L;

    @ColumnDefault("0")
    private int thisWeekCompleteCount = 0;

    @ColumnDefault("0L")
    private Long thisWeekTotalClimbingTime = 0L;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "access_token")
    private String accessToken;

    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;

    @NotNull
    private String fcmToken;

    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateFCMToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void updateNotification(boolean isAllowFollowNotification, boolean isAllowLikeNotification, boolean isAllowCommentNotification, boolean isAllowAdNotification){
        this.isAllowFollowNotification = isAllowFollowNotification;
        this.isAllowLikeNotification = isAllowLikeNotification;
        this.isAllowCommentNotification = isAllowCommentNotification;
        this.isAllowAdNotification = isAllowAdNotification;
    }

    public void thisWeekTotalClimbingTimeUp(Long sec){
        this.thisWeekTotalClimbingTime += sec;
    }

    public void thisWeekTotalClimbingTimeDown(Long sec){
        this.thisWeekTotalClimbingTime -= sec;
    }

    public void increaseFollwerCount(){
        this.followerCount++;
    }

    public void decreaseFollwerCount(){
        this.followerCount++;
    }

}