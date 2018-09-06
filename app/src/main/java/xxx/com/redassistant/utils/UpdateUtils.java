package xxx.com.redassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import xxx.com.redassistant.Constants;
import xxx.com.redassistant.R;
import xxx.com.redassistant.dialog.CommonDialog;
import xxx.com.redassistant.dialog.DialogHelper;
import xxx.com.redassistant.dialog.WaitDialog;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/3/21 0021
 * 应用更新工具类
 */
public class UpdateUtils {

    private static WaitDialog waitDialog;

    /**
     * 检查更新
     *
     * @param isclient 是否不显示加载等提示信息
     * @param context  上下文
     */
    public static void checkUpdate(final boolean isclient, final Context context) {
        //以下代码暂时不从服务器更新新版本
        /*if (context != null) {
            if (!isclient)
                ToastUtils.showShort(context.getString(R.string.cur_is_least_version));
            return;
        }*/
        //以上代码暂时不从服务器更新新版本
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(context.getString(R.string.error_code_10_));
            return;
        }
        RequestParams params = new RequestParams(Constants.UPDATER);
        params.addBodyParameter("version", AppUtils.getAppVersionName());
        if (!isclient) {
            waitDialog = DialogHelper.getWaitDialog((Activity) context, R.string.getting_last_version_info);
            waitDialog.setCancelable(true);
            waitDialog.show();
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.getString("version").equals(AppUtils.getAppVersionName())) {
                        //有新版本
                        String apkUrl = jsonObject.getString("packUrl");//apk包下载地址
                        String content = jsonObject.getString("content");//本次升级的内容
                        //格式参考content = "版本caihongqiao_v1.4.12更新点 : <br>
                        // 1.修复某些手机在修改头像时崩溃的bug<br>
                        // 2.修复手表状态经常显示离线(明明在线)的bug<br>
                        // 3.修复某些手机消息界面雾化效果不消失的bug<br>
                        // 4.历史轨迹优化";
                        boolean isForce = jsonObject.getBoolean("force");//是否强制升级
                        //isForce = false;//测试的时候打开
                        showUpdateDialog(apkUrl, context, content, isForce);
                    } else {
                        //是最新版本
                        if (!isclient)
                            BamToast.show(context.getString(R.string.cur_is_least_version));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("onError : " + ex.toString());
                if (!isclient)
                    BamToast.show(context.getString(R.string.server_is_lost));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("onCancelled : " + cex.toString());
                if (!isclient)
                    BamToast.show(context.getString(R.string.server_is_lost));
            }

            @Override
            public void onFinished() {
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
        });

    }

    /**
     * 发现新版本,提示应用更新对话框
     *
     * @param apkUrl String
     */
    public static void showUpdateDialog(final String apkUrl, final Context context, String content, final boolean isForce) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setTitle(context.getString(R.string.version_update_hint,
                context.getString(R.string.app_name)));
        if (content == null) {
            commonDialog.setMessage(context.getString(R.string.verson_find_new_version));
        } else {
            commonDialog.setMessage(content);
        }
        commonDialog.setPositiveButton(context.getString(R.string.update_immediately), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(apkUrl, context, isForce);
                dialog.dismiss();
            }
        });
        commonDialog.setNegativeButton(context.getString(R.string.version_ignore), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isForce)
                    dialog.dismiss();
            }
        });
        commonDialog.setCancelable(!isForce);
        commonDialog.show();
    }

    /**
     * 下载apk
     *
     * @param apkUrl apk下载地址
     */
    public static void downloadApk(final String apkUrl, final Context context, final boolean isForce) {
        RequestParams params = new RequestParams(apkUrl);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            private ProgressBar pb;
            private TextView tv;
            private CommonDialog commonDialog1;

            @Override
            public void onWaiting() {
                LogUtils.i("onWaiting");
                commonDialog1 = new CommonDialog(context);
                View view = LayoutInflater.from(context).inflate(R.layout.layout_download_apk, null);
                pb = (ProgressBar) view.findViewById(R.id.pb_downapk);
                tv = (TextView) view.findViewById(R.id.tv);
                commonDialog1.setCancelable(false);
                commonDialog1.setContent(view);
                pb.setMax(100);
            }

            @Override
            public void onStarted() {
                if (isForce) {
                    commonDialog1.show();//如果需要强制更新,则直接在页面上,否则只在通知栏显示
                }
                LogUtils.i("onStarted:" + apkUrl);
                NotificationUtils.createDownloadNotification(context, context.getString(R.string.start_download));
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtils.i(current + "/" + total);
                String progressInfo = context.getString(R.string.app_name) +
                        context.getString(R.string.downloading_progress, (int) (current / (total / 100)));
                tv.setText(progressInfo);
                pb.setProgress((int) (current / (total / 100)));
                NotificationUtils.showDownloadNotification(current, total, progressInfo);
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.i("onSuccess:" + result.getAbsolutePath());
                ApkUtils.installApk(context, result);
                NotificationUtils.dismissDownloadNotification();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                if (commonDialog1 != null) commonDialog1.dismiss();
                LogUtils.i("onFinished");
                BamToast.show(context.getString(R.string.download_finish));
            }
        });
    }
}
