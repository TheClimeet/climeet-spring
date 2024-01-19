package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isAllowFollowNotification;

    private Boolean isAllowLikeNotification;

    private Boolean isAllowCommentNotification;

    private Boolean isAllowAdNotification;

    private Long followerCount = 0L;

    private Long followingCount = 0L;

    public void updateNotification(boolean isAllowFollowNotification, boolean isAllowLikeNotification, boolean isAllowCommentNotification, boolean isAllowAdNotification){
        this.isAllowFollowNotification = isAllowFollowNotification;
        this.isAllowLikeNotification = isAllowLikeNotification;
        this.isAllowCommentNotification = isAllowCommentNotification;
        this.isAllowAdNotification = isAllowAdNotification;
    }

}