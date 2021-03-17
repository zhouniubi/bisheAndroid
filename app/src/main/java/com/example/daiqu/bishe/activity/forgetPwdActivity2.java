package com.example.daiqu.bishe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.daiqu.R;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.tool;
import com.mob.MobSDK;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class forgetPwdActivity2 extends Activity {
    protected String AppKey = "32248c7149128", AppSecret = "3c4985489d3ac7c3f6d9c33a84f46c6b";
    private EditText login_phone_forget, login_pwd_forget, login_pwd_verify_forget, yanzheng_code_forget;
    private ImageView cha9, eye5, eye6, cha8;
    private Button getyanzheng_forget, login_btn_forget;
    private TextView noticeMsglogin_phone_forget,noticeMsglogin_pwd_forget,noticeMsglogin_pwd_verify_forget,noticeMsglogin_yanzheng_forget;
    MyHandler myHandler = new MyHandler(this);
    //定义内部类的handler解决警报问题（关于潜在的内存泄漏问题）
    private static class MyHandler extends Handler {
        private final WeakReference<forgetPwdActivity2> mActivity;

        public MyHandler(forgetPwdActivity2 activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            forgetPwdActivity2 myActivity = mActivity.get();
           /* msg.what=1:处理短信验证码等相关的问题；
              msg.what=2:处理修改结果*/
            if (msg.what == 1) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                // 短信注册成功
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    } else {
                        // TODO 处理错误的结果
                        ((Throwable) data).printStackTrace();
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        String phone = AES.encrypt(myActivity.login_phone_forget.getText().toString());
                        String pwd = AES.encrypt(myActivity.login_pwd_forget.getText().toString());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("phone", phone);
                        map.put("pwd", pwd);
                        new Thread(() -> {
                            String state = HttpUtils.sendPostMessage(map, "UTF-8", "updateForgetPwd2");
                            Message msg1 = new Message();
                            msg1.obj = state;
                            myActivity.myHandler.sendMessage(msg1);
                            msg1.what = 2;
                        }).start();
                    } else {
                        // TODO 处理错误的结果
                        Toast.makeText(myActivity, "验证码错误", Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            } else if (msg.what == 2) {
                String state = msg.obj.toString();
                if (state.equals("fg_1")) {
                    Toast.makeText(myActivity, "修改成功啦，请牢牢记住密码哦", Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(myActivity, "数据库出错啦，请稍后重试＞︿＜", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            msg.what = 1;
            myHandler.sendMessage(msg);
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.forget_pwd_nomibao);
        login_phone_forget = findViewById(R.id.login_phone_forget);
        login_pwd_forget = findViewById(R.id.login_pwd_forget);
        login_pwd_verify_forget = findViewById(R.id.login_pwd_verify_forget);
        yanzheng_code_forget = findViewById(R.id.yanzheng_code_forget);
        cha8 = findViewById(R.id.cha8);
        cha9 = findViewById(R.id.cha9);
        eye5 = findViewById(R.id.eye5);
        eye6 = findViewById(R.id.eye6);
        getyanzheng_forget = findViewById(R.id.getyanzheng_forget);
        login_btn_forget = findViewById(R.id.login_btn_forget);
        noticeMsglogin_phone_forget = findViewById(R.id.noticeMsglogin_phone_forget);
        noticeMsglogin_pwd_forget = findViewById(R.id.noticeMsglogin_pwd_forget);
        noticeMsglogin_pwd_verify_forget = findViewById(R.id.noticeMsglogin_pwd_verify_forget);
        noticeMsglogin_yanzheng_forget = findViewById(R.id.noticeMsglogin_yanzheng_forget);
        initListener();
        login_btn_forget.setEnabled(false);
        MobSDK.init(this, AppKey, AppSecret);
        SMSSDK.registerEventHandler(eventHandler);
        getyanzheng_forget.setOnClickListener(v -> {
            if (!tool.isPhone(login_phone_forget.getText().toString())) {
                noticeMsglogin_phone_forget.setText("手机号码格式错误！");
            } else {
                noticeMsglogin_phone_forget.setText("");
                countDown(30);
                Toast.makeText(this, "请注意查收验证码（30s后可以重新获得）！", Toast.LENGTH_SHORT).show();
                //通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", login_phone_forget.getText().toString());
            }
        });
        login_btn_forget.setOnClickListener(v -> {
            //判断手机号与密码格式是否规范
            if (!tool.isPhone(login_phone_forget.getText().toString())) {
                if (!tool.isPwd(login_pwd_forget.getText().toString())) {
                    noticeMsglogin_phone_forget.setText("手机号码格式错误！");
                    noticeMsglogin_pwd_forget.setText("请保证密码为6~15位的数字和字母组成！");
                    noticeMsglogin_pwd_verify_forget.setText("");
                } else {
                    noticeMsglogin_phone_forget.setText("手机号码格式错误！");
                    noticeMsglogin_pwd_forget.setText("");
                    noticeMsglogin_pwd_verify_forget.setText("");
                }
            } else {
                if (!tool.isPwd(login_pwd_forget.getText().toString())) {
                    noticeMsglogin_phone_forget.setText("");
                    noticeMsglogin_pwd_forget.setText("请保证密码为6~15位的数字和字母组成！");
                    noticeMsglogin_pwd_verify_forget.setText("");
                } else {
                    if (!login_pwd_forget.getText().toString().equals(login_pwd_verify_forget.getText().toString())) {
                        noticeMsglogin_phone_forget.setText("");
                        noticeMsglogin_pwd_forget.setText("");
                        noticeMsglogin_pwd_verify_forget.setText("两次密码输入不一致，请核对！");
                    } else {
                        noticeMsglogin_pwd_verify_forget.setText("");
                        noticeMsglogin_phone_forget.setText("");
                        noticeMsglogin_pwd_forget.setText("");
                        //发送验证码并验证
                        SMSSDK.submitVerificationCode("86", login_phone_forget.getText().toString()
                                , yanzheng_code_forget.getText().toString());
                    }
                }
            }
        });
        cha9.setOnClickListener(v -> {
            login_phone_forget.setText("");
        });
        cha8.setOnClickListener(v -> {
            yanzheng_code_forget.setText("");
        });
        //设置密码可见性
       /* eye5.setOnTouchListener((v, event) -> {
            if (true) {
                eye5.setImageResource(R.drawable.eye_open);
                login_pwd_forget.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye5.setSelected(false);
                loadActivity.setSelectionEnd(login_pwd_forget);
            }
            return false;
        });*/
        eye5.setOnClickListener(v -> {
            if (!eye5.isSelected()) {
                //eye5.setImageResource(R.drawable.eye_open);
                eye5.setSelected(true);
                login_pwd_forget.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_forget);
            } else {
                //eye5.setImageResource(R.drawable.eye_close);
                eye5.setSelected(false);
                login_pwd_forget.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_forget);
            }
        });
        //设置密码可见性
       /* eye6.setOnTouchListener((v, event) -> {
            if (true) {
                eye6.setImageResource(R.drawable.eye_open);
                login_pwd_verify_forget.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye6.setSelected(false);
                loadActivity.setSelectionEnd(login_pwd_verify_forget);
            }
            return false;
        });*/
        eye6.setOnClickListener(v -> {
            if (!eye6.isSelected()) {
                eye6.setSelected(true);
                login_pwd_verify_forget.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_verify_forget);
            } else {
                eye6.setSelected(false);
                login_pwd_verify_forget.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_verify_forget);
            }
        });
    }
    private void initListener() {
        TextChanger textChanger = new TextChanger();
        login_phone_forget.addTextChangedListener(textChanger);
        login_pwd_forget.addTextChangedListener(textChanger);
        yanzheng_code_forget.addTextChangedListener(textChanger);
        login_pwd_verify_forget.addTextChangedListener(textChanger);
    }

    //在完成短信验证之后，需要销毁回调监听接口。
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        ActivityCollector.removeActivity(this);
    }
    //设置倒计时
    private void countDown(int time) {
        CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                getyanzheng_forget.setEnabled(false);
                getyanzheng_forget.setText(millisUntilFinished / 1000 + "s");
                getyanzheng_forget.setTextSize(13);
            }

            @Override
            public void onFinish() {
                getyanzheng_forget.setEnabled(true);
                getyanzheng_forget.setText("验证码");
                getyanzheng_forget.setTextSize(13);
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
            int ph = login_phone_forget.length(), pw = login_pwd_forget.length(), code = yanzheng_code_forget.length(), pw_v = login_pwd_verify_forget.length();
            if (ph == 0) {
                cha9.setVisibility(View.INVISIBLE);
            } else {
                cha9.setVisibility(View.VISIBLE);
            }
            if (pw == 0) {
                eye5.setVisibility(View.INVISIBLE);
            } else {
                eye5.setVisibility(View.VISIBLE);
                eye5.requestFocus();
            }
            if (code == 0) {
                cha8.setVisibility(View.INVISIBLE);
            } else {
                cha8.setVisibility(View.VISIBLE);
            }
            if (pw_v == 0) {
                eye6.setVisibility(View.INVISIBLE);
            } else {
                eye6.setVisibility(View.VISIBLE);
                eye6.requestFocus();
            }
            //设置按钮的可点击性
            login_btn_forget.setEnabled(ph != 0 && pw >= 6 && pw <= 15 && code != 0 && pw_v != 0);
        }
    }
}