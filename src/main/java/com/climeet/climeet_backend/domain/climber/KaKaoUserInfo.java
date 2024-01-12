package com.climeet.climeet_backend.domain.climber;

import java.util.Map;

public class KaKaoUserInfo extends Oauth2UserInfo{
    public KaKaoUserInfo(Map<String, Object> attributes){
        super(attributes);
    }
    @Override
    public Long getID() {
        return Long.parseLong(String.valueOf(attributes.get("id")));
    }
    public Map<String, Object> getKakaoAccount(){
        return(Map<String, Object>) attributes.get("kakao_account");
    }
    public Map<String, Object> getProfile(){
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }
    public String getProfileImg() {
        return (String) getProfile().get("profile_image_url");
    }
    public String getProvider(){
        return "KAKAO";
    }
}
