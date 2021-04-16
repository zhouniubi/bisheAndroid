package com.example.daiqu.bishe.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.daiqu.R;
import com.example.daiqu.bishe.data.TaskDataWithName;
import com.example.daiqu.bishe.myWidget.confirmDialog;
import com.example.daiqu.bishe.myWidget.processDialog;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.tool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class showTaskInformation2 extends Activity {
    private TaskDataWithName data;
    private TextView taskInfomation_code, task_info_publisher, taskInformation_fb_time, task_info_accepter, taskInformation_js_time, task_info_type, clickToSeeBigPic, task_info_state, textNum_count;
    private EditText taskInformation_title, task_info_money, task_info_getPlace, task_info_postPlace, task_info_introduce;
    private Spinner spinner_info;
    private ImageView task_info_pic,task_info_chat, task_info_confirm;
    private Bitmap bmap = null;
    private int txtNum = 100;
    private Dialog dialog, confirm_Dialog;
    private String phone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_task_information2);
        ActivityCollector.addActivity(this);
        //获得从StartActivity传来的taskData与phone
        Intent intent = getIntent();
        data = (TaskDataWithName) intent.getSerializableExtra("taskData");
        phone = intent.getStringExtra("phone");
        initWidget();
        showProcess();
        loadPic();
        setText();
        setEnable(false);
        onClick();
    }
    //绑定初始化控件
    private void initWidget() {
        taskInfomation_code = findViewById(R.id.taskInfomation_code2);
        task_info_publisher = findViewById(R.id.task_info_publisher2);
        taskInformation_fb_time = findViewById(R.id.taskInformation_fb_time2);
        task_info_accepter = findViewById(R.id.task_info_accepter2);
        taskInformation_js_time = findViewById(R.id.taskInformation_js_time2);
        task_info_type = findViewById(R.id.task_info_type2);
        clickToSeeBigPic = findViewById(R.id.clickToSeeBigPic2);
        taskInformation_title = findViewById(R.id.taskInformation_title2);
        task_info_money = findViewById(R.id.task_info_money2);
        task_info_getPlace = findViewById(R.id.task_info_getPlace2);
        task_info_postPlace = findViewById(R.id.task_info_postPlace2);
        task_info_introduce = findViewById(R.id.task_info_introduce2);
        spinner_info = findViewById(R.id.spinner_info2);
        task_info_pic = findViewById(R.id.task_info_pic2);
        task_info_chat = findViewById(R.id.task_info_chat2);
        task_info_state = findViewById(R.id.task_info_state2);
        textNum_count = findViewById(R.id.textNum_count2);
        task_info_confirm = findViewById(R.id.task_info_confirm2);
    }
    //加载页面的信息
    private void setText() {
        taskInfomation_code.setText(data.getTaskCode());
        taskInformation_title.setText(data.getTitle());
        task_info_publisher.setText("发布者：" + AES.decrypt(data.getPublisherName()));
        if ("null".equals(data.getAccepterName())) {
            task_info_accepter.setText("接收者：暂无");
        } else {
            task_info_accepter.setText("接收者：" + AES.decrypt(data.getAccepterName()));
        }
        taskInformation_fb_time.setText("发布时间：" + data.getTime());
        if ("null".equals(data.getTime2())) {
            taskInformation_js_time.setText("接收时间：暂无");
        } else {
            taskInformation_js_time.setText("接收时间：" + data.getTime2());
        }
        task_info_money.setText(data.getMoney());
        if ("0".equals(data.getType())) {
            task_info_type.setText("类型：洗衣代取");
        } else if ("1".equals(data.getType())) {
            task_info_type.setText("类型：超市代取");
        } else if ("2".equals(data.getType())) {
            task_info_type.setText("类型：外卖代取");
        } else {
            task_info_type.setText("类型：快递代取");
        }
        if (data.getState().equals("00")) {
            task_info_state.setText("无人接取");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state1, null));
        } else if (data.getState().equals("01")) {
            task_info_state.setText("派送中");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state2, null));
        } else if (data.getState().equals("11")) {
            task_info_state.setText("派送完成");
            task_info_state.setTextColor(getResources().getColor(R.color.task_state3, null));
        }
        task_info_getPlace.setText(data.getGetPlace());
        task_info_postPlace.setText(data.getPostPlace());
        if (!data.getInfomation().equals("")) {
            task_info_introduce.setText(data.getInfomation());
        } else {
            task_info_introduce.setHint("暂无任务详情");
        }
        //设置文字计数
        textNum_count.setText(txtNum - task_info_introduce.length() + "/" + txtNum);
        tool.textNumCount(task_info_introduce, textNum_count, txtNum);
    }
    private void setEnable(boolean enable) {
        if (!enable) {
            task_info_money.setFocusable(enable);
            task_info_getPlace.setFocusable(enable);
            task_info_postPlace.setFocusable(enable);
            task_info_introduce.setFocusable(enable);
            taskInformation_title.setEnabled(enable);
            task_info_money.setEnabled(enable);
            task_info_getPlace.setEnabled(enable);
            task_info_postPlace.setEnabled(enable);
            task_info_introduce.setEnabled(enable);
            setSpinner(data.getNeedTime(), enable);
        } else {
            taskInformation_title.setEnabled(enable);
            task_info_money.setEnabled(enable);
            task_info_getPlace.setEnabled(enable);
            task_info_postPlace.setEnabled(enable);
            task_info_introduce.setEnabled(enable);
            taskInformation_title.setFocusableInTouchMode(enable);
            task_info_money.setFocusableInTouchMode(enable);
            task_info_getPlace.setFocusableInTouchMode(enable);
            task_info_postPlace.setFocusableInTouchMode(enable);
            task_info_introduce.setFocusableInTouchMode(enable);
            taskInformation_title.requestFocus();
            setSpinner(data.getNeedTime(), enable);
        }
    }
    private void setSpinner(String time, Boolean enable) {
        String[] items = getResources().getStringArray(R.array.spinner_data);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_info.setAdapter(spinnerAdapter);
        switch (time) {
            case "5min":
                spinner_info.setSelection(0);
                break;
            case "10min":
                spinner_info.setSelection(1);
                break;
            case "30min":
                spinner_info.setSelection(2);
                break;
            case "1h":
                spinner_info.setSelection(3);
                break;
            case "5h":
                spinner_info.setSelection(4);
                break;
            case "10h":
                spinner_info.setSelection(5);
                break;
        }
        if (enable) {
            spinner_info.setEnabled(enable);
            spinner_info.setClickable(enable);
        } else {
            spinner_info.setEnabled(enable);
            spinner_info.setClickable(enable);
        }
    }
    private void loadPic() {
        if (data.getPic().equals("")) {
            task_info_pic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_picture));
            closeProcess();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("pic", data.getPic());
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    closeProcess();
                    e.printStackTrace();
                    Toast.makeText(showTaskInformation2.this, "服务器连接出错了！", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] dataByte = response.body().bytes();
                    runOnUiThread(() -> {
                        Glide.with(showTaskInformation2.this).load(dataByte).centerCrop().into(task_info_pic);
                    });
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(dataByte, 0, dataByte.length, options);
                    WindowManager m = getWindowManager();
                    Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                    Point size = new Point();
                    d.getSize(size);
                    p.height = (int) (size.y*0.2 );
                    p.width = (int) (size.x*0.8 );
                    //压制图片
                    bmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2,bitmap.getHeight()/2,true );
                    closeProcess();
                }
            };
            new Thread(() -> HttpUtils.postPicWithParam(map, null, callback, "loadPic")).start();
            task_info_pic.setClickable(false);
        }
    }
    //设置加载栏开关
    private void showProcess() {
        if (dialog == null) {
            dialog = processDialog.createLoadingDialog(this, "正在加载");
            WindowManager m = this.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            Point size = new Point();
            d.getSize(size);
            p.height = (int) (size.y * 0.2);
            p.width = (int) (size.x * 0.8);
            dialog.show();
        }
    }

    private void closeProcess() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //设置确认对话框的开关
    private void showConfirmDialog(String text) {
        if (confirm_Dialog == null) {
            confirm_Dialog = confirmDialog.createLoadingDialog(this, text);
            WindowManager m = this.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            Window dialogWindow = confirm_Dialog.getWindow();
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            Point size = new Point();
            d.getSize(size);
            p.height = (int) (size.y * 0.2);
            p.width = (int) (size.x * 0.6);
            confirm_Dialog.show();
        }
    }
    private void closeConfirmDialog() {
        if (confirm_Dialog != null) {
            confirm_Dialog.dismiss();
            confirm_Dialog = null;
        }
    }
    private void onClick(){
        clickToSeeBigPic.setOnClickListener(v->{
            Dialog bigPic = new Dialog(this, R.style.introduce_dialog);
            ImageView image = new ImageView(this);
            image.setImageBitmap(bmap);
            bigPic.setContentView(image);
            bigPic.show();
            //设置再次点击退出
            image.setOnClickListener(v1 -> {
                bigPic.cancel();
            });
        });
        task_info_confirm.setOnClickListener(v -> {
            showConfirmDialog("确认送达?");
            confirm_Dialog.getWindow().findViewById(R.id.cancle_button).setOnClickListener(v1 -> {
                closeConfirmDialog();
            });
            confirm_Dialog.getWindow().findViewById(R.id.confirm_button).setOnClickListener(v1 -> {
                new Thread(()->{
                    Map<String,String> map = new HashMap<>();
                    map.put("taskCode", data.getTaskCode());
                    data.setState("11");
                    map.put("state", data.getState());
                    String state = HttpUtils.sendPostMessage(map,"UTF-8","updateState1");
                    Looper.prepare();
                    if(state.equals("1")){
                        Toast.makeText(this, "确认送达啦(ง •_•)ง继续加油！", Toast.LENGTH_SHORT).show();
                        runOnUiThread(this::setText);
                        closeConfirmDialog();
                        finish();
                    }else{
                        Toast.makeText(this, "服务器出问题啦", Toast.LENGTH_SHORT).show();
                        closeConfirmDialog();
                    }
                    Looper.loop();
                }).start();
            });
        });
    }
}