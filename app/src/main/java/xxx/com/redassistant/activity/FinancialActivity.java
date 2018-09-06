package xxx.com.redassistant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxx.com.redassistant.R;
import xxx.com.redassistant.base.BaseActivity;

public class FinancialActivity extends BaseActivity {

    private ListView financialList;
    private List<Financial> financials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);
        financialList = (ListView) findViewById(R.id.financial_list);
        initData();
        financialList.setAdapter(new MyAdapter());

        financialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Financial financial = financials.get(position);
                /*Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(financial.getLinkUrl());
                intent.setData(content_url);
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);*/
                //http://www.cnblogs.com/markgg/p/6635705.html
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(financial.getLinkUrl());
                intent.setDataAndType(content_url, "text/html");
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        financials = new ArrayList<>();

        Financial financial = new Financial();
        financial.setAppName("暴风金融");
        financial.setTitle("我已投了暴风金融,收益还不错,快来试试吧!");
        financial.setHint("点击投资,咱俩都能!得!收!收!收!益!约吗?");
        financial.setResId(R.drawable.baofeng);
        financial.setLinkUrl("https://8.baofeng.com/bfh5/html/share/invite_page.html?inviteCode=785828&channelid=appshare&telnum=188****1705");

        financials.add(financial);

        Financial financial1 = new Financial();
        financial1.setAppName("友金所");
        financial1.setTitle("送你88元红包,马上来领吧!");
        financial1.setHint("我在友金所投资,推荐你跟我一起赚钱!");
        financial1.setResId(R.drawable.youjinsuo);
        financial1.setLinkUrl("https://www.yyfax.com/h5/activity/adviser/register.html?code=yu0c7g&nickname=188****1705");

        financials.add(financial1);

        Financial financial2 = new Financial();
        financial2.setAppName("甜橙理财");
        financial2.setTitle("新客送188元券+16%收益!");
        financial2.setHint("中国电信官方理财平台,已为2500万人赚了20亿元!历史兑付度100%!");
        financial2.setResId(R.drawable.tiancheng);
        financial2.setLinkUrl("https://wap.bestpay.com.cn/financial/redpacket?referee=18818991705");

        financials.add(financial2);

        Financial financial3 = new Financial();
        financial3.setAppName("麻袋理财");
        financial3.setTitle("您的好友已经投资了,现在接受邀请享三重注册大礼!");
        financial3.setHint("友福同享|领取1888元投资券+200元代金券,和ta一起盆满钵满吧!");
        financial3.setResId(R.drawable.madai);
        financial3.setLinkUrl("https://www.madailicai.com/h5/mobile/lp/2017/07/invite/friend.html?nickname=%E9%92%9F%E6%88%90%E5%86%9B&mobileNo=188******05&inviteCode=2357105&utm_source=app&utm_medium=wxsession");

        financials.add(financial3);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return financials.size();
        }

        @Override
        public Object getItem(int position) {
            return financials.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = View.inflate(FinancialActivity.this, R.layout.financial_item, null);
            TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
            TextView tv_hint = (TextView) inflate.findViewById(R.id.tv_hint);
            TextView tv_appname = (TextView) inflate.findViewById(R.id.tv_appname);
            ImageView iv_app_icon = (ImageView) inflate.findViewById(R.id.iv_app_icon);
            ImageView iv_icon = (ImageView) inflate.findViewById(R.id.iv_icon);

            Financial financial = financials.get(position);
            tvTitle.setText(financial.getTitle());
            tv_hint.setText(financial.getHint());
            tv_appname.setText(financial.getAppName());
            iv_app_icon.setImageResource(financial.getResId());
            iv_icon.setImageResource(financial.getResId());
            return inflate;
        }
    }

    class Financial {
        String title;
        String hint;
        int resId;
        String appName;
        String linkUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }
}


