package com.climeet.climeet_backend.domain.climber;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class NaverUserInfo extends Oauth2UserInfo{

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public Long getID() {
        return Long.parseLong(String.valueOf(attributes.get("id")));
    }

    public String getProvider() {
        return "NAVER";
    }

    public Map<String, Object> getResponse() {
        return (Map<String, Object>) attributes.get("response");
    }

    public String getProfileImg() {
        return (String) getResponse().get("profile_image");
    }
}

