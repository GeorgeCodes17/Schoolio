package com.Academical.objects;

import com.google.gson.annotations.SerializedName;

public class LessonObject {
    @SerializedName("id") private final int id;
    @SerializedName("name") private final String name;
    @SerializedName("year") private final YearObject year;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("deleted_at") private String deletedAt;

    public LessonObject(int id, String name, YearObject year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public YearObject getYear() {
        return year;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }
}
