package com.example.daiqu.bishe.activity;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.daiqu.R;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.HttpUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class loadActivity extends Activity {

    private EditText phone_input, pwd_input;
    private TextView notice,forget_pwd,login;
    private ImageView cha, eye;
    private Button load_button;
    private String phone = "", pwd = "";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_load);
        phone_input = findViewById(R.id.input_phone);
        pwd_input = findViewById(R.id.input_pwd);
        load_button = findViewById(R.id.load_btn);
        notice = findViewById(R.id.noticeMsg);
        cha = findViewById(R.id.cha1);
        eye = findViewById(R.id.eye);
        forget_pwd = findViewById(R.id.login_forget);
        login = findViewById(R.id.load_login);
        //设置只能输入数字
        phone_input.setInputType(InputType.TYPE_CLASS_NUMBER);
        load_button.setEnabled(false);
        initListener();

        //登录操作
        load_button.setOnClickListener(v -> {
            HashMap<String, String> dataMap = new HashMap<>();
            phone = AES.encrypt(phone_input.getText().toString());
            pwd = AES.encrypt(pwd_input.getText().toString());
            dataMap.put("phone", phone);
            dataMap.put("pwd", pwd);
            new Thread(() -> {
                String state = HttpUtils.sendPostMessage(dataMap, "UTF-8", "load");
                runOnUiThread(new Thread(() -> {
                    if (!isPhone(phone_input.getText().toString())) {
                        notice.setText("请输入正确的手机号！");
                    }else{
                        switch (state) {
                            case "111":
                                notice.setText("登录成功！");
                                break;
                            case "110":
                                notice.setText("密码错误！");
                                break;
                            case "100":
                                notice.setText("用户不存在！");
                                break;
                            default:
                                notice.setText("未知错误！");
                                break;
                        }
                    }
                }));
            }).start();

        });
        //跳转修改密码界面
        forget_pwd.setOnClickListener(v -> {
            Intent intent = new Intent(loadActivity.this, forgetPwdActivity.class);
            startActivity(intent);
        });
        //跳转注册界面
        login.setOnClickListener(v -> {
            Intent intent = new Intent(loadActivity.this, loginActivity.class);
            startActivity(intent);
        });
        //一键手机号置空
        cha.setOnClickListener(v -> {
            phone_input.setText("");
        });
        //设置密码可见性
        eye.setOnTouchListener((v, event) -> {
            if (true) {
                eye.setImageResource(R.drawable.eye_open);
                pwd_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye.setSelected(false);
                setSelectionEnd(pwd_input);
            }
            return false;
        });
        eye.setOnClickListener(v -> {
            if (eye.isSelected()) {
                eye.setImageResource(R.drawable.eye_open);
                pwd_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye.setSelected(false);
                setSelectionEnd(pwd_input);
            } else {
                eye.setImageResource(R.drawable.eye_close);
                pwd_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                eye.setSelected(true);
                setSelectionEnd(pwd_input);
            }
        });


    }

    //正则表达式判断是不是手机号
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }
    //判断密码是否规范

    //设置光标在末尾
    public static void setSelectionEnd(EditText editText) {
        Editable b = editText.getText();
        editText.setSelection(b.length());
    }

    private  void initListener() {
        TextChanger textChanger = new TextChanger();
        phone_input.addTextChangedListener(textChanger);
        pwd_input.addTextChangedListener(textChanger);
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
            int ph = phone_input.length(), pw = pwd_input.length();
            if (ph == 0 && pw == 0) {
                cha.setVisibility(View.INVISIBLE);
                eye.setVisibility(View.INVISIBLE);
                load_button.setEnabled(false);
            } else if (ph != 0 && pw == 0) {
                cha.setVisibility(View.VISIBLE);
                eye.setVisibility(View.INVISIBLE);
                load_button.setEnabled(false);
            } else if (ph == 0 && pw != 0) {
                cha.setVisibility(View.INVISIBLE);
                eye.setVisibility(View.VISIBLE);
                load_button.setEnabled(false);
            } else {
                cha.setVisibility(View.VISIBLE);
                eye.setVisibility(View.VISIBLE);
                load_button.setEnabled(true);
            }
        }
    }
    //定义内部类的handler解决警报问题（关于潜在的内存泄漏问题）
    static class MyHandler extends Handler{
        WeakReference<Activity> mActivity;
        MyHandler(Activity activity){
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    }


}