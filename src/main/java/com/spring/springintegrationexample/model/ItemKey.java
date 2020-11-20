package com.spring.springintegrationexample.model;

public class ItemKey {

    private String formattedDate;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    @Override
    public String toString() {
        return "ItemKey{" +
                "formattedDate='" + formattedDate + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
