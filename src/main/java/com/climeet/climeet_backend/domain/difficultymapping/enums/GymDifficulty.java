package com.climeet.climeet_backend.domain.difficultymapping.enums;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;

public enum GymDifficulty {
    하양("#FFFFFF"),
    빨강("#F34040"),
    주황("#FF9000"),
    노랑("#FDDA16"),
    초록("#63B75D"),
    하늘("#74D5FF"),
    파랑("#0094FF"),
    남색("#393FD6"),
    보라("#A259FF"),
    핑크("#FF74E9"),
    갈색("#6E4C41"),
    회색("#8B8B8B"),
    검정("#000000"),
    컴피("#000000");

    private String colorCode;

    GymDifficulty(String stringValue) {
        this.colorCode = stringValue;
    }

    public static GymDifficulty findByName(String gymDifficultyName) {
        for (GymDifficulty gymDifficulty : GymDifficulty.values()) {
            if (gymDifficulty.getColorName().equals(gymDifficultyName)) {
                return gymDifficulty;
            }
        }
        throw new GeneralException(ErrorStatus._INVALID_DIFFICULTY);
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColorName() {
        return this.toString();
    }
}
