package xxx.com.redassistant.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import xxx.com.redassistant.R;
import xxx.com.redassistant.utils.SharedPreferencesUtils;
import xxx.com.redassistant.utils.TTSController;
import xxx.com.redassistant.utils.VoicePlayUtils;

/**
 * 抢红包外挂服务
 */
public class RedAssistantService extends AccessibilityService {

    static final String TAG = "MZIA";
    private static String preActivity;

    /**
     * 微信的包名
     */
    static final String WECHAT_PACKAGENAME = "com.tencent.mm";
    /**
     * 红包消息的关键字
     */
    static final String HONGBAO_TEXT_KEY = "[微信红包]";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.i("onAccessibilityEvent", "事件---->event.getClassName() = " + event.getClassName());
        preActivity = SharedPreferencesUtils.getPreActivity(this);
        SharedPreferencesUtils.setPreActivity(this, event.getClassName().toString());
        //LogUtils.i("onAccessibilityEvent", "事件---->event = " + event);
        //是否帮助抢红包
        boolean helpGradRedBag = SharedPreferencesUtils.isHelpGradRedBag(RedAssistantService.this);
        final int eventType = event.getEventType();
        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            TTSController.getInstance(RedAssistantService.this).playText("来通知了啦");
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if (text.contains(HONGBAO_TEXT_KEY)) {
                        try {
                            playVoice(RedAssistantService.this, "yigeyi.mp3");
                            openNotify(event, helpGradRedBag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            try {
                openHongBao(event, helpGradRedBag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放语音
     *
     * @param context  上下文
     * @param fileName 文件名称,包含后缀
     */
    private void playVoice(Context context, String fileName) {
        if (SharedPreferencesUtils.isAutoRead(this)) {
            //如果需要自动播报红包来了的语音
            VoicePlayUtils.playVoice(context, fileName);
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //return super.onKeyEvent(event);
        return true;
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, R.string.redassistant_service_open_success, Toast.LENGTH_SHORT).show();
    }

    private void sendNotifyEvent() {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setPackageName(WECHAT_PACKAGENAME);
        event.setClassName(Notification.class.getName());
        CharSequence tickerText = HONGBAO_TEXT_KEY;
        event.getText().add(tickerText);
        manager.sendAccessibilityEvent(event);
    }

    /**
     * 打开通知栏消息
     */
    private void openNotify(AccessibilityEvent event, boolean helpGradRedBag) {
        if (!helpGradRedBag) {
            return;
        }
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开红包
     */
    private void openHongBao(AccessibilityEvent event, boolean helpGradRedBag) {
        LogUtils.i("openHongBaoaaa event = "+event+",helpGradRedBag = "+helpGradRedBag);
        if (helpGradRedBag) {
            if ("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f".equals(event.getClassName())) {
                LogUtils.i("openHongBao 这是拆红包的对话框页面,自动拆开红包");
                openRed();
            } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
                //拆完红包后看详细的纪录界面
                LogUtils.i("openHongBao 这是红包详情页面");
                luckyMoneyDetail();
            } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
                //在聊天界面,去点中红包
                LogUtils.i("openHongBao 这是聊天页面,检查是否有红包");

                //如果是从红包详情返回到聊天界面,就不打开了
                LogUtils.i("openHongBao preActivity = " + preActivity);
                if (preActivity.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
                    return;
                }
                checkKey2();
            }
        }
    }

    /**
     * 点中了红包，下一步就是去拆红包
     */
    private void openRed() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        LogUtils.i("openRed nodeInfo = "+nodeInfo);
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        //由于微信将"拆红包"文本改成了图片的"開",故不能通过文字来判断,只能通知以下方法
        //nodeInfo的第四个孩子就是那个"開"的按钮,将其执行点击事件
        nodeInfo.getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    /**
     * 由红包详情页面判断是否成功抢到了红包
     */
    private void luckyMoneyDetail() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            LogUtils.i("luckyMoneyDetail rootWindow为空");
            return;
        }
        //如果包含这个,说明抢成功啦,其实最多只可能包含一个,如果抢失败了,就只有0个
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("已存入零钱");
        //红包大小,如果抢成功了,那么取这一个
        List<AccessibilityNodeInfo> money = nodeInfo.findAccessibilityNodeInfosByText("元");
        //谁的红包
        List<AccessibilityNodeInfo> who = nodeInfo.findAccessibilityNodeInfosByText("的红包");
        LogUtils.i("luckyMoneyDetail list.size = " + list.size());
        LogUtils.i("luckyMoneyDetail money.size = " + money.size());
        LogUtils.i("luckyMoneyDetail who.size = " + who.size());
        if (list.size() > 0) {
            for (int i = 0; i < money.size(); i++) {
                LogUtils.i("luckyMoneyDetail money.get(i) = " + money.get(i).getText());
            }
            LogUtils.i("luckyMoneyDetail 成功抢到了" + who.get(0).getText());
            playVoice(RedAssistantService.this, "qiangyigeqi.mp3");
        } else {
            LogUtils.i("luckyMoneyDetail 主人不好意思,红包被抢光啦");
            playVoice(RedAssistantService.this, "qiangguangla.mp3");
        }
    }

    /**
     * 检查聊天界面是否有微信红包字样点击红包并打开
     */
    private void checkKey2() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            LogUtils.i("checkKey2 rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        LogUtils.i("checkKey2  当前聊天页面的微信红包数量:" + list.size());
        if (list.isEmpty()) {
            LogUtils.i("checkKey2 没有微信红包");
            list = nodeInfo.findAccessibilityNodeInfosByText(HONGBAO_TEXT_KEY);
            for (AccessibilityNodeInfo n : list) {
                LogUtils.i("checkKey2 微信红包:" + n);
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            //最新的红包领起
            LogUtils.i("checkKey2 有微信红包");
            for (int i = list.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                LogUtils.i("checkKey2 正在领取第(" + list.size() + ")个红包-" + parent);
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
            //改进 : 只领取最新一个红包
            /*AccessibilityNodeInfo parent = list.get(list.size() - 1).getParent();
            if (parent != null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }*/
        }
    }

}
