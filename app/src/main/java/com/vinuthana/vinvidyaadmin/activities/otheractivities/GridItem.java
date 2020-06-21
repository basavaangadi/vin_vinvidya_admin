package com.vinuthana.vinvidyaadmin.activities.otheractivities;

public class GridItem {
    private String image;
    private String title;

    public GridItem(String image) {
        this.image = image;
    }

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}