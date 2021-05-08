package com.example.daiqu.bishe.myWidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.activity.loadActivity;
import com.example.daiqu.bishe.data.mibaoData;
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.tool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class updateMibaoDialog extends Dialog {
    private userData uData;
    private Context context;
    private EditText dialog_pwd;
    private ImageView eye7;
    private Button into_button;
    private Dialog dialog;
    private mibaoData mbData;
    private Activity activity;
    public updateMibaoDialog(@NonNull Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        setContentView(R.layout.dialog_update_mibao_into);
        onInitWidget();

        onClick();

    }
    //初始化控件
    protected void onInitWidget(){
        dialog_pwd = findViewById(R.id.dialog_pwd);
        eye7 = findViewById(R.id.eye7);
        into_button = findViewById(R.id.into_button);
        dialog_pwd.addTextChangedListener(new TextChanger());
        eye7.setVisibility(View.INVISIBLE);
        into_button.setEnabled(false);
    }
    //获取当前用户信息
    protected void getUser(){
        SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation, userData.class);
    }
    //设置点击事件
    protected void onClick(){
        into_button.setOnClickListener(v -> {
            if(!uData.getPwd().equals(AES.encrypt(dialog_pwd.getText().toString()))){
                Toast.makeText(context, "密码错误！😢", Toast.LENGTH_SHORT).show();
            }else{
                showProcess();
                new Thread(() -> {
                    Map<String,String> map = new HashMap<>();
                    map.put("phone", getPhone());
                    Callback callback = new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            closeProcess();
                            Looper.prepare();
                            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            closeProcess();
                            mibaoData mbData = JSONArray.parseObject(response.body().string(), mibaoData.class);
                            Log.d("mbDataMSG", mbData.getAnswer1()+mbData.getAnswer2());
                            updateMibaoDialog.super.cancel();
                            Looper.prepare();
                            inputMibaoDialog dialog = new inputMibaoDialog(context,(Activity)context,mbData);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            Looper.loop();
                        }
                    };
                    HttpUtils.postPicWithParam(map,null,callback,"findMibao");
                }).start();
            }
        });
        eye7.setOnClickListener(v -> {
            if(!eye7.isSelected()){
                dialog_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(dialog_pwd);
                eye7.setSelected(true);
            }else{
                dialog_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loadActivity.setSelectionEnd(dialog_pwd);
                eye7.setSelected(false);
            }

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
    private String getPhone(){
        SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation, userData.class);
        return uData.getPhone();
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
            int pwd_length = dialog_pwd.length();
            if(pwd_length!=0){
                eye7.requestFocus();
                eye7.setVisibility(View.VISIBLE);
                into_button.setEnabled(true);
            }else{
                eye7.clearFocus();
                eye7.setVisibility(View.INVISIBLE);
                into_button.setEnabled(false);
            }
        }
    }
}
