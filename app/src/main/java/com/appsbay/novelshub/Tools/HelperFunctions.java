package com.appsbay.novelshub.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

public class HelperFunctions {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static boolean isFirstRun(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("First Run", Context.MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
            return true;
        }
        return false;
    }
}
