package xxx.com.redassistant.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.x;

import xxx.com.redassistant.utils.BamToast;

/**
 * Created by MZIA(527633405@qq.com) on 2016/11/19 19:23
 */
public class MyApp extends Application {
    private static MyApp context;
    @Override
    public void onCreate() {
        super.onCreate();
        initXuitls();
        this.context = this;
        //讯飞语音初始化
        Utils.init(context);
        BamToast.init(context);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=583035c5");
    }

    public static Context context(){
        return context;
    }

    /**
     * xutils初始化
     */
    private void initXuitls() {
        x.Ext.init(this);
        // 是否输出debug日志
        x.Ext.setDebug(false);
    }
}
