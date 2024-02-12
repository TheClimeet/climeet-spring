package com.climeet.climeet_backend.domain.shorts;

import java.util.List;

public enum ShortsVisibility {
    PUBLIC, FOLLOWERS_ONLY, PRIVATE;

    public static List<ShortsVisibility> getPublicAndFollowersOnlyList() {
        return List.of(PUBLIC, FOLLOWERS_ONLY);
    }
}