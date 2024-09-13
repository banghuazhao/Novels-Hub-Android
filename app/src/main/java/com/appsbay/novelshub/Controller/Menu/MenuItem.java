package com.appsbay.novelshub.Controller.Menu;

import android.graphics.drawable.Drawable;

public class MenuItem {
    String itemName;
    Drawable icon;

    public MenuItem(String itemName, Drawable icon) {
        this.itemName = itemName;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIconName(Drawable icon) {
        this.icon = icon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
