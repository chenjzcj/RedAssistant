package xxx.com.redassistant.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxx.com.redassistant.R;
import xxx.com.redassistant.base.BaseActivity;
import xxx.com.redassistant.bean.Problem;

public class ProblemActivity extends BaseActivity {

    private ListView lvProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        lvProblem = (ListView) findViewById(R.id.lv_problem);
        List<Problem> problems = initData();
        lvProblem.setAdapter(new MyAdapter(problems, this));
    }

    private List<Problem> initData() {
        List<Problem> problems = new ArrayList<>();

        String[] problemDesc = getResources().getStringArray(R.array.problemDesc);
        String[] answer = getResources().getStringArray(R.array.answer);

        for (int i = 0; i < problemDesc.length; i++) {
            Problem problem = new Problem();
            problem.setProblemDesc(problemDesc[i]);
            problem.setAnswer(answer[i]);
            problems.add(problem);
        }
        return problems;
    }

    class MyAdapter extends BaseAdapter {

        private List<Problem> problems;
        private Context context;

        public MyAdapter(List<Problem> problems, Context context) {
            this.problems = problems;
            this.context = context;
        }

        @Override
        public int getCount() {
            return problems.size();
        }

        @Override
        public Object getItem(int position) {
            return problems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.layout_problem, null);
                viewHolder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
                viewHolder.answer = (TextView) convertView.findViewById(R.id.tv_answer);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Problem problem = problems.get(position);
            viewHolder.desc.setText(position + " . " + problem.getProblemDesc());
            viewHolder.answer.setText("答 : " + problem.getAnswer());
            return convertView;
        }
    }

    class ViewHolder {
        TextView desc;
        TextView answer;
    }
}
