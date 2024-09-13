package com.appsbay.novelshub.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class MyColor {

    static public int getBackgroundColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        switch (backgroundColorName) {
            case "default":
                return Color.parseColor("#F6EFDE");
            case "white":
                return Color.parseColor("#FFFEFF");
            case "green":
                return Color.parseColor("#CCEFCF");
            case "dark":
                return Color.parseColor("#131316");
            default:
                return Color.parseColor("#F6EFDE");
        }

    }

    static public int getActionBarColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        switch (backgroundColorName) {
            case "default":
                return Color.parseColor("#F6EFDE");
            case "white":
                return Color.parseColor("#FFFEFF");
            case "green":
                return Color.parseColor("#CCEFCF");
            case "dark":
                return Color.parseColor("#131316");
            case "bg":
                return Color.parseColor("#FEE4D9");
            case "bg1":
                return Color.parseColor("#FBE0D7");
            case "bg2":
                return Color.parseColor("#F59D97");
            case "bg3":
                return Color.parseColor("#8DA9DC");
            case "bg4":
                return Color.parseColor("#EDCCCE");
            case "bg5":
                return Color.parseColor("#E6D0B4");
            default:
                return Color.parseColor("#F6EFDE");
        }
    }

    static public int getBottomBarColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        switch (backgroundColorName) {
            case "default":
                return Color.parseColor("#F6EFDE");
            case "white":
                return Color.parseColor("#FFFEFF");
            case "green":
                return Color.parseColor("#CCEFCF");
            case "dark":
                return Color.parseColor("#131316");
            case "bg":
                return Color.parseColor("#E1BA96");
            case "bg1":
                return Color.parseColor("#E1BA96");
            case "bg2":
                return Color.parseColor("#F59D97");
            case "bg3":
                return Color.parseColor("#E8BDDC");
            case "bg4":
                return Color.parseColor("#9FBDC5");
            case "bg5":
                return Color.parseColor("#F0C878");
            default:
                return Color.parseColor("#F6EFDE");
        }
    }

    static public int getTitleTextColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        if (backgroundColorName.equals("dark")) {
            return Color.parseColor("#EFF0F2");
        } else {
            return Color.parseColor("#0E141E");
        }
    }

    static public int getDetailTextColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        if (backgroundColorName.equals("dark")) {
            return Color.parseColor("#8C8C8D");
        } else {
            return Color.parseColor("#656565");
        }
    }

    static public int getButtonTintColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        if (backgroundColorName.equals("dark")) {
            return Color.parseColor("#B3B4B6");
        } else {
            return Color.parseColor("#5C636D");
        }
    }

    static public int getSeparatorColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");
        if (backgroundColorName.equals("dark")) {
            return Color.parseColor("#323232");
        } else {
            return Color.parseColor("#BFBFBE");
        }
    }
}
