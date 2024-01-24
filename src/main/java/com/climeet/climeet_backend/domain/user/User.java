package com.climeet.climeet_backend.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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

    private Long followerCount = 0L;

    private Long followingCount = 0L;

    private int thisWeekCompleteCount = 0;

    private Long thisWeekTotalClimbingTime = 0L;

    private int thisWeekHighDifficulty = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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

}