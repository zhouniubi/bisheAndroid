package com.example.daiqu.bishe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.TencentUtils.TencentIM;
import com.example.daiqu.bishe.activity.forgetPwdActivity1;
import com.example.daiqu.bishe.activity.forgetPwdActivity2;
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.myWidget.updateDialog;
import com.example.daiqu.bishe.myWidget.updateMibaoDialog;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;
import com.example.daiqu.bishe.tool.tool;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout user_information_layout3,update_information,update_pwd,back_msg,update_mibao;
    private TextView user_name,user_identity,user_introduce;
    private ImageView sex_pic,user_pic;
    private userData uData;
    public userFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static userFragment newInstance(String param1, String param2) {
        userFragment fragment = new userFragment();
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
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        getUser();
        initWidget(view);
        onClick();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
        onInit();
        Log.d("onStart", "开始啦");
    }

    private void initWidget(View view){
        user_information_layout3 = view.findViewById(R.id.user_information_layout3);
        update_information = view.findViewById(R.id.update_information);
        update_pwd = view.findViewById(R.id.update_pwd);
        back_msg = view.findViewById(R.id.back_msg);
        update_mibao = view.findViewById(R.id.update_mibao);
        user_name = view.findViewById(R.id.user_name);
        user_identity = view.findViewById(R.id.user_identity);
        sex_pic = view.findViewById(R.id.sex_pic);
        user_introduce = view.findViewById(R.id.user_introduce);
        user_pic = view.findViewById(R.id.user_pic);
        onInit();
    }
    public void onInit(){
        user_name.setText("姓名："+ AES.decrypt(uData.getName()));
        if(uData.getIdentity().equals("1")){
            user_identity.setText("身份：教师");
        }else{
            user_identity.setText("身份：学生");
        }
        if(uData.getSex().equals("1")){
            sex_pic.setImageResource(R.drawable.sex_boy);
        }else{
            sex_pic.setImageResource(R.drawable.sex_girl);
        }
        if(uData.getIntroduce().equals("null")){
            user_introduce.setText("个人信息：暂无");
        }else{
            user_introduce.setText("个人信息："+uData.getIntroduce());
        }
        switch (uData.getPic()){
            case "000":
                user_pic.setImageResource(R.drawable.boy_img);
                break;
            case "001":
                user_pic.setImageResource(R.drawable.boy_img1);
                break;
            case "002":
                user_pic.setImageResource(R.drawable.boy_img2);
                break;
            case "100":
                user_pic.setImageResource(R.drawable.girl_img);
                break;
            case "101":
                user_pic.setImageResource(R.drawable.girl_img1);
                break;
            case "102":
                user_pic.setImageResource(R.drawable.girl_img2);
                break;
            default:
                break;
        }
    }
    private void onClick(){
        //退出登录并修改登录状态
        user_information_layout3.setOnClickListener(v -> {
            SharedPreferences  sharedPreferences = getActivity().getSharedPreferences("loadStatePerference", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loadState", "000");
            editor.apply();
            TencentIM.logout();
            ActivityCollector.finishAll();
        });
        //弹出更新对话框
        update_information.setOnClickListener(v -> {
            updateDialog dialog = new updateDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            tool.setDialogSize(getActivity(), dialog, 1.0, 1.0);
            dialog.show();
        });
        //修改密码
        update_pwd.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getActivity(), R.style.introduce_dialog);
            dialog.setContentView(R.layout.forget_choose_way);
            dialog.show();
            dialog.getWindow().findViewById(R.id.have_mibao).setOnClickListener(v1 -> {
                dialog.cancel();
                Intent intent = new Intent(getActivity(), forgetPwdActivity1.class);
                startActivity(intent);
            });
            dialog.getWindow().findViewById(R.id.no_mibao).setOnClickListener(v1 -> {
                Intent intent = new Intent(getActivity(), forgetPwdActivity2.class);
                startActivity(intent);
                dialog.cancel();
            });
            dialog.getWindow().findViewById(R.id.layout_mibao_choose).setOnClickListener(v1 -> {
                dialog.cancel();
            });
        });
        //意见反馈
        back_msg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] email = {"2308911314@qq.com"};
            intent.setType("message/rfc822"); // 设置邮件格式
            intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
            intent.putExtra(Intent.EXTRA_CC, email); // 抄送人  
            intent.putExtra(Intent.EXTRA_SUBJECT,"校园代取app意见反馈"); // 主题
            startActivity(Intent.createChooser(intent,"请选择邮件类应用"));
        });
        //修改密保
        update_mibao.setOnClickListener(v -> {
            updateMibaoDialog dialog = new updateMibaoDialog(getContext());
            dialog.show();
        });
    }
    public void getUser(){
        SharedPreferences sp = getActivity().getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation,userData.class);

    }
}