package xxx.com.redassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MZIA(527633405@qq.com) on 2016/11/18 23:09
 */
public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("redassistant", Context.MODE_PRIVATE);
    }

    /**
     * 是否帮助抢红包
     *
     * @param context Context
     * @return true即为帮助抢红包
     */
    public static boolean isHelpGradRedBag(Context context) {
        return getSharedPreferences(context).getBoolean("HelpGradRedBag", false);
    }

    /**
     * 设置是否帮助抢红包
     *
     * @param context        Context
     * @param helpGradRedBag 帮助抢红包
     */
    public static void setHelpGradRedBag(Context context, boolean helpGradRedBag) {
        getSharedPreferences(context).edit().putBoolean("HelpGradRedBag", helpGradRedBag).apply();
    }

    /**
     * 是否自动报读通知栏消息
     *
     * @param context Context
     * @return true即为自动报读
     */
    public static boolean isAutoRead(Context context) {
        return getSharedPreferences(context).getBoolean("AutoRead", false);
    }

    /**
     * 设置是否自动报读通知栏消息
     *
     * @param context  Context
     * @param autoRead 是否自动报读
     */
    public static void setAutoRead(Context context, boolean autoRead) {
        getSharedPreferences(context).edit().putBoolean("AutoRead", autoRead).apply();
    }

    /**
     * 获取前面一个Activity
     *
     * @return 前面一个Activity
     */
    public static String getPreActivity(Context context) {
        return getSharedPreferences(context).getString("PreActivity", "");
    }

    /**
     * 保存前面一个
     *
     * @param context Context
     * @param clazz   前面一个类
     */
    public static void setPreActivity(Context context, String clazz) {
        getSharedPreferences(context).edit().putString("PreActivity", clazz).apply();
    }
}
