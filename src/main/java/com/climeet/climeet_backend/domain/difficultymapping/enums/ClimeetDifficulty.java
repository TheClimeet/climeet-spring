package com.climeet.climeet_backend.domain.difficultymapping.enums;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Arrays;

public enum ClimeetDifficulty {
    VB("VB", 0),
    V0("V0", 1),
    V1("V1", 2),
    V2("V2", 3),
    V3("V3", 4),
    V4("V4", 5),
    V5("V5", 6),
    V6("V6", 7),
    V7("V7", 8),
    V8("V8", 9),
    V9("V9+", 10),
    C("C", null);

    private String stringValue;
    private Integer intValue;

    ClimeetDifficulty(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Integer getIntValue() {
        return intValue;
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
