package com.appsbay.novelshub.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class StoreHelper {

    public static void goToMarket(Context context, String packageName) {

        if (isAvilible(context, "com.android.vending")) {
            // google play
            com.appsbay.novelshub.Tools.StoreHelper.goToGoogleMarket(context, packageName);
        } else if (isAvilible(context, "com.sec.android.app.samsungapps")) {
            // samsung app store
            com.appsbay.novelshub.Tools.StoreHelper.goToSamsungappsMarket(context, packageName);
        } else if (isAvilible(context, "com.amazon.venezia")) {
            // amazon app
            com.appsbay.novelshub.Tools.StoreHelper.goToAmazonMarket(context, packageName);
        } else if (isAvilible(context, "com.xiaomi.market")) {
            // amazon app
            com.appsbay.novelshub.Tools.StoreHelper.goToXiaoMiMarket(context, packageName);
        } else if (isAvilible(context, "com.oppo.market")) {
            // amazon app
            com.appsbay.novelshub.Tools.StoreHelper.goToOPPOMarket(context, packageName);
        } else {
            com.appsbay.novelshub.Tools.StoreHelper.goToGoogleMarket(context, packageName);
        }

    }


    public static boolean isAvilible(Context context, String installerPackageName) {

        // 获取packagemanager

        final PackageManager packageManager = context.getPackageManager();

        String installer = packageManager.getInstallerPackageName(context.getPackageName());

        if (installer == null) {
            return false;
        }

        Log.d("installer:", installer);

        // 判断pName中是否有目标程序的包名，有true，没有false

        return installer.contains(installerPackageName);

    }

    /**
     * 跳转到谷歌应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToGoogleMarket(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.setPackage("com.android.vending");//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {//没有应用市场，通过浏览器跳转到Google Play
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent2);
                } else {
                    //没有Google Play 也没有浏览器
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到三星应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToSamsungappsMarket(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName);
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
            goToMarket.setData(uri);
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转Amazon应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToAmazonMarket(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("amzn://apps/android?p=" + packageName);
            Intent goToMarket = new Intent();
            goToMarket.setData(uri);
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转小米应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToXiaoMiMarket(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.setPackage("com.xiaomi.market");//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转oppo应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToOPPOMarket(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.setPackage("com.oppo.market");//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}