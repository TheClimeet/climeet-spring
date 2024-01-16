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


    public String getID() {
        String id = (String) getResponse().get("id");
        if (id != null && !id.isEmpty()) {
            return id;
        } else {
            throw new RuntimeException("ID가 없거나 비어있습니다.");
        }
    }
    public Map<String, Object> getResponse() {
        Object response = attributes.get("response");
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        } else {
            throw new RuntimeException("'response'가 Map 형식이 아닙니다.");
        }
    }


    public String getProfileImg() {
        Map<String, Object> response = getResponse();
        Object profileImage = response.get("profile_image");
        System.out.println(profileImage);
        if (profileImage instanceof String) {
            return (String) profileImage;
        } else {
            throw new RuntimeException("'profile_image'가 String 형식이 아닙니다.");
        }
    }
}

