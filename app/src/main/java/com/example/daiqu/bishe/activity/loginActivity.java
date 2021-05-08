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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.daiqu.R;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.returnState;
import com.example.daiqu.bishe.tool.tool;
import com.mob.MobSDK;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class loginActivity extends Activity {
    private String sex = "1", identity = "1";
    protected String AppKey = "32248c7149128", AppSecret = "3c4985489d3ac7c3f6d9c33a84f46c6b";

    //定义内部类的handler解决警报问题（关于潜在的内存泄漏问题）
    private static class MyHandler extends Handler {
        private final WeakReference<loginActivity> mActivity;

        public MyHandler(loginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            loginActivity myActivity = mActivity.get();
           /* msg.what=1:处理短信验证码等相关的问题；
              msg.what=2:处理注册结果*/
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
                        String phone = AES.encrypt(myActivity.login_phone.getText().toString());
                        String name = AES.encrypt(myActivity.login_name.getText().toString());
                        String pwd = AES.encrypt(myActivity.login_pwd.getText().toString());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("phone", phone);
                        map.put("name", name);
                        map.put("pwd", pwd);
                        map.put("sex", myActivity.sex);
                        map.put("identity", myActivity.identity);
                        new Thread(() -> {
                            String state = HttpUtils.sendPostMessage(map, "UTF-8", "login");
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
                if (state.equals(returnState.insert_success)) {
                    Toast.makeText(myActivity, "注册成功啦，请返回登录界面(￣▽￣)", Toast.LENGTH_SHORT).show();

                } else if (state.equals(returnState.insert_fail)) {
                    Toast.makeText(myActivity, "该手机号已被注册，请更换手机号呢（；´д｀）ゞ", Toast.LENGTH_SHORT).show();
                } else {
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

    private EditText login_phone, login_pwd, yanzheng_code, login_name, login_pwd_verify;
    private ImageView cha5, eye2, cha6, cha7, eye3;
    private Button login_btn, getyanzheng;
    private RadioGroup login_radioGropSex, login_radioGropIdentity;
    private RadioButton radio_bt_boy, radio_bt_girl, radio_bt_teacher, radio_bt_student;
    private TextView text_boy, text_girl, noticeMsglogin_phone, noticeMsglogin_pwd, noticeMsglogin_yanzheng, noticeMsglogin_name, noticeMsglogin_pwd_verify;

    private final MyHandler myHandler = new MyHandler(this);


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
        setContentView(R.layout.activity_login);

        login_phone = findViewById(R.id.login_phone);
        login_pwd = findViewById(R.id.login_pwd);
        yanzheng_code = findViewById(R.id.yanzheng_code);
        cha5 = findViewById(R.id.cha5);
        eye2 = findViewById(R.id.eye2);
        login_btn = findViewById(R.id.login_btn);
        login_radioGropSex = findViewById(R.id.login_radioGropSex);
        radio_bt_boy = findViewById(R.id.radio_bt_boy);
        radio_bt_girl = findViewById(R.id.radio_bt_girl);
        text_boy = findViewById(R.id.text_boy);
        text_girl = findViewById(R.id.text_girl);
        login_radioGropIdentity = findViewById(R.id.login_radioGropIdentity);
        radio_bt_teacher = findViewById(R.id.radio_bt_teacher);
        radio_bt_student = findViewById(R.id.radio_bt_student);
        getyanzheng = findViewById(R.id.getyanzheng);
        noticeMsglogin_phone = findViewById(R.id.noticeMsglogin_phone);
        noticeMsglogin_pwd = findViewById(R.id.noticeMsglogin_pwd);
        noticeMsglogin_yanzheng = findViewById(R.id.noticeMsglogin_yanzheng);
        login_name = findViewById(R.id.login_name);
        cha6 = findViewById(R.id.cha6);
        cha7 = findViewById(R.id.cha7);
        noticeMsglogin_name = findViewById(R.id.noticeMsglogin_name);
        eye3 = findViewById(R.id.eye3);
        login_pwd_verify = findViewById(R.id.login_pwd_verify);
        noticeMsglogin_pwd_verify = findViewById(R.id.noticeMsglogin_pwd_verify);
        initListener();
        login_btn.setEnabled(false);
        radio_bt_boy.setChecked(true);
        radio_bt_girl.setChecked(false);
        text_boy.setEnabled(true);
        text_girl.setEnabled(false);
        radio_bt_teacher.setChecked(true);
        radio_bt_student.setChecked(false);
        MobSDK.init(this, AppKey, AppSecret);
        SMSSDK.registerEventHandler(eventHandler);


        //设置用户的性别，男生为1，女生为0
        login_radioGropSex.setOnCheckedChangeListener((group, checkedId) -> {
            if (radio_bt_boy.isChecked()) {
                radio_bt_girl.setChecked(false);
                sex = "1";
            }
            if (radio_bt_girl.isChecked()) {
                radio_bt_boy.setChecked(false);
                sex = "0";
            }
        });
        radio_bt_boy.setOnClickListener(v -> {
            changeTextColor();
        });
        radio_bt_girl.setOnClickListener(v -> {
            changeTextColor();
        });
        //设置用户的身份，教师为1，学生为0
        login_radioGropIdentity.setOnCheckedChangeListener((group, checkedId) -> {
            if (radio_bt_teacher.isChecked()) {
                radio_bt_student.setChecked(false);
                identity = "1";
            }
            if (radio_bt_student.isChecked()) {
                radio_bt_teacher.setChecked(false);
                identity = "0";
            }
        });
        getyanzheng.setOnClickListener(v -> {
            if (!tool.isPhone(login_phone.getText().toString())) {
                noticeMsglogin_phone.setText("手机号码格式错误！");
            } else {
                noticeMsglogin_phone.setText("");
                countDown(30);
                Toast.makeText(this, "请注意查收验证码（30s后可以重新获得）！", Toast.LENGTH_SHORT).show();
                //通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", login_phone.getText().toString());
            }
        });
        login_btn.setOnClickListener(v -> {
            //判断手机号与密码格式是否规范
            if (!tool.isPhone(login_phone.getText().toString())) {
                if (!tool.isPwd(login_pwd.getText().toString())) {
                    noticeMsglogin_phone.setText("手机号码格式错误！");
                    noticeMsglogin_pwd.setText("请保证密码为6~15位的数字和字母组成！");
                    noticeMsglogin_pwd_verify.setText("");
                } else {
                    noticeMsglogin_phone.setText("手机号码格式错误！");
                    noticeMsglogin_pwd.setText("");
                    noticeMsglogin_pwd_verify.setText("");
                }
            } else {
                if (!tool.isPwd(login_pwd.getText().toString())) {
                    noticeMsglogin_phone.setText("");
                    noticeMsglogin_pwd.setText("请保证密码为6~15位的数字和字母组成！");
                    noticeMsglogin_pwd_verify.setText("");
                } else {
                    if (!login_pwd.getText().toString().equals(login_pwd_verify.getText().toString())) {
                        noticeMsglogin_phone.setText("");
                        noticeMsglogin_pwd.setText("");
                        noticeMsglogin_pwd_verify.setText("两次密码输入不一致，请核对！");
                    } else {
                        noticeMsglogin_pwd_verify.setText("");
                        noticeMsglogin_phone.setText("");
                        noticeMsglogin_pwd.setText("");
                        //发送验证码并验证
                        SMSSDK.submitVerificationCode("86", login_phone.getText().toString()
                                , yanzheng_code.getText().toString());
                    }
                }
            }
        });
        cha5.setOnClickListener(v -> {
            login_phone.setText("");
        });
        cha6.setOnClickListener(v -> {
            login_name.setText("");
        });
        cha7.setOnClickListener(v -> {
            yanzheng_code.setText("");
        });
        //设置密码可见性
        /*eye2.setOnTouchListener((v, event) -> {
            if (true) {
                eye2.setImageResource(R.drawable.eye_open);
                login_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye2.setSelected(false);
                loadActivity.setSelectionEnd(login_pwd);
            }
            return false;
        });*/
        eye2.setOnClickListener(v -> {
            if (!eye2.isSelected()) {
                //eye2.setImageResource(R.drawable.eye_open);
                eye2.setSelected(true);
                login_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd);
            } else {
                //eye2.setImageResource(R.drawable.eye_close);
                eye2.setSelected(false);
                login_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd);
            }
        });
        //设置密码可见性
     /*   eye3.setOnTouchListener((v, event) -> {
            if (true) {
                eye3.setImageResource(R.drawable.eye_open);
                login_pwd_verify.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eye3.setSelected(false);
                loadActivity.setSelectionEnd(login_pwd_verify);
            }
            return false;
        });*/
        eye3.setOnClickListener(v -> {
            if (!eye3.isSelected()) {
                //eye3.setImageResource(R.drawable.eye_open);
                eye3.setSelected(true);
                login_pwd_verify.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_verify);
            } else {
                //eye3.setImageResource(R.drawable.eye_close);
                eye3.setSelected(false);
                login_pwd_verify.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(login_pwd_verify);
            }
        });
    }

    private void initListener() {
        TextChanger textChanger = new TextChanger();
        login_phone.addTextChangedListener(textChanger);
        login_pwd.addTextChangedListener(textChanger);
        yanzheng_code.addTextChangedListener(textChanger);
        login_name.addTextChangedListener(textChanger);
        login_pwd_verify.addTextChangedListener(textChanger);

    }

    //设置选择文本的颜色（通过可使用状态来转换）
    private void changeTextColor() {
        if (radio_bt_boy.isChecked()) {
            text_boy.setEnabled(true);
            text_girl.setEnabled(false);
        }
        if (radio_bt_girl.isChecked()) {
            text_boy.setEnabled(false);
            text_girl.setEnabled(true);
        }
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
                getyanzheng.setEnabled(false);
                getyanzheng.setText(millisUntilFinished / 1000 + "s");
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
            int ph = login_phone.length(), pw = login_pwd.length(), code = yanzheng_code.length(), pn = login_name.length(), pw_v = login_pwd_verify.length();
            if (ph == 0) {
                cha5.setVisibility(View.INVISIBLE);
            } else {
                cha5.setVisibility(View.VISIBLE);
            }
            if (pn == 0) {
                cha6.setVisibility(View.INVISIBLE);
            } else {
                cha6.setVisibility(View.VISIBLE);
            }
            if (pw == 0) {
                eye2.setVisibility(View.INVISIBLE);
            } else {
                eye2.setVisibility(View.VISIBLE);
                eye2.requestFocus();
            }
            if (code == 0) {
                cha7.setVisibility(View.INVISIBLE);
            } else {
                cha7.setVisibility(View.VISIBLE);
            }
            if (pw_v == 0) {
                eye3.setVisibility(View.INVISIBLE);
            } else {
                eye3.setVisibility(View.VISIBLE);
                eye3.requestFocus();
            }
            //设置按钮的可点击性
            login_btn.setEnabled(ph != 0 && pw >= 6 && pw <= 15 && code != 0 && pn != 0 && pw_v != 0);
        }
    }
}