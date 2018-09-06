package xxx.com.redassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;

import xxx.com.redassistant.R;
import xxx.com.redassistant.base.BaseActivity;
import xxx.com.redassistant.utils.ShareUtils;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView version = (TextView) findViewById(R.id.tv_version);
        version.setText(getString(R.string.version_info, AppUtils.getAppVersionName(), AppUtils.getAppVersionCode()));

        findViewById(R.id.btn_share_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(AboutActivity.this);
            }
        });
    }


}
