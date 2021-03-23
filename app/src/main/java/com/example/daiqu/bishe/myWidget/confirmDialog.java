package com.example.daiqu.bishe.myWidget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daiqu.R;

public class confirmDialog {
    //Button confirm_button,cancle_button;

    public static Dialog createLoadingDialog(Context context, String text) {
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.confirm_dialog, null);
        // 获取整个布局
        RelativeLayout layout = view.findViewById(R.id.confirm_layout);
        // 设置进度框中的文字信息
        TextView textView = view.findViewById(R.id.msg_text);
        textView.setHint(text);
        // 页面中的button
        //confirm_button = view.findViewById(R.id.confirm_button);
        //cancle_button = view.findViewById(R.id.cancle_button);
        // 创建自定义样式的Dialog
        Dialog dialog = new Dialog(context, R.style.add_dialog);
        // 设置返回键无效
        dialog.setCancelable(false);
        dialog.setContentView(layout, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        return dialog;
    }
}
