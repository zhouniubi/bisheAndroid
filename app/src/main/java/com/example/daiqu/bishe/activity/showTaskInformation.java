package com.example.daiqu.bishe.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class showTaskInformation extends Activity {
    private TaskDataWithName data;
    private TextView taskInfomation_code, task_info_publisher, taskInformation_fb_time, task_info_accepter, taskInformation_js_time, task_info_type, clickToSeeBigPic, task_info_state, textNum_count;
    private EditText taskInformation_title, task_info_money, task_info_getPlace, task_info_postPlace, task_info_introduce;
    private Spinner spinner_info;
    private ImageView task_info_pic, task_info_deletePic, task_info_delete, task_info_chat, task_info_commit, task_info_update, task_info_quxiao, task_info_confirm;
    private Bitmap bmap = null;
    private String time = "";
    private int txtNum = 100;
    private Dialog dialog, confirm_Dialog;
    private File file = null;
    private final int CHOOSE_PIC = 0;
    private Uri pic_uri;
    private String pic_path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_task_information);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        data = (TaskDataWithName) intent.getSerializableExtra("taskData");
        //绑定控件
        initWidget();
        //显示进度对话框
        showProcess();
        //加载图片
        loadPic();
        //设置页面文本
        setText();
        //设置相关控件的可用性
        setEnable(false);
        //设置底部ui的可见性
        //setVisible(0, 0, 0, 8, 8, 8);
        //设置点击事件
        onClick();
        //设置图片的可点击性
        task_info_pic.setClickable(false);
    }

    private void initWidget() {
        taskInfomation_code = findViewById(R.id.taskInfomation_code);
        task_info_publisher = findViewById(R.id.task_info_publisher);
        taskInformation_fb_time = findViewById(R.id.taskInformation_fb_time);
        task_info_accepter = findViewById(R.id.task_info_accepter);
        taskInformation_js_time = findViewById(R.id.taskInformation_js_time);
        task_info_type = findViewById(R.id.task_info_type);
        clickToSeeBigPic = findViewById(R.id.clickToSeeBigPic);
        taskInformation_title = findViewById(R.id.taskInformation_title);
        task_info_money = findViewById(R.id.task_info_money);
        task_info_getPlace = findViewById(R.id.task_info_getPlace);
        task_info_postPlace = findViewById(R.id.task_info_postPlace);
        task_info_introduce = findViewById(R.id.task_info_introduce);
        spinner_info = findViewById(R.id.spinner_info);
        task_info_pic = findViewById(R.id.task_info_pic);
        task_info_deletePic = findViewById(R.id.task_info_deletePic);
        task_info_delete = findViewById(R.id.task_info_delete);
        task_info_chat = findViewById(R.id.task_info_chat);
        task_info_commit = findViewById(R.id.task_info_commit);
        task_info_update = findViewById(R.id.task_info_update);
        task_info_state = findViewById(R.id.task_info_state);
        task_info_quxiao = findViewById(R.id.task_info_quxiao);
        textNum_count = findViewById(R.id.textNum_count);
        task_info_confirm = findViewById(R.id.task_info_confirm);
    }

    private void setText() {
        taskInfomation_code.setText(data.getTaskCode());
        taskInformation_title.setText(data.getTitle());
        task_info_publisher.setText("发布者：" + AES.decrypt(data.getPublisherName()));
        if ("null".equals(data.getAccepterName())) {
            task_info_accepter.setText("接收者：暂无");
            setVisible(0, 8, 0, 8, 8, 8);
        } else {
            task_info_accepter.setText("接收者：" + AES.decrypt(data.getAccepterName()));
            setVisible(0, 0, 0, 8, 8, 8);
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

    @Override
    protected void onStart() {
        super.onStart();
        setText();
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

    private void setVisible(int visible1, int visible2, int visible3, int visible4, int visible5, int visible6) {
        //visible:0;Invisible:4;gone:8
        //visible1：设置删除img的存在性；visible2：设置聊天img的存在性；
        // visible3：设置确认的存在性；visible4：设置取消的存在性；visible5:设置提交存在性；visible6设置删除图片的存在性
        task_info_delete.setVisibility(visible1);
        task_info_chat.setVisibility(visible2);
        task_info_confirm.setVisibility(visible3);
        task_info_quxiao.setVisibility(visible4);
        task_info_commit.setVisibility(visible5);
        task_info_deletePic.setVisibility(visible6);
    }

    private void onClick() {
        task_info_update.setOnClickListener(v -> {
            setEnable(true);
            //设置删除图标的显示
            if (data.getPic().equals("")) {
                setVisible(8, 8, 8, 0, 0, 8);
            } else {
                setVisible(8, 8, 8, 0, 0, 0);
            }
            task_info_pic.setClickable(true);
        });
        task_info_delete.setOnClickListener(v -> {
            Map<String, String> map = new HashMap<>();
            map.put("taskCode", data.getTaskCode());
            showConfirmDialog("确认删除?");
            confirm_Dialog.getWindow().findViewById(R.id.cancle_button).setOnClickListener(v1 -> {
                closeConfirmDialog();
            });
            confirm_Dialog.getWindow().findViewById(R.id.confirm_button).setOnClickListener(v1 -> {
                new Thread(() -> {
                    Looper.prepare();
                    String state = HttpUtils.sendPostMessage(map, "UTF-8", "deleteTask");
                    Log.d("deleteState", state);
                    if (state.equals("1")) {
                        Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "删除失败！服务器连接异常！", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                    closeConfirmDialog();
                }).start();
            });
        });
        task_info_quxiao.setOnClickListener(v -> {
            showProcess();
            loadPic();
            setText();
            setEnable(false);
            setVisible(0, 0, 0, 8, 8, 8);
            onClick();
            task_info_pic.setClickable(false);
        });
        clickToSeeBigPic.setOnClickListener(v -> {
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
        task_info_deletePic.setOnClickListener(v -> {
            //删除文件路径与file,将图片的pic置空
            file = null;
            pic_path = "";
            data.setPic("");
            task_info_pic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_picture));
            task_info_pic.setClickable(true);
            task_info_deletePic.setVisibility(View.GONE);
        });
        task_info_pic.setOnClickListener(v -> {
            /*因为miui系统问题设置不打开侧边菜单栏进行照片选择*/
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, CHOOSE_PIC);
        });
        task_info_confirm.setOnClickListener(v -> {
            showConfirmDialog("确认收货?");
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
                        Toast.makeText(this, "确认收货啦(❤ ω ❤)", Toast.LENGTH_SHORT).show();
                        runOnUiThread(this::setText);
                        closeConfirmDialog();
                    }else{
                        Toast.makeText(this, "服务器出问题啦", Toast.LENGTH_SHORT).show();
                        closeConfirmDialog();
                    }
                    Looper.loop();
                }).start();
            });
        });
        task_info_commit.setOnClickListener(v -> {
            showConfirmDialog("确认更新?");
            confirm_Dialog.getWindow().findViewById(R.id.cancle_button).setOnClickListener(v1 -> {
                closeConfirmDialog();
            });
            confirm_Dialog.getWindow().findViewById(R.id.confirm_button).setOnClickListener(v1 -> {
                Log.d("pic详情：", data.getPic());
                showProcess();
                //对回调结果进行处理
                Callback callback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        closeProcess();
                        closeConfirmDialog();
                        Toast.makeText(showTaskInformation.this, "本地上传出错啦＞﹏＜", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        closeProcess();
                        closeConfirmDialog();
                        String resStr = response.body().string();
                        Looper.prepare();
                        if ("11".equals(resStr)) {
                            Toast.makeText(showTaskInformation.this, "上传成功啦", Toast.LENGTH_SHORT).show();
                            runOnUiThread(() -> {
                                task_info_pic.setClickable(false);
                                finish();
                            });

                        } else {
                            Toast.makeText(showTaskInformation.this, "未知错误", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    }
                };
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(data.getId()));
                map.put("title", taskInformation_title.getText().toString());
                map.put("needTime", time);
                map.put("getPlace", task_info_getPlace.getText().toString());
                map.put("postPlace", task_info_postPlace.getText().toString());
                map.put("money", task_info_money.getText().toString());
                map.put("infomation", task_info_introduce.getText().toString());
                map.put("publisherPhone", data.getPublisherPhone());
                map.put("accepterPhone", data.getAccepterPhone());
                map.put("type", data.getType());
                map.put("state", data.getState());
                map.put("time", data.getTime());
                map.put("time2", data.getTime2());
                map.put("taskCode", data.getTaskCode());
                try {
                    if (!pic_path.equals("")) {
                        file = new File(pic_path);
                        HttpUtils.postPicWithParam(map, file, callback, "updateTask");
                    } else {
                        file = null;
                        if (!data.getPic().equals("")) {
                            map.put("pic", data.getPic());
                        } else {
                            map.put("pic", "");
                        }
                        HttpUtils.postPicWithParam(map, file, callback, "updateTask");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        });
        spinner_info.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = spinner_info.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        task_info_chat.setOnClickListener(v -> {
           /* Intent intent = new Intent();
            intent.putExtra("toUser", AES.decrypt(data.getAccepterPhone()));
            intent.putExtra("toUserName", AES.decrypt(data.getAccepterName()));
            startActivity(intent);*/


        });
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
            task_info_deletePic.setVisibility(View.GONE);
            closeProcess();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("pic", data.getPic());
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    closeProcess();
                    e.printStackTrace();
                    Toast.makeText(showTaskInformation.this, "服务器连接出错了！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] dataByte = response.body().bytes();
                    runOnUiThread(() -> {
                        Glide.with(showTaskInformation.this).load(dataByte).centerCrop().into(task_info_pic);
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
            task_info_deletePic.setVisibility(View.VISIBLE);
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

    //设置获取本地图片并显示
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PIC:
                if (resultCode == RESULT_OK) {
                    pic_uri = data.getData();
                    tool tool_t = new tool();
                    pic_path = tool_t.getPath(this, pic_uri);
                    System.out.println("pic_path:" + pic_path);
                    Toast.makeText(this, pic_path, Toast.LENGTH_SHORT).show();
                    try {
                        runOnUiThread(()->{Glide.with(showTaskInformation.this).load(pic_path).centerCrop().into(task_info_pic);});
                       /* Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(pic_path)
                                , task_info_pic.getHeight(), task_info_pic.getWidth(), false);
                        *//* 将Bitmap设定到ImageView *//*
                        task_info_pic.setImageBitmap(bitmap);*/
                        //task_info_pic.setClickable(false);
                        task_info_deletePic.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
                break;
        }
    }

}