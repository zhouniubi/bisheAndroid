package com.example.daiqu.bishe.myWidget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daiqu.R;

public class processDialog {


    public static Dialog createLoadingDialog(Context context,String text) {
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.process_dialog1, null);
        // 获取整个布局
        LinearLayout layout = view.findViewById(R.id.process_layout);
        // 设置进度框中的文字信息
        TextView textView = view.findViewById(R.id.processText);
        textView.setHint(text);
        // 页面中的Img
        ImageView img =  view.findViewById(R.id.process_img);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.process1_anim);
        // 显示动画
        img.startAnimation(animation);
        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.add_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }
}
