package com.Academical.exceptions;

public class LessonScheduleException extends Exception {
    public LessonScheduleException(String message) {
        super(message);
    }

    public LessonScheduleException(Exception message) {
        super(message);
    }
}
