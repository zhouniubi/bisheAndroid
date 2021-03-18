package com.example.daiqu.bishe.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.example.daiqu.R;
import com.example.daiqu.bishe.activity.postTaskActivity;
import com.example.daiqu.bishe.activity.startActivity;
import com.example.daiqu.bishe.adapter.RecentTaskListViewAdapter;
import com.example.daiqu.bishe.data.TaskData;
import com.example.daiqu.bishe.tool.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link taskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class taskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private TextView textView;
    private LinearLayout Running_task_layout1;
    public static String phone = "";
    //private String data = "";

    public taskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment taskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static taskFragment newInstance(String param1, String param2) {
        taskFragment fragment = new taskFragment();
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
        startActivity activity = (startActivity) getActivity();
        phone = activity.getPhone();
        Log.d("Phone是", phone);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        //initWidget(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d("DATA是", data);
        initWidget(getView());
        Running_task_layout1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), postTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //ActivityCollector.finishAll();
    }

    private void initWidget(View view) {
        textView = view.findViewById(R.id.recentTaskHint);
        listView = view.findViewById(R.id.recentTaskList);
        Running_task_layout1 = view.findViewById(R.id.Running_task_layout1);
        Map<String, String> map = new HashMap<>();
        map.put("publisherPhone", phone);
        new Thread(() -> {
            String data = HttpUtils.sendPostMessage(map, "UTF-8", "findPublisherPhone");
            if (data.equals("-999")) {
                Looper.prepare();
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                if (getPerference().equals("null")) {
                    getActivity().runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                        textView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        List<TaskData> list = JSONArray.parseArray(getPerference(), TaskData.class);
                        RecentTaskListViewAdapter adapter = new RecentTaskListViewAdapter(view.getContext(), R.id.recentTaskList, R.layout.list_item_layout1, list);
                        listView.setAdapter(adapter);
                        //去掉边缘的拖影
                        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    });
                }
                Looper.loop();
            } else {
                if (data.equals("[{\"id\":null,\"taskCode\":null,\"publisherPhone\":null,\"accepterPhone\":null,\"type\":null,\"title\":null,\"getPlace\":null,\"postPlace\":null,\"needTime\":null,\"money\":null,\"infomation\":null,\"state\":null,\"pic\":null,\"time\":null,\"time2\":null}]")) {
                    postPreference("null");
                    getActivity().runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                        textView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    });
                    postPreference(data);
                    List<TaskData> list = JSONArray.parseArray(data, TaskData.class);
                    getActivity().runOnUiThread((() -> {
                        RecentTaskListViewAdapter adapter = new RecentTaskListViewAdapter(view.getContext(), R.id.recentTaskList, R.layout.list_item_layout1, list);
                        listView.setAdapter(adapter);
                        //去掉边缘的拖影
                        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    }));
                }
            }
        }).start();
    }

    private void postPreference(String data) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("recentTask", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", data);
        editor.apply();
    }

    private String getPerference() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("recentTask", 0);
        String answer = sharedPreferences.getString("data", "null");
        return answer;
    }
}