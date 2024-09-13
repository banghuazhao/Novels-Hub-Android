package com.appsbay.novelshub.Controller.Menu.MoreApps;

import android.graphics.drawable.Drawable;

public class MoreApp {
    String name;
    String description;
    String packageName;
    Drawable iconDrawable;

    public MoreApp(String name, String description, String packageName, Drawable iconDrawable) {
        this.name = name;
        this.description = description;
        this.packageName = packageName;
        this.iconDrawable = iconDrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}
