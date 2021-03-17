package com.example.daiqu.bishe.fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
 * Use the {@link kuaidiTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class kuaidiTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int CHOOSE_PIC = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int num = 100;
    private Spinner spinner;
    private TextView textNum4, text_addImg4;
    private RelativeLayout layout_addPic4;
    private EditText task_introduce4, task_name4, task_money4, task_getplace4, task_postplace4;
    private ImageView task_addPicture4, task_deletePicture4;
    private Button task_commit4;
    private File file = null;
    private String pic_path = "", phone = "", type = "3", time = "";
    private Uri pic_uri;
    private Dialog dialog;
    public kuaidiTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment kuaidiTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static kuaidiTaskFragment newInstance(String param1, String param2) {
        kuaidiTaskFragment fragment = new kuaidiTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kuaidi_task, container, false);
        initwidget(view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置显示本地图片
        layout_addPic4.setOnClickListener(v -> {
            /*因为miui系统问题设置不打开侧边菜单栏进行照片选择*/
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, CHOOSE_PIC);
        });
        //设置删除图片
        task_deletePicture4.setOnClickListener(v -> {
            deletePic();
        });
        //提交任务
        task_commit4.setOnClickListener(v -> {
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
            map.put("title", task_name4.getText().toString());
            map.put("money", task_money4.getText().toString());
            map.put("getPlace", task_getplace4.getText().toString());
            map.put("postPlace", task_postplace4.getText().toString());
            map.put("needTime", time);
            map.put("publisherPhone", phone);
            map.put("type", type);
            map.put("infomation", task_introduce4.getText().toString());
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
    private void deletePic(){
        new Thread(() -> getActivity().runOnUiThread(() -> {
            pic_path = "";
            file = null;
            Resources resources = getActivity().getResources();
            Drawable imageDrawable = resources.getDrawable(R.drawable.add_picture, null); //图片在drawable文件夹下
            task_addPicture4.setImageDrawable(imageDrawable);
            task_deletePicture4.setVisibility(View.GONE);
            text_addImg4.setVisibility(View.VISIBLE);
            layout_addPic4.setClickable(true);
        })).start();
    }
    //设置加载栏开关
    private void showProcess() {
        if (dialog == null) {
            dialog = processDialog.createLoadingDialog(getContext());
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
    private void clearMsgAll() {
        new Thread(() -> getActivity().runOnUiThread(() -> {
            task_name4.setText("");
            task_money4.setText("");
            spinner.setSelection(0);
            task_getplace4.setText("");
            task_postplace4.setText("");
            deletePic();
            task_introduce4.setText("");
        })).start();
    }
    private void initwidget(View view){
        TextChanger textChanger = new TextChanger();
        spinner = view.findViewById(R.id.time_spinner_checkText4);
        layout_addPic4 = view.findViewById(R.id.layout_addPic4);
        textNum4 = view.findViewById(R.id.textNum4);
        textNum4.setText(num + "/" + num);
        text_addImg4 = view.findViewById(R.id.text_addImg4);
        task_introduce4 = view.findViewById(R.id.task_introduce4);
        task_name4 = view.findViewById(R.id.task_name4);
        task_name4.addTextChangedListener(textChanger);
        task_money4 = view.findViewById(R.id.task_money4);
        task_money4.addTextChangedListener(textChanger);
        task_getplace4 = view.findViewById(R.id.task_getplace4);
        task_getplace4.addTextChangedListener(textChanger);
        task_postplace4 = view.findViewById(R.id.task_postplace4);
        task_postplace4.addTextChangedListener(textChanger);
        task_addPicture4 = view.findViewById(R.id.task_addPicture4);
        task_deletePicture4 = view.findViewById(R.id.task_deletePicture4);
        task_commit4 = view.findViewById(R.id.task_commit4);
        task_commit4.setEnabled(false);
        mySpinner(view);
        numCount();
    }
    private void mySpinner(View view){
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
    }

    private void numCount(){
        //设置文字计数
        task_introduce4.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                textNum4.setText(num - wordNum.length() + "/" + num);
                selectionStart = task_introduce4.getSelectionStart();
                selectionEnd = task_introduce4.getSelectionEnd();
                if (wordNum.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    task_introduce4.setText(s);
                    task_introduce4.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
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
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(pic_path)
                                , getView().getHeight(), getView().getWidth(), false);
                        /* 将Bitmap设定到ImageView */
                        task_addPicture4.setImageBitmap(bitmap);
                        task_deletePicture4.setVisibility(View.VISIBLE);
                        text_addImg4.setVisibility(View.GONE);
                        layout_addPic4.setClickable(false);
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
            int name = task_name4.length(), money = task_money4.length(), gp = task_getplace4.length(), pp = task_postplace4.length();
            //设置按钮的可点击性
            task_commit4.setEnabled(name != 0 && money != 0 && gp != 0 && pp != 0);
        }
    }
}