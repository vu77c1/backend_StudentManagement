package org.example.final_project.util;

public enum Grade {
    A("A"),
    B_PLUS("B+"),
    B("B"),
    C_PLUS("C+"),
    C("C"),
    D("D"),
    F("F");

    private final String displayName;

    Grade(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Grade fromDisplayName(String displayName) {
        for (Grade grade : Grade.values()) {
            if (grade.displayName.equals(displayName)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}

