package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Shorts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sector sector;

    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String videoUrl;

    private String thumbnailImageUrl;

    private int likeCount = 0;

    private int commentCount = 0;

    private int bookmarkCount = 0;

    private int shareCount = 0;

    private String description;

    private Boolean isSoundEnabled;

    private Boolean isPublic;
}