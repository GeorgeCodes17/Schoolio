package com.Academical.objects;

public record LessonItem(String text, LessonObject lessonObject) {
    @Override
    public String toString() {
        return text;
    }
}
