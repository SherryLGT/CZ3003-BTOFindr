package com.btofindr.model;

/**
 * This describes a navigation drawer item.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
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