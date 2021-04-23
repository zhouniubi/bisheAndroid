package com.example.daiqu.bishe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.daiqu.R;
import com.example.daiqu.bishe.TencentUtils.TencentIM;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class loadActivity extends Activity {
    public static loadActivity ldActivity;
    private EditText phone_input, pwd_input;
    private TextView notice, load_forget, load_login, load_introduce;
    private ImageView cha, eye;
    private Button load_button;
    private String phone = "", pwd = "";
    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将当前活动加入管理器进行管理
        ldActivity = this;
        ActivityCollector.addActivity(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        SharedPreferences  sharedPreferences = getSharedPreferences("loadStatePerference", 0);
        String loadState = sharedPreferences.getString("loadState", "null");

        if (loadState.equals("111")) {
            //初始化腾讯服务
            TencentIM.initIm(this);
            //设置自动登录
            Intent intent = new Intent(loadActivity.this, startActivity.class);
            intent.putExtra("phone", sharedPreferences.getString("phone", "null"));
            Log.d("sharedPhone", sharedPreferences.getString("phone", "null"));
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_load);
            phone_input = findViewById(R.id.input_phone);
            pwd_input = findViewById(R.id.input_pwd);
            load_button = findViewById(R.id.load_btn);
            notice = findViewById(R.id.noticeMsg);
            cha = findViewById(R.id.cha1);
            eye = findViewById(R.id.eye);

            load_forget = findViewById(R.id.load_forget);
            load_login = findViewById(R.id.load_login);
            load_introduce = findViewById(R.id.load_introduce);
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
                        } else {
                            switch (state) {
                                case "111":
                                    //初始化腾讯服务
                                    TencentIM.initIm(this);
                                    Intent intent = new Intent(loadActivity.this, startActivity.class);
                                    intent.putExtra("phone", phone);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("loadState", "111");
                                    editor.putString("phone", phone);
                                    editor.apply();
                                    startActivity(intent);
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
            //跳转忘记密码界面（先弹出dialog选择验证方式）
            load_forget.setOnClickListener(v -> {
                Dialog dialog = new Dialog(this, R.style.introduce_dialog);
                dialog.setContentView(R.layout.forget_choose_way);
                dialog.show();
                dialog.getWindow().findViewById(R.id.have_mibao).setOnClickListener(v1 -> {
                    dialog.cancel();
                    Intent intent = new Intent(loadActivity.this, forgetPwdActivity1.class);
                    startActivity(intent);
                });
                dialog.getWindow().findViewById(R.id.no_mibao).setOnClickListener(v1 -> {
                    Intent intent = new Intent(loadActivity.this, forgetPwdActivity2.class);
                    startActivity(intent);
                    dialog.cancel();

                });
                dialog.getWindow().findViewById(R.id.layout_mibao_choose).setOnClickListener(v1 -> {
                    dialog.cancel();
                });

            });
            //跳转注册界面
            load_login.setOnClickListener(v -> {
                Intent intent = new Intent(loadActivity.this, loginActivity.class);
                startActivity(intent);
            });
            //弹出介绍对话框
            load_introduce.setOnClickListener(v -> {
                Dialog dialog = new Dialog(this, R.style.introduce_dialog);
                dialog.setContentView(R.layout.dialog_introduce);
                dialog.getWindow().findViewById(R.id.layout_introduce).setOnClickListener(v1 -> {
                    dialog.cancel();
                });
                dialog.show();

            });
            //一键手机号置空
            cha.setOnClickListener(v -> {
                phone_input.setText("");
            });
            //设置密码可见性
            eye.setOnClickListener(v -> {
                if (!eye.isSelected()) {
                    pwd_input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setSelectionEnd(pwd_input);
                    eye.setSelected(true);
                } else {
                    pwd_input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setSelectionEnd(pwd_input);
                    eye.setSelected(false);
                }
            });


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*ActivityCollector.removeActivity(this);*/
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

    private void initListener() {
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
                eye.requestFocus();
                load_button.setEnabled(false);
            } else {
                cha.setVisibility(View.VISIBLE);
                eye.setVisibility(View.VISIBLE);
                eye.requestFocus();
                load_button.setEnabled(true);
            }
        }
    }

    //定义内部类的handler解决警报问题（关于潜在的内存泄漏问题）
    static class MyHandler extends Handler {
        WeakReference<Activity> mActivity;

        MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    }


}