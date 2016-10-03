package com.btofindr.model;

/**
 * Created by Sherry on 31/08/2016.
 */

public class NavDrawerItem {

    private int icon;
    private String title;

    public NavDrawerItem(){};

    public NavDrawerItem(int icon, String title){
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}