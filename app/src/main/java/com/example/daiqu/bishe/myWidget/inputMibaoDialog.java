package com.example.daiqu.bishe.myWidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.data.mibaoData;
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.tool;

import java.util.HashMap;
import java.util.Map;

public class inputMibaoDialog extends Dialog {
    private Context context;
    private userData uData;
    private Activity activity;
    private mibaoData mbData;
    private Dialog dialog;
    private Button mibao_commit;
    private ImageView cha12, cha10, cha11, cha13, cha14;
    private EditText mibao1_qs_input, mibao1_as_input, mibao2_qs_input, mibao2_as_input;

    public inputMibaoDialog(@NonNull Context context, Activity activity, mibaoData mbData) {
        super(context, R.style.add_dialog);
        this.context = context;
        this.activity = activity;
        this.mbData = mbData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mibao_show);
        onInitWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        onClick();
    }

    protected void onInitWidget() {
        cha12 = findViewById(R.id.cha12);
        cha10 = findViewById(R.id.cha10);
        cha11 = findViewById(R.id.cha11);
        cha13 = findViewById(R.id.cha13);
        cha14 = findViewById(R.id.cha14);
        mibao1_qs_input = findViewById(R.id.mibao1_qs_input);
        mibao1_as_input = findViewById(R.id.mibao1_as_input);
        mibao2_qs_input = findViewById(R.id.mibao2_qs_input);
        mibao2_as_input = findViewById(R.id.mibao2_as_input);
        mibao_commit = findViewById(R.id.mibao_commit);
        TextChanger textChanger = new TextChanger();
        mibao1_qs_input.addTextChangedListener(textChanger);
        mibao1_as_input.addTextChangedListener(textChanger);
        mibao2_qs_input.addTextChangedListener(textChanger);
        mibao2_as_input.addTextChangedListener(textChanger);

    }

    protected void onClick() {
        cha12.setOnClickListener(v -> {
            inputMibaoDialog.super.cancel();
        });
        cha10.setOnClickListener(v -> {
            mibao1_qs_input.setText("");
        });
        cha11.setOnClickListener(v -> {
            mibao2_qs_input.setText("");
        });
        cha13.setOnClickListener(v -> {
            mibao1_as_input.setText("");
        });
        cha14.setOnClickListener(v -> {
            mibao2_as_input.setText("");
        });
        mibao_commit.setOnClickListener(v -> {
            showProcess();
            String qs1 = mibao1_qs_input.getText().toString();
            String as1 = mibao1_as_input.getText().toString();
            String qs2 = mibao2_qs_input.getText().toString();
            String as2 = mibao2_as_input.getText().toString();
            int mb1_qs = mibao1_qs_input.length();
            int mb1_as = mibao1_as_input.length();
            int mb2_qs = mibao2_qs_input.length();
            int mb2_as = mibao2_as_input.length();
            if((mb1_qs==0&&mb1_as!=0)||(mb1_qs!=0&&mb1_as==0)||(mb2_qs==0&&mb2_as!=0)||(mb2_qs!=0&&mb2_as==0)){
                Toast.makeText(context, "密保信息不完善！", Toast.LENGTH_SHORT).show();
                closeProcess();
            }else if(mb1_qs==0&&mb1_as==0&&mb2_qs==0&&mb2_as==0){
                Toast.makeText(context, "不可以不设置密保！", Toast.LENGTH_SHORT).show();
                closeProcess();
            } else{
                new Thread(() -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", getPhone());
                    if (mb1_qs==0 && mb2_qs!=0) {
                        map.put("mibao1", "");
                        map.put("answer1", "");
                        map.put("mibao2", AES.encrypt(qs2));
                        map.put("answer2", AES.encrypt(as2));
                    } else if (mb1_qs!=0 && mb2_qs==0) {
                        map.put("mibao1", AES.encrypt(qs1));
                        map.put("answer1", AES.encrypt(as1));
                        map.put("mibao2", "");
                        map.put("answer2", "");
                    } else {
                        map.put("mibao1", AES.encrypt(qs1));
                        map.put("mibao2", AES.encrypt(qs2));
                        map.put("answer1", AES.encrypt(as1));
                        map.put("answer2", AES.encrypt(as2));
                    }
                    String state = HttpUtils.sendPostMessage(map, "UTF-8", "insertMibao");
                    if (state.equals("-999")) {
                        Looper.prepare();
                        closeProcess();
                        Toast.makeText(context, "密保更新失败(网络连接异常)！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else if (state.equals("1")) {
                        Looper.prepare();
                        closeProcess();
                        Toast.makeText(context, "密保更新成功！", Toast.LENGTH_SHORT).show();
                        inputMibaoDialog.super.cancel();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        closeProcess();
                        Toast.makeText(context, "密保更新失败(数据库异常)！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }

    protected void initView() {
        activity.runOnUiThread(() -> {
            if (!mbData.getMibao1().equals("null")) {
                mibao1_qs_input.setText(AES.decrypt(mbData.getMibao1()));
            } else {
                mibao1_qs_input.setText("");
            }
            if (!mbData.getAnswer1().equals("null")) {
                mibao1_as_input.setText(AES.decrypt(mbData.getAnswer1()));
            } else {
                mibao1_as_input.setText("");
            }
            if (!mbData.getMibao2().equals("null")) {
                mibao2_qs_input.setText(AES.decrypt(mbData.getMibao2()));
            } else {
                mibao2_qs_input.setText("");
            }
            if (!mbData.getAnswer2().equals("null")) {
                mibao2_as_input.setText(AES.decrypt(mbData.getAnswer2()));
            } else {
                mibao2_as_input.setText("");
            }
           /* mibao_commit.setEnabled(true);*/
        });
    }

    //设置加载栏开关
    private void showProcess() {
        if (dialog == null) {
            dialog = processDialog.createLoadingDialog(context, "正在加载");
            tool.setDialogSize((Activity) context, dialog, 0.2, 0.8);
            dialog.show();
        }
    }

    private void closeProcess() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private String getPhone() {
        SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
        String userInformation = sp.getString("userInformation", "null");
        uData = JSONArray.parseObject(userInformation, userData.class);
        return uData.getPhone();
    }

    class TextChanger implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int mb1_qs = mibao1_qs_input.length();
            int mb1_as = mibao1_as_input.length();
            int mb2_qs = mibao2_qs_input.length();
            int mb2_as = mibao2_as_input.length();
            mibao_commit.setEnabled(mb1_qs != 0 || mb1_as != 0 || mb2_qs != 0 || mb2_as != 0);
            if (mb1_qs == 0) {
                cha10.setVisibility(View.INVISIBLE);
            } else {
                cha10.setVisibility(View.VISIBLE);
            }
            if (mb1_as == 0) {
                cha13.setVisibility(View.INVISIBLE);
            } else {
                cha13.setVisibility(View.VISIBLE);
            }
            if (mb2_qs == 0) {
                cha11.setVisibility(View.INVISIBLE);
            } else {
                cha11.setVisibility(View.VISIBLE);
            }
            if (mb2_as == 0) {
                cha14.setVisibility(View.INVISIBLE);
            } else {
                cha14.setVisibility(View.VISIBLE);
            }
        }
    }

}
