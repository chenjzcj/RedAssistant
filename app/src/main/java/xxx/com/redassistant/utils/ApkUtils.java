package xxx.com.redassistant.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by MZIA(527633405@qq.com) on 2015/1/20 0020 11:12
 * 与apk相关的工具类(应用安装,卸载等)
 */
public class ApkUtils {
    private static String TAG = ApkUtils.class.getSimpleName();

    /**
     * 安装指定apk文件
     *
     * @param file .apk安装文件
     */
    public static void installApk(Context context, File file) {
        installApk(context, Uri.fromFile(file));
    }

    /**
     * 安装指定文件路径的apk文件
     *
     * @param path apk的路径
     */
    public static void installApk(Context context, String path) {
        installApk(context, Uri.fromFile(new File(path)));
    }

    /**
     * 安装apk包
     *
     * @param uri Uri
     */
    public static void installApk(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 根据包名卸载手机中的应用
     *
     * @param pkgName 指定应用包名
     */
    public static void uninstallApk(Context context, String pkgName) {
        if (isPackageExist(context, pkgName)) {
            Uri packageURI = Uri.parse("package:" + pkgName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 判断指定包名的应用是否存在
     *
     * @param context 上下文
     * @param pkgName 指定包名
     * @return true为存在
     */
    public static boolean isPackageExist(Context context, String pkgName) {
        try {
            PackageInfo pckInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            if (pckInfo != null) return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * 检查指定apk是否已经安装
     *
     * @param context 上下文
     * @param pkgName apk包名
     * @return true为已经安装了
     */
    public static boolean isAppInstalled(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            // 捕捉到异常,说明未安装
            installed = false;
        }
        return installed;
    }

    /**
     * 通过包名获取应用的入口Intent
     *
     * @param context Context
     * @param pkgname 应用包名
     * @return 跳转的应用入口Intent
     */
    public static Intent getLaunchIntentForPackage(Context context, String pkgname) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(pkgname);
    }

    /**
     * 通过packagename启动应用
     *
     * @param context Context
     * @param pkgname 应用包名
     */
    public static void startAPPFromPackageName(Context context, String pkgname) {
        Intent intent = getLaunchIntentForPackage(context, pkgname);
        if (intent != null) context.startActivity(intent);
    }


    /**
     * 在应用市场中打开App
     *
     * @param context Context
     */
    public static void openAppInMarket(Context context) {
        String pckName = context.getPackageName();
        try {
            gotoMarket(context, pckName);
        } catch (Exception ex) {
            try {
                String otherMarketUri = "http://market.android.com/details?id=" + pckName;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherMarketUri));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据包名打开应用市场
     *
     * @param context Context
     * @param pck     包名
     */
    public static void gotoMarket(Context context, String pck) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        context.startActivity(intent);
    }

    /**
     * 根据包名打开谷歌应用市场
     *
     * @param context Context
     * @param pck     包名
     * @return true为打开成功
     */
    public static boolean gotoGoogleMarket(Context context, String pck) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.android.vending");
            intent.setData(Uri.parse("market://details?id=" + pck));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
