package xxx.com.redassistant.utils;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import xxx.com.redassistant.R;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/9 0009 11:21
 * 讯飞语音帮助组件
 */
public class TTSController {

    public static TTSController mTTSController;
    private Context mContext;
    boolean isfinish = true;
    // 语音合成器
    private SpeechSynthesizer mSpeechSynthesizer;

    private TTSController(Context context) {
        mContext = context;
        initSpeechSynthesizer();
    }

    public static TTSController getInstance(Context context) {
        if (mTTSController == null) {
            mTTSController = new TTSController(context);
        }
        return mTTSController;
    }

    public void initSpeechSynthesizer() {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        // 设置发音人
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置语速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
        // 设置音量，范围0~100
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
        // 设置语调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, mContext.getString(R.string.preference_key_tts_pitch));
        // 设置云端
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }

    /**
     * 停止说话
     */
    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    /**
     * 开始说话
     */
    public void startSpeaking() {
        isfinish = true;
    }

    /**
     * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
     *
     * @param playText 需要语音播报的文本
     */
    public void playText(String playText) {
        if (!isfinish) {
            return;
        }
        if (mSpeechSynthesizer == null) {
            initSpeechSynthesizer();
        }
        // 进行语音合成.
        mSpeechSynthesizer.startSpeaking(playText, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            //开始说话
            isfinish = false;
        }

        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
            // 缓冲进度
        }

        @Override
        public void onSpeakPaused() {
            // 暂停说话
        }

        @Override
        public void onSpeakResumed() {
            // 重新开始说话
        }

        @Override
        public void onSpeakProgress(int arg0, int arg1, int arg2) {
            // 说话进度
        }

        @Override
        public void onCompleted(SpeechError arg0) {
            // 说话完成
            isfinish = true;
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            //其他事件
        }
    };
}
