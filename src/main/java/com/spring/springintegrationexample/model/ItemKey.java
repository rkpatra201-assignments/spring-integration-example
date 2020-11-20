package com.spring.springintegrationexample.model;

import java.io.Serializable;
import java.util.Objects;

public class ItemKey implements Serializable {

    private String formattedDate;
    private String category;
    private int hash;

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

    public int getHash() {
        if (this.hash == 0)
            this.hash = this.hashCode();
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemKey itemKey = (ItemKey) o;
        return Objects.equals(formattedDate, itemKey.formattedDate) &&
                Objects.equals(category, itemKey.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formattedDate, category);
    }

    @Override
    public String toString() {
        return "ItemKey{" +
                "formattedDate='" + formattedDate + '\'' +
                ", category='" + category + '\'' +
                ", hash=" + getHash() +
                '}';
    }
}
