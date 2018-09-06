package xxx.com.redassistant.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * 音频播放工具类
 * http://www.bkjia.com/Androidjc/870591.html
 * http://blog.csdn.net/aiqing0119/article/details/24632247
 */
public class VoicePlayUtils {
    static MediaPlayer mMediaPlayer;
    public static boolean isPlaying = false;

    public static void stopPlayVoice() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        isPlaying = false;
    }

    /**
     * http://www.xuebuyuan.com/375079.html
     *
     * @param fileName assets文件夹中的文件名,建议音视频文件都放入assets目录而不是res/raw目录
     */
    public static void playVoice(Context context, String fileName) {
        if (isPlaying) {
            stopPlayVoice();
        }
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            //这一句很重要啊
            mMediaPlayer.setDataSource(fileDescriptor, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            //mMediaPlayer.setDataSource(fileDescriptor);这样是不行的,会播放第一个音频文件,我也是醉了
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayVoice();
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPlaying = true;
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}