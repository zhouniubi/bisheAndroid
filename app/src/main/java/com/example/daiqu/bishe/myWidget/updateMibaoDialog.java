package com.example.daiqu.bishe.myWidget;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.data.userData;

public class updateMibaoDialog extends Dialog {
    private userData uData;
    private Context context;
    public updateMibaoDialog(@NonNull Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        setContentView(R.layout.dialog_update_mibao_into);
    }

    //获取当前用户信息
    protected void getUser(){
        SharedPreferences sp = context.getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation, userData.class);
    }
}
