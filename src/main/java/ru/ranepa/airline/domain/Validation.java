package ru.ranepa.airline.domain;

public final class Validation {
    private Validation() { }

    public static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + ": значение не должно быть пустым");
        }
        return value.trim();
    }
}
