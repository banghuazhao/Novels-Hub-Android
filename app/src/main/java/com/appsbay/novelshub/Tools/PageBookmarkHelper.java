package com.appsbay.novelshub.Tools;

import android.content.Context;
import android.content.SharedPreferences;

public class PageBookmarkHelper {

    public static float getBookMark(Context context, String bookName, String chapterNumber, String chapterName) {
        String pagerBookmarkKey = "bookName: " + bookName + ", " + "chapterNumber: " + chapterNumber + ", "  + "chapterName: " + chapterName;
        SharedPreferences preferences = context.getSharedPreferences(pagerBookmarkKey, Context.MODE_PRIVATE);
        return preferences.getFloat(pagerBookmarkKey, 0);
    }

    public static void setBookMark(Context context, String bookName, String chapterNumber, String chapterName, float position) {
        String pagerBookmarkKey = "bookName: " + bookName + ", " + "chapterNumber: " + chapterNumber + ", "  + "chapterName: " + chapterName;
        SharedPreferences preferences = context.getSharedPreferences(pagerBookmarkKey, Context.MODE_PRIVATE);
        preferences.edit().putFloat(pagerBookmarkKey, position).commit();
    }
}
