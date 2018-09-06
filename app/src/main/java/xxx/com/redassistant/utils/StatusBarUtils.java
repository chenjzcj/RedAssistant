package xxx.com.redassistant.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;

import xxx.com.redassistant.R;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/2/19 0019
 * 状态栏工具类
 */
public class StatusBarUtils {

    private StatusBarUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取状态栏高度,建议使用此方法更精准
     *
     * @param context 上下文
     * @return 通知栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 是否含有状态栏
     *
     * @param activity 需要判断的Activity
     * @return true为包含状态栏
     */
    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 将状态栏设置成透明状态(沉浸式状态栏的实现,注意系统一定是在4.4以上)
     * http://www.eoeandroid.com/thread-594710-1-1.html?_dsign=72a7ee25
     * https://github.com/jgilfelt/SystemBarTint
     * 详细分析 : http://www.jianshu.com/p/f8374d6267ef
     *
     * @param on         是否设置成透明状态
     * @param activity   activity
     * @param colorResId 颜色资源id
     */
    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Activity activity, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);

            //设置状态栏和底下的导航栏的颜色
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource((colorResId != 0) ? colorResId : R.color.colorAccent);//通知栏所需颜色
            tintManager.setNavigationBarTintResource((colorResId != 0) ? colorResId : R.color.colorAccent);
        }
    }

}
