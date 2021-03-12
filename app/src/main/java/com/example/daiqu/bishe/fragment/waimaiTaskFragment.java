package com.example.daiqu.bishe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daiqu.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link waimaiTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class waimaiTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int CHOOSE_PIC = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //num是介绍所能输入的最大的长度
    private int num = 100;
    private Spinner spinner;
    private TextView textNum3, text_addImg3;
    private RelativeLayout layout_addPic3;
    private EditText task_introduce3, task_name3, task_money3, task_getplace3, task_postplace3;
    private ImageView task_addPicture3, task_deletePicture3;
    private Button task_commit3;
    private File file = null;
    private String pic_path = "", phone = "", type = "2", time = "";
    private Uri pic_uri;
    private Dialog dialog;
    public waimaiTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment waimaiTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static waimaiTaskFragment newInstance(String param1, String param2) {
        waimaiTaskFragment fragment = new waimaiTaskFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waimai_task, container, false);
        initwidget(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置显示本地图片
        layout_addPic3.setOnClickListener(v -> {
            /*因为miui系统问题设置不打开侧边菜单栏进行照片选择*/
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, CHOOSE_PIC);

        });
        //设置删除图片
        task_deletePicture3.setOnClickListener(v -> {
            deletePic();
        });
    }
    private void deletePic(){
        new Thread(() -> getActivity().runOnUiThread(() -> {
            pic_path = "";
            file = null;
            Resources resources = getActivity().getResources();
            Drawable imageDrawable = resources.getDrawable(R.drawable.add_picture, null); //图片在drawable文件夹下
            task_addPicture3.setImageDrawable(imageDrawable);
            task_deletePicture3.setVisibility(View.GONE);
            text_addImg3.setVisibility(View.VISIBLE);
            layout_addPic3.setClickable(true);
        })).start();
    }
    private void initwidget(View view){
        //TextChanger textChanger = new TextChanger();
        spinner = view.findViewById(R.id.time_spinner_checkText3);
        layout_addPic3 = view.findViewById(R.id.layout_addPic3);
        textNum3 = view.findViewById(R.id.textNum3);
        textNum3.setText(num + "/" + num);
        text_addImg3 = view.findViewById(R.id.text_addImg3);
        task_introduce3 = view.findViewById(R.id.task_introduce3);
        task_name3 = view.findViewById(R.id.task_name3);
        //task_name3.addTextChangedListener(textChanger);
        task_money3 = view.findViewById(R.id.task_money3);
        //task_money3.addTextChangedListener(textChanger);
        task_getplace3 = view.findViewById(R.id.task_getplace3);
        //task_getplace3.addTextChangedListener(textChanger);
        task_postplace3 = view.findViewById(R.id.task_postplace3);
        //task_postplace3.addTextChangedListener(textChanger);
        task_addPicture3 = view.findViewById(R.id.task_addPicture3);
        task_deletePicture3 = view.findViewById(R.id.task_deletePicture3);
        task_commit3 = view.findViewById(R.id.task_commit3);
        task_commit3.setEnabled(false);
    }
}