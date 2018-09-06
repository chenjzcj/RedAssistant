package xxx.com.redassistant.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import xxx.com.redassistant.R;


public class BamToast extends Toast {

    /**
     * Toast颜色_黑色
     */
    public static final int COLOR_BLACK = 1;
    /**
     * Toast颜色_棕色
     */
    public static final int COLOR_BROWN = 2;
    /**
     * Toast颜色_深棕色
     */
    public static final int COLOR_BROWN_DARK = 3;
    /**
     * Toast颜色_红色
     */
    public static final int COLOR_RED = 4;
    /**
     * Toast颜色_橘黄色
     */
    public static final int COLOR_ORANGE = 5;
    /**
     * Toast颜色_黄色
     */
    public static final int COLOR_YELLOW = 6;
    /**
     * Toast颜色_绿色
     */
    public static final int COLOR_GREEN = 7;
    /**
     * Toast颜色_茶绿色
     */
    public static final int COLOR_GREEN_TEG = 8;
    /**
     * Toast颜色_黛绿色
     */
    public static final int COLOR_GREEN_DAISY = 9;
    /**
     * Toast颜色_天蓝色
     */
    public static final int COLOR_BLUE_SKY = 10;
    /**
     * Toast颜色_蓝色
     */
    public static final int COLOR_BLUE = 11;
    /**
     * Toast颜色_紫色
     */
    public static final int COLOR_PURPLE = 12;
    /**
     * Toast颜色_应用主色
     */
    public static final int COLOR_MAIN = 13;

    /**
     * Toast单例
     */
    private static BamToast toast;

    /**
     * Toast容器
     */
    private static RelativeLayout toast_layout;

    /**
     * 构造
     *
     * @param context
     */
    public BamToast(Context context) {
        super(context);
    }

    private static Context context;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull Context context) {
        BamToast.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 显示Toast
     *
     * @param text 显示的文本
     */
    public static void show(CharSequence text) {
        show(getContext(), text, COLOR_MAIN);
    }

    /**
     * 显示Toast
     *
     * @param resId 显示的文本资源id
     */
    public static void show(int resId) {
        show(getContext(), getContext().getString(resId), COLOR_MAIN);
    }

    /**
     * 显示Toast
     *
     * @param context   上下文
     * @param text      显示的文本
     * @param colorType 背景颜色
     */
    public static void show(Context context, CharSequence text, int colorType) {
        if (text == null || "".equals(text)) {
            return;
        }

        // 初始化一个新的Toast对象
        initToast(context, text, colorType);

        toast.setDuration(text.length() < 15 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);

        toast_layout.setPivotY(0);
        toast_layout.setScaleY(0f);
        ObjectAnimator anim = ObjectAnimator.ofFloat(toast_layout, "scaleY", 0, 1);
        anim.setStartDelay(200);
        anim.setDuration(800);
        anim.setInterpolator(new BounceInterpolator());
        anim.start();

        // 显示Toast
        toast.show();

    }

    /**
     * 初始化Toast
     *
     * @param context   上下文
     * @param text      显示的文本
     * @param colorType 背景颜色
     */
    private static void initToast(Context context, CharSequence text, int colorType) {
        try {
            cancelToast();

            toast = new BamToast(getContext());
            // 获取LayoutInflater对象
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由layout文件创建一个View对象
            View layout = inflater.inflate(R.layout.toast_layout, null);

            // 吐司容器
            toast_layout = (RelativeLayout) layout.findViewById(R.id.toast_layout);

            changeColor(colorType);

            // 实例化ImageView和TextView对象
            TextView toast_text = (TextView) layout.findViewById(R.id.toast_text);
            toast_text.setText(text);

            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变背景颜色
     *
     * @param colorType 背景颜色
     */
    private static void changeColor(int colorType) {
        switch (colorType) {
            case COLOR_BROWN:
                toast_layout.setBackgroundResource(R.drawable.toast_brown);
                break;
            case COLOR_BROWN_DARK:
                toast_layout.setBackgroundResource(R.drawable.toast_brown_dark);
                break;
            case COLOR_RED:
                toast_layout.setBackgroundResource(R.drawable.toast_red);
                break;
            case COLOR_ORANGE:
                toast_layout.setBackgroundResource(R.drawable.toast_orange);
                break;
            case COLOR_YELLOW:
                toast_layout.setBackgroundResource(R.drawable.toast_yellow);
                break;
            case COLOR_GREEN:
                toast_layout.setBackgroundResource(R.drawable.toast_green);
                break;
            case COLOR_GREEN_TEG:
                toast_layout.setBackgroundResource(R.drawable.toast_green_teg);
                break;
            case COLOR_GREEN_DAISY:
                toast_layout.setBackgroundResource(R.drawable.toast_green_daisy);
                break;
            case COLOR_BLUE_SKY:
                toast_layout.setBackgroundResource(R.drawable.toast_blue_sky);
                break;
            case COLOR_BLUE:
                toast_layout.setBackgroundResource(R.drawable.toast_blue);
                break;
            case COLOR_PURPLE:
                toast_layout.setBackgroundResource(R.drawable.toast_purple);
                break;
            case COLOR_BLACK:
                toast_layout.setBackgroundResource(R.drawable.toast_black);
                break;
            case COLOR_MAIN:
                toast_layout.setBackgroundResource(R.drawable.toast_main_color);
                break;
            default:
                toast_layout.setBackgroundColor(colorType);
                break;
        }
    }

    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }

}