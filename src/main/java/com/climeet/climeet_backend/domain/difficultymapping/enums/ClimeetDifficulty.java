package com.climeet.climeet_backend.domain.difficultymapping.enums;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Arrays;

public enum ClimeetDifficulty {
    VB("VB", 0),
    V1("V1", 1),
    V2("V2", 2),
    V3("V3", 3),
    V4("V4", 4),
    V5("V5", 5),
    V6("V6", 6),
    V7("V7", 7),
    V8("V8", 8),
    V9("V9+", 9),
    C("C", 10);

    private String stringValue;
    private int intValue;

    ClimeetDifficulty(String stringValue, int intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
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

    public static ClimeetDifficulty findByInt(int climeetDifficulty){
        return Arrays.stream(ClimeetDifficulty.values())
            .filter(difficulty -> difficulty.getIntValue() == climeetDifficulty)
            .findFirst()
            .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_DIFFICULTY));
    }
}
