package com.climeet.climeet_backend.domain.difficultymapping.enums;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Arrays;

public enum ClimeetDifficulty {
    VB("VB", 0, "#FFFFFF"),
    V0("V0", 1, "#FFFFFF"),
    V1("V1", 2, "#F34040"),
    V2("V2", 3, "#FF9000"),
    V3("V3", 4, "#FDDA16"),
    V4("V4", 5, "#63B75D"),
    V5("V5", 6, "#74D5FF"),
    V6("V6", 7, "#0094FF"),
    V7("V7", 8, "#393FD6"),
    V8("V8", 9, "#A259FF"),
    V9("V9+", 10, "#8B8B8B"),
    C("C", null, "#000000");

    private String stringValue;
    private Integer intValue;
    private String colorCode;

    ClimeetDifficulty(String stringValue, Integer intValue, String colorCode) {
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.colorCode = colorCode;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }
    public String getColorCode() {
        return colorCode;
    }

    public static ClimeetDifficulty findByString(String climeetDifficultyName) {
        for (ClimeetDifficulty difficulty : ClimeetDifficulty.values()) {
            if (difficulty.getStringValue().equals(climeetDifficultyName)) {
                return difficulty;
            }
        }
        throw new GeneralException(ErrorStatus._INVALID_DIFFICULTY);
    }

    public static ClimeetDifficulty findByInt(Integer climeetDifficulty){
        return Arrays.stream(ClimeetDifficulty.values())
            .filter(difficulty -> difficulty.getIntValue() == climeetDifficulty)
            .findFirst()
            .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_DIFFICULTY));
    }
}
