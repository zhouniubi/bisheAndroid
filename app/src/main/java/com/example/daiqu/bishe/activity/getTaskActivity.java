package com.example.daiqu.bishe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.adapter.PostTaskListViewAdapter;
import com.example.daiqu.bishe.data.TaskDataWithName;
import com.example.daiqu.bishe.fragment.taskFragment;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getTaskActivity extends Activity {
    private ListView postTaskList;
    private TextView textView;
    private ImageView title_paixu;
    private String phone = "";
    private List<TaskDataWithName> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_get_task);
        initWidget();
        ActivityCollector.addActivity(this);

    }
    private void initWidget(){
        phone = taskFragment.phone;
        postTaskList = findViewById(R.id.postTaskList3);
        textView = findViewById(R.id.show_no_msg3);
        title_paixu = findViewById(R.id.title_paixu3);
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Thread(() -> {
            Map<String,String> map = new HashMap<>();
            //设置选择01状态
            map.put("state", "01");
            map.put("accepterPhone", phone);
            String data = HttpUtils.sendPostMessage(map, "UTF-8", "findTaskByStateAndPhone");
            if (data.equals("-999")) {
                Looper.prepare();
                Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
                if (getPerference().equals("null")) {
                    runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                        postTaskList.setVisibility(View.GONE);
                        title_paixu.setVisibility(View.GONE);
                    });
                } else {
                    runOnUiThread(() -> {
                        textView.setVisibility(View.GONE);
                        postTaskList.setVisibility(View.VISIBLE);
                        title_paixu.setVisibility(View.VISIBLE);
                        //去掉边缘的拖影
                        List<TaskDataWithName> list = JSONArray.parseArray(getPerference(), TaskDataWithName.class);
                        PostTaskListViewAdapter adapter = new PostTaskListViewAdapter(this, R.id.postTaskList3, R.layout.list_item_layout2, list);
                        postTaskList.setAdapter(adapter);
                        postTaskList.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    });
                }
                Looper.loop();
            } else {
                if (data.equals("[{\"publisherName\":null,\"accepterName\":null,\"id\":null,\"taskCode\":null,\"publisherPhone\":null,\"accepterPhone\":null,\"type\":null,\"title\":null,\"getPlace\":null,\"postPlace\":null,\"needTime\":null,\"money\":null,\"infomation\":null,\"state\":null,\"pic\":null,\"file\":null,\"time\":null,\"time2\":null}]")) {
                    Log.d("DATA空是", data);
                    postPreference("null");
                    runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                        postTaskList.setVisibility(View.GONE);
                        title_paixu.setVisibility(View.GONE);
                    });
                } else {
                    Log.d("DATA不空是", data);
                    runOnUiThread(() -> {
                        textView.setVisibility(View.GONE);
                        postTaskList.setVisibility(View.VISIBLE);
                        title_paixu.setVisibility(View.VISIBLE);
                    });
                    postPreference(data);
                    List<TaskDataWithName> list = JSONArray.parseArray(data, TaskDataWithName.class);
                    this.list = list;
                    runOnUiThread((() -> {
                        PostTaskListViewAdapter adapter = new PostTaskListViewAdapter(this, R.id.postTaskList3, R.layout.list_item_layout2, list);
                        postTaskList.setAdapter(adapter);
                        //去掉边缘的拖影
                        postTaskList.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    }));
                }
            }
        }).start();
        title_paixu.setOnClickListener(v -> {
            Collections.reverse(list);
            runOnUiThread((() -> {
                PostTaskListViewAdapter adapter = new PostTaskListViewAdapter(this, R.id.postTaskList3, R.layout.list_item_layout2, list);
                postTaskList.setAdapter(adapter);
                //去掉边缘的拖影
                postTaskList.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }));
        });
        postTaskList.setOnItemClickListener((parent, view, position, id) -> {
            TaskDataWithName taskData = list.get(position);
            Intent intent = new Intent(this,showTaskInformation2.class);
            intent.putExtra("taskData", taskData);
            intent.putExtra("phone", phone);
            Log.d("taskData", taskData.getTitle());
            startActivity(intent);
        });
    }
    private void postPreference(String data) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("getTaskUnfinish", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", data);
        editor.apply();
    }

    private String getPerference() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("getTaskUnfinish", 0);
        String answer = sharedPreferences.getString("data", "null");
        return answer;
    }
}