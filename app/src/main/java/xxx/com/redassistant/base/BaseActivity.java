package xxx.com.redassistant.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import xxx.com.redassistant.R;
import xxx.com.redassistant.utils.StatusBarUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2017/1/14 23:03
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.setTranslucentStatus(true, this, android.R.color.holo_red_light);
        //BmobUtils.init(this);
        //BmobUtils.getVersionInfo(this,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 进入activity
     *
     * @param activity 上下文
     * @param clazz    Class
     */
    protected void enterActivity(Activity activity, Class clazz) {
        startActivity(new Intent(activity, clazz));
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        this.overridePendingTransition(0, R.anim.push_right_acc);
    }
}
