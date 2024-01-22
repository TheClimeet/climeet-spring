package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Map;

public class NaverUserInfo extends Oauth2UserInfo{

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }


    public String getId() {
        String id = (String) getResponse().get("id");
        if (id == null || id.isEmpty()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return id;
    }
    public Map<String, Object> getResponse() {
        Object response = attributes.get("response");
        if (!(response instanceof Map)) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return (Map<String, Object>) response;
    }

    public String getProfileImg() {
        Map<String, Object> response = getResponse();
        Object profileImage = response.get("profile_image");
        if (!(profileImage instanceof String)) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return (String) profileImage;
    }


}

