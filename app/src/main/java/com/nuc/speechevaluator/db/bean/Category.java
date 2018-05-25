package com.nuc.speechevaluator.db.bean;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;

public class Category extends RealmObject implements Serializable {

    private String id;
    private String title;
    private String message;

    public static Category newCategory(String title) {
        Category category = new Category();
        category.setTitle(title);
        category.setId(UUID.randomUUID().toString());
        return category;
    }

    public String getId() {
        return id;
    }

    public Category setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Category setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Category setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
