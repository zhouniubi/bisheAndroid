package com.example.daiqu.bishe.myWidget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.activity.startActivity;
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class updateDialog extends Dialog {
    private String pic,sex,identity,introduce;
    private ImageView select_pic1,select_pic2,select_pic3,select_pic4,select_pic5,select_pic6;
    private RadioGroup update_radioGropSex, update_radioGropIdentity;
    private RadioButton radio_bt_boy1, radio_bt_girl1, radio_bt_teacher1, radio_bt_student1;
    private TextView text_boy1, text_girl1;
    private EditText update_user_name,task_info_introduce;
    private Button update_exit,update_commit;
    private Context context;
    private userData uData;
    public updateDialog(@NonNull Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        setContentView(R.layout.dialog_update_user_information);
        init();
        onClick();
    }


    protected void init(){
        update_exit = findViewById(R.id.update_exit);
        update_commit = findViewById(R.id.update_commit);
        select_pic1 = findViewById(R.id.select_pic1);
        select_pic2 = findViewById(R.id.select_pic2);
        select_pic3 = findViewById(R.id.select_pic3);
        select_pic4 = findViewById(R.id.select_pic4);
        select_pic5 = findViewById(R.id.select_pic5);
        select_pic6 = findViewById(R.id.select_pic6);
        update_user_name = findViewById(R.id.update_user_name);
        task_info_introduce = findViewById(R.id.update_task_info_introduce);
        update_radioGropSex = findViewById(R.id.update_radioGropSex);
        update_radioGropIdentity = findViewById(R.id.update_radioGropIdentity);
        radio_bt_boy1 = findViewById(R.id.radio_bt_boy1);
        radio_bt_girl1 = findViewById(R.id.radio_bt_girl1);
        text_boy1 = findViewById(R.id.text_boy1);
        text_girl1 = findViewById(R.id.text_girl1);
        radio_bt_teacher1 = findViewById(R.id.radio_bt_teacher1);
        radio_bt_student1 = findViewById(R.id.radio_bt_student1);
        TextChanger textChanger = new TextChanger();
        update_user_name.addTextChangedListener(textChanger);
        initView();
    }
    protected void onClick(){
        update_exit.setOnClickListener(v -> {
            super.cancel();
        });
        select_pic1.setOnClickListener(v -> {
            setPicSelected(true,false,false,false,false,false);
            pic = "000";
        });
        select_pic2.setOnClickListener(v -> {
            setPicSelected(false,true,false,false,false,false);
            pic = "001";
        });
        select_pic3.setOnClickListener(v -> {
            setPicSelected(false,false,true,false,false,false);
            pic = "002";
        });
        select_pic4.setOnClickListener(v -> {
            setPicSelected(false,false,false,true,false,false);
            pic = "100";
        });
        select_pic5.setOnClickListener(v -> {
            setPicSelected(false,false,false,false,true,false);
            pic = "101";
        });
        select_pic6.setOnClickListener(v -> {
            setPicSelected(false,false,false,false,false,true);
            pic = "102";
        });
        //设置性别按钮监听
        update_radioGropSex.setOnCheckedChangeListener(((group, checkedId) -> {
            if(radio_bt_boy1.isChecked()){
                radio_bt_girl1.setChecked(false);
                sex="1";
            }else{
                radio_bt_girl1.setChecked(true);
                sex="0";
            }
        }));
        radio_bt_boy1.setOnClickListener(v -> {
            changeTextColor();
        });
        radio_bt_girl1.setOnClickListener(v -> {
            changeTextColor();
        });
        //设置教师按钮监听
        update_radioGropIdentity.setOnCheckedChangeListener(((group, checkedId) -> {
            if(radio_bt_teacher1.isChecked()){
                radio_bt_student1.setChecked(false);
                identity="1";
            }else{
                radio_bt_student1.setChecked(true);
                identity="0";
            }
        }));
        //提交按钮监听
        update_commit.setOnClickListener(v -> {
            String name = AES.encrypt(update_user_name.getText().toString());
            if(task_info_introduce.length()==0){
                introduce = "null";
            }else{
                introduce = task_info_introduce.getText().toString();
            }
            if(radio_bt_boy1.isChecked()){
                sex = "1";
            }
            if(radio_bt_girl1.isChecked()){
                sex = "0";
            }
            if(radio_bt_teacher1.isChecked()){
                identity = "1";
            }
            if(radio_bt_student1.isChecked()){
                identity = "0";
            }
            new Thread(() -> {
                Map<String,String> map = new HashMap<>();
                map.put("phone", uData.getPhone());map.put("name", name);
                map.put("sex",sex);map.put("identity", identity);
                map.put("pic", pic);map.put("introduce", introduce);
                String state = HttpUtils.sendPostMessage(map,"UTF-8","updateUserInformation");
                Looper.prepare();
                if(state.equals("11")){
                    Toast.makeText(context,"更新成功啦ヾ(≧▽≦*)o", Toast.LENGTH_SHORT).show();
                    uData.setIdentity(identity);uData.setName(name);uData.setPic(pic);uData.setSex(sex);
                    SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    String userInformation = JSONArray.toJSONString(uData);
                    editor.putString("userInformation",userInformation);
                    editor.apply();
                    updateDialog.super.cancel();
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, startActivity.class);
                    context.startActivity(intent);

                }else if(state.equals("db_error")){
                    Toast.makeText(context,"数据库异常！！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"未知错误！！", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }).start();
        });
    }
    //设置图片的选中状态
    protected void setPicSelected(boolean pic1, boolean pic2, boolean pic3, boolean pic4, boolean pic5, boolean pic6){
        select_pic1.setSelected(pic1);
        select_pic2.setSelected(pic2);
        select_pic3.setSelected(pic3);
        select_pic4.setSelected(pic4);
        select_pic5.setSelected(pic5);
        select_pic6.setSelected(pic6);
    }
    //获取当前用户信息
    protected void getUser(){
        SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation, userData.class);
    }
    //设置选择文本的颜色（通过可使用状态来转换）
    private void changeTextColor() {
        if (radio_bt_boy1.isChecked()) {
            text_boy1.setEnabled(true);
            text_girl1.setEnabled(false);
        }
        if (radio_bt_girl1.isChecked()) {
            text_boy1.setEnabled(false);
            text_girl1.setEnabled(true);
        }
    }
    //初始化可以显示的视图
    protected void initView(){
        String picId = uData.getPic();
        pic = picId;
        switch (picId){
            case "000":
                setPicSelected(true,false,false,false,false,false);
                break;
            case "001":
                setPicSelected(false,true,false,false,false,false);
                break;
            case "002":
                setPicSelected(false,false,true,false,false,false);
                break;
            case "100":
                setPicSelected(false,false,false,true,false,false);
                break;
            case "101":
                setPicSelected(false,false,false,false,true,false);
                break;
            case "102":
                setPicSelected(false,false,false,false,false,true);
                break;
            default:
                break;
        }
        update_user_name.setText(AES.decrypt(uData.getName()));
        if(uData.getSex().equals("1")){
            radio_bt_boy1.setChecked(true);
            changeTextColor();
        }else{
            radio_bt_girl1.setChecked(true);
            changeTextColor();
        }
        if(uData.getIdentity().equals("1")){
            radio_bt_teacher1.setChecked(true);
        }else{
            radio_bt_student1.setChecked(true);
        }
        if(uData.getIntroduce().equals("null")){
            task_info_introduce.setHint("个人介绍：暂无");
        }else{
            task_info_introduce.setText(uData.getIntroduce());
        }
    }
    class TextChanger implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int nl = update_user_name.length();
            update_commit.setEnabled(nl != 0 );
        }
    }
}
