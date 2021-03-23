package com.example.daiqu.bishe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.daiqu.R;
import com.example.daiqu.bishe.activity.createTaskActivity;
import com.example.daiqu.bishe.myWidget.processDialog;
import com.example.daiqu.bishe.tool.HttpUtils;
import com.example.daiqu.bishe.tool.tool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link washTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class washTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private final int CHOOSE_PIC = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //num是介绍所能输入的最大的长度
    private int num = 100;
    private Spinner spinner;
    private RelativeLayout layout_addPic1;
    private TextView textNum, text_addImg;
    private EditText task_introduce1, task_name1, task_money1, task_getplace1, task_postplace1;
    private ImageView task_addPicture1, task_deletePicture1;
    private Button task_commit1;
    private File file = null;
    private String pic_path = "", phone = "", type = "0", time = "";
    private Uri pic_uri;

    private Dialog dialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public washTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment washTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static washTaskFragment newInstance(String param1, String param2) {
        washTaskFragment fragment = new washTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wash_task, container, false);
        initWidget(view);
        textNum.setText(num + "/" + num);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] items = getResources().getStringArray(R.array.spinner_data);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item_layout, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = spinner.getItemAtPosition(position).toString();
                Log.v("time", time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设置文字计数
        tool.textNumCount(task_introduce1,textNum,num);
        //设置显示本地图片
        layout_addPic1.setOnClickListener(v -> {
            /*因为miui系统问题设置不打开侧边菜单栏进行照片选择*/
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, CHOOSE_PIC);

        });
        //设置删除图片
        task_deletePicture1.setOnClickListener(v -> {
            deletePic();
        });
        //提交测试哈
        task_commit1.setOnClickListener(v -> {
            showProcess();
            //对回调结果进行处理
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    closeProcess();
                    Toast.makeText(getContext(), "本地上传出错啦＞﹏＜", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    closeProcess();
                    String resStr = response.body().string();
                    Looper.prepare();
                    switch (resStr) {
                        case "00":
                            Toast.makeText(getContext(), "服务器保存出问题啦", Toast.LENGTH_SHORT).show();
                            break;
                        case "01":
                            Toast.makeText(getContext(), "图片保存出问题啦", Toast.LENGTH_SHORT).show();
                            break;
                        case "11":
                            Toast.makeText(getContext(), "上传成功啦", Toast.LENGTH_SHORT).show();
                            clearMsgAll();
                            break;
                        default:
                            Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                            Log.d("resStr室：", resStr);
                            break;
                    }
                    Looper.loop();
                }
            };
            HashMap<String, String> map = new HashMap<>();
            map.put("title", task_name1.getText().toString());
            map.put("money", task_money1.getText().toString());
            map.put("getPlace", task_getplace1.getText().toString());
            map.put("postPlace", task_postplace1.getText().toString());
            map.put("needTime", time);
            map.put("publisherPhone", phone);
            map.put("type", type);
            map.put("infomation", task_introduce1.getText().toString());
            try {
                if (!pic_path.equals("")) {
                    file = new File(pic_path);
                    HttpUtils.postPicWithParam(map, file, callback, "createTask");
                } else {
                    file = null;
                    HttpUtils.postPicWithParam(map, file, callback, "createTask");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createTaskActivity taskActivity = (createTaskActivity) getActivity();
        phone = taskActivity.getPhone();
    }

    void initWidget(View view) {
        TextChanger textChanger = new TextChanger();
        layout_addPic1 = view.findViewById(R.id.layout_addPic1);

        spinner = view.findViewById(R.id.time_spinner_checkText1);
        task_introduce1 = view.findViewById(R.id.task_introduce1);
        textNum = view.findViewById(R.id.textNum1);
        task_addPicture1 = view.findViewById(R.id.task_addPicture1);
        task_commit1 = view.findViewById(R.id.task_commit1);
        task_commit1.setEnabled(false);
        task_deletePicture1 = view.findViewById(R.id.task_deletePicture1);
        text_addImg = view.findViewById(R.id.text_addImg1);
        task_name1 = view.findViewById(R.id.task_name1);
        task_name1.addTextChangedListener(textChanger);
        task_money1 = view.findViewById(R.id.task_money1);
        task_money1.addTextChangedListener(textChanger);
        task_getplace1 = view.findViewById(R.id.task_getplace1);
        task_getplace1.addTextChangedListener(textChanger);
        task_postplace1 = view.findViewById(R.id.task_postplace1);
        task_postplace1.addTextChangedListener(textChanger);
    }

    //获取本地图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PIC:
                if (resultCode == RESULT_OK) {
                    pic_uri = data.getData();
                    tool tool_t = new tool();
                    pic_path = tool_t.getPath(getView().getContext(), pic_uri);
                    System.out.println("pic_path:" + pic_path);
                    Toast.makeText(getActivity(), pic_path, Toast.LENGTH_SHORT).show();
                    try {
                        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(pic_path)
                                , getView().getHeight(), getView().getWidth(), false);
                        Log.d("Height()是:",  String.valueOf(getView().getHeight()));
                        Log.d("Width()是:",  String.valueOf(getView().getWidth()));

                        /* 将Bitmap设定到ImageView */
                        task_addPicture1.setImageBitmap(bitmap);
                        layout_addPic1.setClickable(false);
                        task_deletePicture1.setVisibility(View.VISIBLE);
                        text_addImg.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
                break;
        }

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
            int name = task_name1.length(), money = task_money1.length(), gp = task_getplace1.length(), pp = task_postplace1.length();
            //设置按钮的可点击性
            task_commit1.setEnabled(name != 0 && money != 0 && gp != 0 && pp != 0);
        }
    }

    //设置加载栏开关
    private void showProcess() {
        if (dialog == null) {
            dialog = processDialog.createLoadingDialog(getContext(),"正在上传");
            WindowManager m = getActivity().getWindowManager();
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

    private void deletePic() {
        new Thread(() -> getActivity().runOnUiThread(() -> {
            pic_path = "";
            file = null;
             /* Resources resources = getActivity().getResources();
            Drawable imageDrawable = resources.getDrawable(R.drawable.add_picture, null);*/
            //图片在drawable文件夹下(上面的方法过时了。。。)
            task_addPicture1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.add_picture));
            task_deletePicture1.setVisibility(View.GONE);
            text_addImg.setVisibility(View.VISIBLE);
            layout_addPic1.setClickable(true);
        })).start();
    }

    private void clearMsgAll() {
        new Thread(() -> getActivity().runOnUiThread(() -> {
            task_name1.setText("");
            task_money1.setText("");
            spinner.setSelection(0);
            task_getplace1.setText("");
            task_postplace1.setText("");
            deletePic();
            task_introduce1.setText("");
        })).start();
    }
}