package com.example.daiqu.bishe.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.daiqu.R;

public class loginActivity extends Activity {
   private EditText login_phone,login_pwd,yanzheng_code;
   private ImageView cha5,eye2;
   private Button login_btn,getyanzheng;
   private RadioGroup login_radioGropSex,login_radioGropIdentity;
   private RadioButton radio_bt_boy,radio_bt_girl,radio_bt_teacher,radio_bt_student;
   private TextView text_boy,text_girl;
   private String sex,identity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login);

        login_phone=findViewById(R.id.login_phone);
        login_pwd=findViewById(R.id.login_pwd);
        yanzheng_code=findViewById(R.id.yanzheng_code);
        cha5=findViewById(R.id.cha5);
        eye2=findViewById(R.id.eye2);
        login_btn = findViewById(R.id.login_btn);
        login_radioGropSex = findViewById(R.id.login_radioGropIdentity);
        radio_bt_boy = findViewById(R.id.radio_bt_boy);
        radio_bt_girl = findViewById(R.id.radio_bt_girl);
        text_boy = findViewById(R.id.text_boy);
        text_girl = findViewById(R.id.text_girl);
        login_radioGropIdentity = findViewById(R.id.login_radioGropIdentity);
        radio_bt_teacher = findViewById(R.id.radio_bt_teacher);
        radio_bt_student = findViewById(R.id.radio_bt_student);
        getyanzheng = findViewById(R.id.getyanzheng);
        initListener();
        login_btn.setEnabled(false);
        radio_bt_boy.setChecked(true);
        radio_bt_girl.setChecked(false);
        text_boy.setEnabled(true);
        text_girl.setEnabled(false);
        radio_bt_teacher.setChecked(true);
        radio_bt_student.setChecked(false);

        login_radioGropSex.setOnCheckedChangeListener((group, checkedId) -> {
            if(radio_bt_boy.isChecked()){
                radio_bt_girl.setChecked(false);
                sex="1";
            }
            if(radio_bt_girl.isChecked()){
                radio_bt_boy.setChecked(false);
                sex="0";
            }
        });
        radio_bt_boy.setOnClickListener(v -> {
            changeTextColor();
        });
        radio_bt_girl.setOnClickListener(v -> {
            changeTextColor();
        });
        login_radioGropIdentity.setOnCheckedChangeListener((group, checkedId) -> {
            if(radio_bt_teacher.isChecked()){
                radio_bt_student.setChecked(false);
                identity="1";
            }
            if(radio_bt_student.isChecked()){
                radio_bt_teacher.setChecked(false);
                identity="0";
            }
        });
       getyanzheng.setOnClickListener(v -> {
            countDown(30);
        });
    }
    private void initListener() {
        TextChanger textChanger = new TextChanger();
        login_phone.addTextChangedListener(textChanger);
        login_pwd.addTextChangedListener(textChanger);
        yanzheng_code.addTextChangedListener(textChanger);
    }
    //设置选择文本的颜色（通过可使用状态来转换）
    private void changeTextColor(){
        if(radio_bt_boy.isChecked()){
            text_boy.setEnabled(true);
            text_girl.setEnabled(false);
        }
        if(radio_bt_girl.isChecked()){
            text_boy.setEnabled(false);
            text_girl.setEnabled(true);
        }
    }
    //设置倒计时
    private void countDown(int time){
        CountDownTimer countDownTimer = new CountDownTimer(time*1000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                getyanzheng.setEnabled(false);
                getyanzheng.setText(millisUntilFinished/1000+"s");
                getyanzheng.setTextSize(13);
            }
            @Override
            public void onFinish() {
                getyanzheng.setEnabled(true);
                getyanzheng.setText("验证码");
                getyanzheng.setTextSize(13);
            }
        }.start();
    }
    //重写编辑栏监听部分，实现按钮颜色变换,部分图标的可视化
    class TextChanger implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int ph = login_phone.length(), pw = login_pwd.length(),code = yanzheng_code.length();
            if(ph==0){
                cha5.setVisibility(View.INVISIBLE);
            }else{
                cha5.setVisibility(View.VISIBLE);
            }
            if(pw==0){
                eye2.setVisibility(View.INVISIBLE);
            }else{
                eye2.setVisibility(View.VISIBLE);
            }
            if(ph==0||pw<6||pw>15||code==0){
                login_btn.setEnabled(false);
            }else{
                login_btn.setEnabled(true);
            }
        }
    }
}