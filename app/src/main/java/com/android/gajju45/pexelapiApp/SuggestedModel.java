package com.android.gajju45.pexelapiApp;

import androidx.core.content.ContextCompat;

public class SuggestedModel {
    int image;
    String title;
    int color_contain;

    public SuggestedModel(int image, String title , int color_contain) {
        this.image = image;
        this.title = title;
        this.color_contain = color_contain;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor_contain() {
        return color_contain;
    }

    public void setColor_contain(int color_contain) {

        this.color_contain = color_contain;
    }
}
