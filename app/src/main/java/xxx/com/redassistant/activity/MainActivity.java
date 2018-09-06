package xxx.com.redassistant.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import xxx.com.redassistant.R;
import xxx.com.redassistant.base.BaseActivity;
import xxx.com.redassistant.utils.AccessibilityUtils;
import xxx.com.redassistant.utils.BamToast;
import xxx.com.redassistant.utils.PermissionHelper;
import xxx.com.redassistant.utils.ShareUtils;
import xxx.com.redassistant.utils.SharedPreferencesUtils;
import xxx.com.redassistant.utils.TTSController;
import xxx.com.redassistant.utils.UpdateUtils;

/**
 * 抢红包主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivRedbagRemind;
    private ImageView ivHelpRedbag;
    private ImageView ivNotificationRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.requestPermission(this, 100,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            nextOperation();
        }
    }

    /**
     * 启动页面后续的操作
     */
    private void nextOperation() {
        setContentView(R.layout.activity_main);
        TTSController.getInstance(this).startSpeaking();
        //TTSController.getInstance(MainActivity.this).playText("开始了");
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 已经获取了相应的权限
     */
    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        nextOperation();
    }

    /**
     * 获取了相应的权限失败
     */
    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        BamToast.show(getString(R.string.permission_deny));
        this.finish();
    }


    private void initView() {
        (ivRedbagRemind = (ImageView) findViewById(R.id.iv_redbag_remind)).setOnClickListener(this);
        (ivHelpRedbag = (ImageView) findViewById(R.id.iv_help_redbag)).setOnClickListener(this);
        (ivNotificationRead = (ImageView) findViewById(R.id.iv_notification_read)).setOnClickListener(this);
        ((TextView) findViewById(R.id.ver_name)).setText("V " + AppUtils.getAppVersionName());
        findViewById(R.id.rl_tone).setOnClickListener(this);
        findViewById(R.id.rl_problem).setOnClickListener(this);
        findViewById(R.id.rl_version_update).setOnClickListener(this);
        findViewById(R.id.rl_financial).setOnClickListener(this);
        findViewById(R.id.rl_share_friend).setOnClickListener(this);
        findViewById(R.id.rl_about).setOnClickListener(this);
        setData();
    }

    private void setData() {
        ivRedbagRemind.setSelected(AccessibilityUtils.isAccessibilitySettingsOn(this));
        ivHelpRedbag.setSelected(SharedPreferencesUtils.isHelpGradRedBag(this));
        ivNotificationRead.setSelected(SharedPreferencesUtils.isAutoRead(this));
    }

    /**
     * 打开辅助功能
     */
    public void openService() {
        boolean on = AccessibilityUtils.isAccessibilitySettingsOn(this);
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 0);
        Toast.makeText(this, getString(R.string.find_redassistant_and_open, on ? "关闭" : "开启"), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_redbag_remind:
                openService();
                break;
            case R.id.iv_help_redbag:
                boolean helpGradRedBag = SharedPreferencesUtils.isHelpGradRedBag(this);
                SharedPreferencesUtils.setHelpGradRedBag(this, !helpGradRedBag);
                setData();
                break;
            case R.id.iv_notification_read:
                boolean autoRead = SharedPreferencesUtils.isAutoRead(this);
                SharedPreferencesUtils.setAutoRead(this, !autoRead);
                setData();
                break;
            case R.id.rl_tone:
                enterActivity(this, RemindToneActivity.class);
                break;
            case R.id.rl_problem:
                enterActivity(this, ProblemActivity.class);
                break;
            case R.id.rl_version_update:
                UpdateUtils.checkUpdate(false, this);
                break;
            case R.id.rl_financial:
                enterActivity(this, FinancialActivity.class);
                break;
            case R.id.rl_share_friend:
                ShareUtils.share(this);
                break;
            case R.id.rl_about:
                enterActivity(this, AboutActivity.class);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTSController.getInstance(this).stopSpeaking();
    }
}
