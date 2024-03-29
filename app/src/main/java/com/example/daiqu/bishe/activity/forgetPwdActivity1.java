package com.example.daiqu.bishe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.returnState;
import com.example.daiqu.bishe.tool.tool;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class forgetPwdActivity1 extends Activity {
    private EditText forget_phone, forget_pwd, mibao1, mibao2,forget_pwd_verify;
    private RelativeLayout layout_mibao1, layout_mibao2, layout_pwd,layout_pwd_verify;
    private Button  forget_btn;
    private ImageView cha2, eye1, cha3, cha4, search_mibao_img,eye4;
    private TextView noticeMsg1,noticeMsg2,noticeMsg5;
    private final MyHandler myHandler = new MyHandler(this);

    //定义内部类的handler解决警报问题（关于潜在的内存泄漏问题）
    private static class MyHandler extends Handler {
        private final WeakReference<forgetPwdActivity1> mActivity;

        public MyHandler(forgetPwdActivity1 activity) {
            mActivity = new WeakReference<>(activity);
        }

        /**
         * 关于特定下列msg含义：<000,用户不存在>；<200，未设置密保>；<220,密保2不存在>;<202,密保1不存在>;<222,两个密保都有>
         * <fg_e,密码更新操作出问题（数据库部分）>;<fg_1,忘记的密码更新成功>;<fg_0,密保答案错误>
         */
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            forgetPwdActivity1 myActivity = mActivity.get();

            if(msg.what==1){
                List<String> list = (List<String>) msg.obj;
                Log.d("listt",list.toString());
                String head = list.get(0),body = list.get(1);
                switch (head) {
                    case returnState.user_not_exit:
                        myActivity.noticeMsg1.setText("不存在该用户！");
                        myActivity.search_mibao_img.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd.setVisibility(View.GONE);
                        myActivity.layout_mibao1.setVisibility(View.GONE);
                        myActivity.layout_mibao2.setVisibility(View.GONE);
                        myActivity.layout_pwd_verify.setVisibility(View.GONE);
                        myActivity.forget_btn.setVisibility(View.GONE);
                        break;
                    case returnState.mibao_not_exit:
                        myActivity.noticeMsg1.setText("您未设置密保，不能重置密码！");
                        myActivity.search_mibao_img.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd.setVisibility(View.GONE);
                        myActivity.layout_mibao1.setVisibility(View.GONE);
                        myActivity.layout_mibao2.setVisibility(View.GONE);
                        myActivity.layout_pwd_verify.setVisibility(View.GONE);
                        myActivity.forget_btn.setVisibility(View.GONE);
                        break;
                    case returnState.mibao2_not_exit:
                        myActivity.noticeMsg1.setText("");
                        myActivity.forget_phone.setEnabled(false);
                        myActivity.cha2.setVisibility(View.INVISIBLE);
                        myActivity.search_mibao_img.setVisibility(View.GONE);
                        myActivity.layout_pwd.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd_verify.setVisibility(View.VISIBLE);
                        myActivity.layout_mibao1.setVisibility(View.VISIBLE);
                        myActivity.mibao1.setHint("(密保1)"+AES.decrypt(list.get(1)));
                        myActivity.layout_mibao2.setVisibility(View.GONE);
                        myActivity.forget_btn.setVisibility(View.VISIBLE);
                        break;
                    case returnState.mibao1_not_exit:
                        myActivity.noticeMsg1.setText("");
                        myActivity.forget_phone.setEnabled(false);
                        myActivity.cha2.setVisibility(View.INVISIBLE);
                        myActivity.search_mibao_img.setVisibility(View.GONE);
                        myActivity.layout_pwd.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd_verify.setVisibility(View.VISIBLE);
                        myActivity.layout_mibao1.setVisibility(View.GONE);
                        myActivity.layout_mibao2.setVisibility(View.VISIBLE);
                        myActivity.mibao2.setHint("(密保2)："+AES.decrypt(list.get(1)));
                        myActivity.forget_btn.setVisibility(View.VISIBLE);
                        break;
                    case returnState.mibao_exit:
                        myActivity.noticeMsg1.setText("");
                        myActivity.forget_phone.setEnabled(false);
                        myActivity.cha2.setVisibility(View.INVISIBLE);
                        myActivity.search_mibao_img.setVisibility(View.GONE);
                        myActivity.layout_pwd.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd_verify.setVisibility(View.VISIBLE);
                        myActivity.layout_mibao1.setVisibility(View.VISIBLE);
                        myActivity.mibao1.setHint("(密保1)："+AES.decrypt(body.substring(0,body.indexOf("/"))));
                        myActivity.layout_mibao2.setVisibility(View.VISIBLE);
                        myActivity.mibao2.setHint("(密保2)："+AES.decrypt(body.substring(body.indexOf("/")+1)));
                        myActivity.forget_btn.setVisibility(View.VISIBLE);
                        break;
                    default:
                        myActivity.noticeMsg1.setText("未知错误");
                        myActivity.search_mibao_img.setVisibility(View.VISIBLE);
                        myActivity.layout_pwd.setVisibility(View.GONE);
                        myActivity.layout_mibao1.setVisibility(View.GONE);
                        myActivity.layout_mibao2.setVisibility(View.GONE);
                        myActivity.forget_btn.setVisibility(View.GONE);
                        break;
                }
            }else if(msg.what==2){
                String state = msg.obj.toString();
                switch (state){
                    case "fg_1":
                        Toast.makeText(myActivity.getApplicationContext(),"更新成功！请返回重新登陆一下吧(￣▽￣)",Toast.LENGTH_SHORT).show();
                        break;
                    case "fg_0":
                        Toast.makeText(myActivity.getApplicationContext(),"密保答案错误！Σ(っ °Д °;)っ",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(myActivity.getApplicationContext(),"服务器错误！＞﹏＜",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.forget_pwd_havemibao);
        forget_phone = findViewById(R.id.forget_phone);
        forget_pwd = findViewById(R.id.forget_pwd);
        mibao1 = findViewById(R.id.mibao1);
        mibao2 = findViewById(R.id.mibao2);
        cha2 = findViewById(R.id.cha2);
        cha3 = findViewById(R.id.cha3);
        cha4 = findViewById(R.id.cha4);
        eye1 = findViewById(R.id.eye1);
        layout_pwd = findViewById(R.id.layout_pwd);
        layout_mibao1 = findViewById(R.id.layout_mibao1);
        layout_mibao2 = findViewById(R.id.layout_mibao2);
        search_mibao_img = findViewById(R.id.search_mibao_btn);
        forget_btn = findViewById(R.id.forget_btn);
        noticeMsg1 = findViewById(R.id.noticeMsg1);
        noticeMsg2 = findViewById(R.id.noticeMsg2);
        layout_pwd_verify = findViewById(R.id.layout_pwd_verify);
        forget_pwd_verify = findViewById(R.id.forget_pwd_verify);
        eye4 = findViewById(R.id.eye4);
        noticeMsg5 = findViewById(R.id.noticeMsg5);
        initListener();
        //通过手机号查询是否设置了密保
        search_mibao_img.setEnabled(false);
        search_mibao_img.setOnClickListener(v -> {
            HashMap<String, String> dataMap = new HashMap<>();
            String phone = AES.encrypt(forget_phone.getText().toString());
            dataMap.put("phone", phone);
            if (!loadActivity.isPhone(forget_phone.getText().toString())) {
                noticeMsg1.setText("请输入正确的手机号！");
            } else {
                new Thread(() -> {
                    String state = HttpUtils.sendPostMessage(dataMap, "UTF-8", "mibaoExit");
                    Log.d("statett",state);
                    //List<String> list = Arrays.asList(state);
                    List<String> List = JSONArray.parseArray(state,String.class);
                    Message msg = new Message();
                    msg.obj = List;
                    myHandler.sendMessage(msg);
                    msg.what=1;
                }).start();
            }

        });
        //提交密保答案并修改密码
        forget_btn.setOnClickListener(v -> {
            if(!tool.isPwd(forget_pwd.getText().toString())){
                noticeMsg2.setText("注意：密码由6~15位数字字母组合而成！");
            }else {
                if (!forget_pwd.getText().toString().equals(forget_pwd_verify.getText().toString())) {
                    noticeMsg5.setText("两次密码输入不一致，请核对！");
                } else {
                    noticeMsg2.setText("");
                    noticeMsg5.setText("");
                    forget_btn.setEnabled(true);
                    HashMap<String, String> map = new HashMap<>();
                    String phone = AES.encrypt(forget_phone.getText().toString());
                    String answer1 = AES.encrypt(mibao1.getText().toString());
                    String answer2 = AES.encrypt(mibao2.getText().toString());
                    String pwd = AES.encrypt(forget_pwd.getText().toString());
                    map.put("phone", phone);
                    map.put("answer1", answer1);
                    map.put("answer2", answer2);
                    map.put("pwd", pwd);
                    new Thread(() -> {
                        String state = HttpUtils.sendPostMessage(map, "UTF-8", "updateForgetPwd");
                        Message msg = new Message();
                        msg.obj = state;
                        myHandler.sendMessage(msg);
                        msg.what = 2;
                    }).start();
                }
            }
        });
        //设置一键清空输入栏
        cha2.setOnClickListener(v -> forget_phone.setText(""));
        cha3.setOnClickListener(v -> mibao1.setText(""));
        cha4.setOnClickListener(v -> mibao2.setText(""));
        //设置密码可见性
        eye1.setOnClickListener(v -> {
            if (!eye1.isSelected()) {
                //eye1.setImageResource(R.drawable.eye_open);
                eye1.setSelected(true);
                forget_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(forget_pwd);
            } else {
                //eye1.setImageResource(R.drawable.eye_close);
                eye1.setSelected(false);
                forget_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(forget_pwd);
            }
        });
        //设置密码可见性
        eye4.setOnClickListener(v -> {
            if (!eye4.isSelected()) {
                eye4.setImageResource(R.drawable.eye_open);
                forget_pwd_verify.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eye4.setSelected(true);
                loadActivity.setSelectionEnd(forget_pwd_verify);
            } else {
                eye4.setImageResource(R.drawable.eye_close);
                forget_pwd_verify.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eye4.setSelected(false);
                loadActivity.setSelectionEnd(forget_pwd_verify);
            }
        });
    }

    private void initListener() {
        TextChanger textChanger = new TextChanger();
        forget_phone.addTextChangedListener(textChanger);
        forget_pwd.addTextChangedListener(textChanger);
        forget_pwd_verify.addTextChangedListener(textChanger);
        mibao1.addTextChangedListener(textChanger);
        mibao2.addTextChangedListener(textChanger);
    }
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //重写编辑栏监听部分，实现按钮颜色变换,部分图标控件的可视化
    class TextChanger implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int ph = forget_phone.length(), pw = forget_pwd.length(), mb1 = mibao1.length(), mb2 = mibao2.length(),pw_v = forget_pwd_verify.length();
            if (ph == 0 || !forget_phone.isEnabled()) {
                cha2.setVisibility(View.INVISIBLE);
                search_mibao_img.setEnabled(false);
            } else {
                cha2.setVisibility(View.VISIBLE);
                search_mibao_img.setEnabled(true);
            }
            if (pw == 0) {
                eye1.setVisibility(View.INVISIBLE);
            } else {
                eye1.setVisibility(View.VISIBLE);
            }
            if (mb1 == 0) {
                cha3.setVisibility(View.INVISIBLE);
            } else {
                cha3.setVisibility(View.VISIBLE);
            }
            if (mb2 == 0) {
                cha4.setVisibility(View.INVISIBLE);
            } else {
                cha4.setVisibility(View.VISIBLE);
            }
            if(pw_v == 0){
                eye4.setVisibility(View.INVISIBLE);
            }else {
                eye4.setVisibility(View.VISIBLE);
            }
            //设置按钮的可点击性
            forget_btn.setEnabled(ph != 0 && pw >= 6 && pw <= 15 && (mb1 != 0 || mb2 != 0) && pw_v != 0);
        }
    }

}
