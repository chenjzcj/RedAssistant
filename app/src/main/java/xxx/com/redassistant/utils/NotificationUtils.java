package xxx.com.redassistant.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import xxx.com.redassistant.R;


/**
 * Created by MZIA(527633405@qq.com) on 2014/12/22 0022 11:12
 * 通知栏工具类
 * http://www.codeceo.com/article/android-notification-4-types.html
 */
public class NotificationUtils {

    private static NotificationManager nm;

    private NotificationUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int notifictionId = 800;


    /*******************************************
     * 下载进度通知栏start
     *********************************************/

    private static RemoteViews remoteViews;
    private static Notification notification;

    /**
     * 创建显示下载进度的通知栏
     *
     * @param context    上下文
     * @param tickerText 弹出通知栏的时候显示在状态栏的文字
     */
    public static void createDownloadNotification(Context context, String tickerText) {
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Notification.Builder builder = new Notification.Builder(context);
        // 必须要设置这个值,否则在通知栏不会显示
        notification = builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)//小图是显示在通知栏上面
                //大图是将通知下拉时显示在通知的左边的
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(tickerText)
                .build();

        // 自定义界面
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_download_apk_notification_progress);
        remoteViews.setProgressBar(R.id.pb_downapk, 100, 0, false);
        remoteViews.setTextViewText(R.id.tv, context.getString(R.string.downloading));
        notification.contentView = remoteViews;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    /**
     * 显示下载进度通知栏
     *
     * @param current 当前大小
     * @param total   总大小
     * @param info    提示信息
     */
    public static void showDownloadNotification(long current, long total, String info) {
        remoteViews.setTextViewText(R.id.tv, info);
        remoteViews.setProgressBar(R.id.pb_downapk, (int) total, (int) current, false);
        notification.contentView = remoteViews;
        nm.notify(R.layout.layout_download_apk_notification_progress, notification);
    }

    /**
     * 取消下载进度通知栏
     */
    public static void dismissDownloadNotification() {
        if (nm != null) {
            nm.cancel(R.layout.layout_download_apk_notification_progress);
        }
    }

    /******************************************下载进度通知栏end*********************************************/

    /**
     * 自定义View的通知栏
     */
    public static void showCustomNotification(Context context, Bitmap bitmap, String tickerText,
                                              String contentTitle, String contentText, Intent contentIntent) {
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Notification.Builder builder = new Notification.Builder(context);
        // 必须要设置这个值,否则在通知栏不会显示
        Notification notification = builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)// 必须要设置这个值,否则在通知栏不会显示
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(PendingIntent.getActivity(context, 100, contentIntent, PendingIntent.FLAG_ONE_SHOT))
                .setDefaults(Notification.DEFAULT_ALL)
                //.setNumber(notifictionId)//不累加了
                .build();

        // 自定义界面
        //RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        //rv.setImageViewBitmap(R.id.iv_notifi_icon, bitmap);
        //rv.setTextViewText(R.id.tv_notifi_title, title);
        //rv.setTextViewText(R.id.tv_notifi_content, content);
        // The color of the led. The hardware will do its best approximation.
        notification.ledARGB = 001100;
        //notification.contentView = rv;
        // 点击后消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //不同的id,防止 覆盖
        nm.notify(notifictionId++, notification);
    }

    /**
     * 显示标准的通知栏
     */
    public static void showStandardNotification(Context context, String tickerText, String contentTitle,
                                                String contentText, Intent contentIntent) {
        //notifictionId++;
        //从系统服务中获得通知管理器
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        //定义notification
        Notification.Builder builder = new Notification.Builder(context);
        // 必须要设置这个值,否则在通知栏不会显示
        Notification notification = builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(PendingIntent.getActivity(context, 100, contentIntent, PendingIntent.FLAG_ONE_SHOT))
                .setDefaults(Notification.DEFAULT_ALL)
                //.setNumber(notifictionId)//不累加了
                .build();

        notification.ledARGB = 001100;
        notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后消失
        //不同的id,防止 覆盖
        nm.notify(notifictionId, notification);
    }

    /**
     * 消除消息
     */
    public static void dismissNotification() {
        if (nm != null) {
            //一定要与nm.notify(notifictionId, notification);中的notifictionId相同
            nm.cancel(notifictionId);
        }
    }

    //---------------------非常好用的通知框架:https://github.com/sd6352051/NiftyNotification  ------------------
}
