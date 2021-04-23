package com.example.daiqu.bishe.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.tool.AES;
import com.example.daiqu.bishe.tool.ActivityCollector;

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
    private RelativeLayout user_information_layout3;
    private TextView user_name,user_identity,user_introduce;
    private ImageView sex_pic;
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
    private void initWidget(View view){
        user_information_layout3 = view.findViewById(R.id.user_information_layout3);
        user_name = view.findViewById(R.id.user_name);
        user_identity = view.findViewById(R.id.user_identity);
        sex_pic = view.findViewById(R.id.sex_pic);
        user_introduce = view.findViewById(R.id.user_introduce);
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
    }
    private void onClick(){
        user_information_layout3.setOnClickListener(v -> {
            SharedPreferences  sharedPreferences = getActivity().getSharedPreferences("loadStatePerference", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loadState", "000");
            String phone = sharedPreferences.getString("phone", "");
            editor.apply();
            TencentIM.logout();
            ActivityCollector.finishAll();
        });
    }
    private void getUser(){
        SharedPreferences sp = getActivity().getSharedPreferences("userDataPreferences", 0);
        String userInformation =sp.getString("userInformation","null");
        uData = JSONArray.parseObject(userInformation,userData.class);
    }
}