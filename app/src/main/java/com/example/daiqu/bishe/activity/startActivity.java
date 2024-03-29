package com.example.daiqu.bishe.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.TencentUtils.TencentIM;
import com.example.daiqu.bishe.adapter.MyFragmentPagerAdapter;
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.fragment.messageFragment;
import com.example.daiqu.bishe.fragment.taskFragment;
import com.example.daiqu.bishe.fragment.userFragment;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;

import java.util.HashMap;
import java.util.Map;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class startActivity extends FragmentActivity {
    private LinearLayout task_layout, msg_layout, user_layout;
    private RadioGroup radiogroup_bottom;
    private TextView text_daiqu, text_message, text_user;
    private RadioButton frag_task, frag_message, frag_user;
    public static ViewPager vpager;
    public static String phone = "";
    private MyFragmentPagerAdapter mAdapter;
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    private taskFragment taskFrag;
    private messageFragment msgFrag;
    private userFragment userFrag;
    private TextView title_text;
    private ImageView title_add;
    private String data = "";
    public static userData uData;
    private Bundle savedInstanceState;
    //private final MyHandler handler = new MyHandler(this);
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        ActivityCollector.addActivity(this);
        if(ActivityCollector.activityList.contains(loadActivity.ldActivity)){
            ActivityCollector.removeActivity(loadActivity.ldActivity);
        }
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_start1);
        initwidget();
         /*此处备注：getSupportFragmentManager（）函数与最新的自定义的MyFragmentPagerAdapter有冲突
        ，具体冲突在于android x中原有的方法被弃用，多了一个参数的输入，待后期官方更新后即可改回，多出的参数为'BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT'
        切忌修改该参数！！！！*/
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        getUser(phone);
        vpager.setAdapter(mAdapter);
        //vpager.setOffscreenPageLimit(2);
        vpager.setCurrentItem(0);
        title_text.setText("任务");
        setRadioBtChecked(true, false, false);
        setTextSelected(true, false, false);

        frag_task.setOnClickListener(v -> {
            vpager.setCurrentItem(0);
            setRadioBtChecked(true, false, false);
            setTextSelected(true, false, false);
            setTitleText(0);
        });
        frag_message.setOnClickListener(v -> {
            vpager.setCurrentItem(1);
            setRadioBtChecked(false, true, false);
            setTextSelected(false, true, false);
            setTitleText(1);
        });
        frag_user.setOnClickListener(v -> {
            vpager.setCurrentItem(2);
            setRadioBtChecked(false, false, true);
            setTextSelected(false, false, true);
            setTitleText(2);
        });
        /*android x新的变动：设置监听事件的同时需要对不同的监听结果即时处理，不再是分开处理了*/
        radiogroup_bottom.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.frag_task:
                    vpager.setCurrentItem(PAGE_ONE);
                    break;
                case R.id.frag_message:
                    vpager.setCurrentItem(PAGE_TWO);
                    break;
                case R.id.frag_user:
                    vpager.setCurrentItem(PAGE_THREE);
                    break;
            }
        });
        //监听并重写View Page页面切换的处理方法
        vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
                if (state == 2) {
                    switch (vpager.getCurrentItem()) {
                        case PAGE_ONE:
                            setRadioBtChecked(true, false, false);
                            setTextSelected(true, false, false);
                            setTitleText(0);
                            break;
                        case PAGE_TWO:
                            setRadioBtChecked(false, true, false);
                            setTextSelected(false, true, false);
                            setTitleText(1);
                            break;
                        case PAGE_THREE:
                            setRadioBtChecked(false, false, true);
                            setTextSelected(false, false, true);
                            setTitleText(2);
                            break;
                    }
                }
            }
        });
        title_add.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this, R.style.add_dialog);
            dialog.setContentView(R.layout.add_dialog);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            Point size = new Point();
            d.getSize(size);
            p.height = (int) (size.y * 0.4);
            p.width = (int) (size.x * 0.8);
            dialog.show();
            dialog.getWindow().findViewById(R.id.creat_task).setOnClickListener(v1 -> {
                dialog.cancel();
                Intent intent = new Intent(this, createTaskActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            });
            dialog.getWindow().findViewById(R.id.post_task).setOnClickListener(v1 -> {
                dialog.cancel();
                Intent intent = new Intent(this, getAllUnfinishTask.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            });
        });
    }


    //封装所有的文本的选择状态
    private void setTextSelected(boolean daiqu, boolean message, boolean user) {
        text_daiqu.setSelected(daiqu);
        text_message.setSelected(message);
        text_user.setSelected(user);
    }

    ;

    //封装所有按钮的点击状态
    private void setRadioBtChecked(boolean daiqu, boolean message, boolean user) {
        frag_task.setChecked(daiqu);
        frag_message.setChecked(message);
        frag_user.setChecked(user);
    }

    //控件初始化
    private void initwidget() {
        radiogroup_bottom = findViewById(R.id.radiogroup_bottom);
        frag_task = findViewById(R.id.frag_task);
        frag_message = findViewById(R.id.frag_message);
        frag_user = findViewById(R.id.frag_user);
        vpager = findViewById(R.id.vpager);
        text_daiqu = findViewById(R.id.text_daiqu);
        text_message = findViewById(R.id.text_message);
        text_user = findViewById(R.id.text_user);
        title_text = findViewById(R.id.title_text);
        title_add = findViewById(R.id.title_add);
        task_layout = findViewById(R.id.task_layout);
        msg_layout = findViewById(R.id.msg_layout);
        user_layout = findViewById(R.id.user_layout);
        SharedPreferences sharedPreferences= getSharedPreferences("loadStatePerference", 0);
        phone = sharedPreferences.getString("phone", "");

    }
    private void setTitleText(int position) {
        if (position == 0) {
            title_text.setText("任务");
            title_add.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            title_text.setText("消息");
            title_add.setVisibility(View.GONE);
        } else {
            title_text.setText("我的");
            title_add.setVisibility(View.GONE);
        }
    }
    //获得手机号
    public String getPhone() {
        return phone;
    }
    //获得数据
    public String getData() {
        return data;
    }
    //查询用户信息
    public void getUser(String phone){
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        new Thread(() -> {
            SharedPreferences sp = getSharedPreferences("userDataPreferences", 0);
            String userInformation =  HttpUtils.sendPostMessage(map,"UTF-8","findUserInformation");
            if(userInformation.equals("-999")){
                userInformation =sp.getString("userInformation","null");
                this.uData = JSONArray.parseObject(userInformation, userData.class);
                Log.d("userInformation", uData.getPhone());
            }else {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userInformation",userInformation);
                editor.apply();
                this.uData = JSONArray.parseObject(userInformation, userData.class);
                Log.d("userInformation",uData.getPhone());
            }
            //腾讯三方登录（用于后续聊天）
            TencentIM.load(phone,uData);
        }).start();
    }
}