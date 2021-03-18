package com.example.daiqu.bishe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daiqu.R;
import com.example.daiqu.bishe.data.TaskDataWithName;
import com.example.daiqu.bishe.tool.AES;

public class showTaskInformation extends Activity {
private TaskDataWithName data;
private TextView taskInfomation_code,time_text_info,task_info_publisher,taskInformation_fb_time,task_info_accepter,taskInformation_js_time
        ,task_info_type,clickToSeeBigPic,task_info_state;
private EditText taskInformation_title,task_info_money,task_info_getPlace,task_info_postPlace,task_info_introduce;
private Spinner spinner_info;
private ImageView task_info_pic,task_info_deletePic,task_info_delete,task_info_chat,task_info_commit,task_info_update,task_info_quxiao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_task_information);
        Intent intent = getIntent();
        data = (TaskDataWithName) intent.getSerializableExtra("taskData");
        Log.d("data详情：", data.getTitle());
        initWidget();//绑定控件
    }
    private void initWidget(){
        taskInfomation_code = findViewById(R.id.taskInfomation_code);
        time_text_info = findViewById(R.id.time_text_info);
        task_info_publisher = findViewById(R.id.task_info_publisher);
        taskInformation_fb_time = findViewById(R.id.taskInformation_fb_time);
        task_info_accepter = findViewById(R.id.task_info_accepter);
        taskInformation_js_time = findViewById(R.id.taskInformation_js_time);
        task_info_type = findViewById(R.id.task_info_type);
        clickToSeeBigPic = findViewById(R.id.clickToSeeBigPic);
        clickToSeeBigPic = findViewById(R.id.clickToSeeBigPic);
        taskInformation_title = findViewById(R.id.taskInformation_title);
        task_info_money = findViewById(R.id.task_info_money);
        task_info_getPlace = findViewById(R.id.task_info_getPlace);
        task_info_postPlace = findViewById(R.id.task_info_postPlace);
        task_info_introduce = findViewById(R.id.task_info_introduce);
        spinner_info = findViewById(R.id.spinner_info);
        task_info_pic = findViewById(R.id.task_info_pic);
        task_info_deletePic = findViewById(R.id.task_info_deletePic);
        task_info_delete = findViewById(R.id.task_info_delete);
        task_info_chat = findViewById(R.id.task_info_chat);
        task_info_commit = findViewById(R.id.task_info_commit);
        task_info_update = findViewById(R.id.task_info_update);
        task_info_state = findViewById(R.id.task_info_state);
        task_info_quxiao = findViewById(R.id.task_info_quxiao);
    }
    protected void onStart() {
        super.onStart();
        setText();
        setEnable(false);
        setVisible(0, 0, 8, 0);
        onClick();

    }
    private void setText(){
        taskInfomation_code.setText(data.getTaskCode());
        taskInformation_title.setText(data.getTitle());
        task_info_publisher.setText("发布者："+ AES.decrypt(data.getPublisherName()));
        if("null".equals(data.getAccepterName())){
            task_info_accepter.setText("接收者：暂无");
        }else {
            task_info_accepter.setText("接收者："+AES.decrypt(data.getAccepterName()));
        }
        taskInformation_fb_time.setText("发布时间："+data.getTime());
        if("null".equals(data.getTime2())){
            taskInformation_js_time.setText("接收时间：暂无");
        }else{
            taskInformation_js_time.setText("接收时间："+data.getTime2());
        }
        task_info_money.setText(data.getMoney());
        if("0".equals(data.getType())){
            task_info_type.setText("类型：洗衣代取");
        }else if("1".equals(data.getType())){
            task_info_type.setText("类型：超市代取");
        }else if("2".equals(data.getType())){
            task_info_type.setText("类型：外卖代取");
        }else{
            task_info_type.setText("类型：快递代取");
        }
        if(data.getState().equals("00")){
            task_info_state.setText("无人接取");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state1, null));
        }else if(data.getState().equals("01")){
            task_info_state.setText("派送中");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state2, null));
        }else if(data.getState().equals("11")){
            task_info_state.setText("派送完成");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state3, null));
        }
        task_info_getPlace.setText(data.getGetPlace());
        task_info_postPlace.setText(data.getPostPlace());
        task_info_introduce.setText("任务详情:"+data.getInfomation());
    }
    private void setEnable(boolean enable){
        if(!enable){
            taskInformation_title.setFocusable(enable);
            task_info_money.setFocusable(enable);
            task_info_getPlace.setFocusable(enable);
            task_info_postPlace.setFocusable(enable);
            task_info_introduce.setFocusable(enable);
            taskInformation_title.setEnabled(enable);
            task_info_money.setEnabled(enable);
            task_info_getPlace.setEnabled(enable);
            task_info_postPlace.setEnabled(enable);
            task_info_introduce.setEnabled(enable);
        }else{
            //taskInformation_title.setFocusable(enable);
            taskInformation_title.setEnabled(enable);
            task_info_money.setEnabled(enable);
            task_info_getPlace.setEnabled(enable);
            task_info_postPlace.setEnabled(enable);
            task_info_introduce.setEnabled(enable);
            taskInformation_title.setFocusableInTouchMode(enable);
            task_info_money.setFocusableInTouchMode(enable);
            task_info_getPlace.setFocusableInTouchMode(enable);
            task_info_postPlace.setFocusableInTouchMode(enable);
            task_info_introduce.setFocusableInTouchMode(enable);
            taskInformation_title.requestFocus();

        }

    }
    private void setVisible(int visible1,int visible2,int visible3,int visible4){
        //visible:0;Invisible:4;gone:8
        //visible1：设置删除img的存在性；visible2：设置聊天img的存在性；visible3：设置删除取消的存在性；visible4：设置提交img的存在性；
        task_info_delete.setVisibility(visible1);
        task_info_chat.setVisibility(visible2);
        task_info_quxiao.setVisibility(visible3);
        task_info_commit.setVisibility(visible4);
    }
    private void onClick(){
        task_info_update.setOnClickListener(v -> {
            setEnable(true);
            setVisible(8, 8, 0, 0);
        });
        task_info_quxiao.setOnClickListener(v -> {
            onStart();
        });
    }


}