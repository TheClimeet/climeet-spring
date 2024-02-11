package com.climeet.climeet_backend.domain.difficultymapping.enums;

public enum GymDifficultyColor {
    WHITE("#FFFFFF"),
    RED("#F34040"),
    ORANGE("#FF9000"),
    YELLOW("#FDDA16"),
    GREEN("#63B75D"),
    SKYBLUE("#74D5FF"),
    BLUE("#0094FF"),
    DEEPBLUE("#393FD6"),
    PURPLE("#A259FF"),
    PINK("#FF74E9"),
    BROWN("#6E4C41"),
    GRAY("#8B8B8B"),
    BLACK("#000000"),
    COMPETITION("CUSTOM");

    private String colorCode;

    GymDifficultyColor(String stringValue) {
        this.colorCode = stringValue;
    }

    public String getColorCode() {
        return colorCode;
    }
}
