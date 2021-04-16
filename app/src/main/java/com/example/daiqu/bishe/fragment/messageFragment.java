package com.example.daiqu.bishe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daiqu.R;
import com.example.daiqu.bishe.activity.chatActivity;
import com.example.daiqu.bishe.adapter.chatRoomListAdapter;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationManager;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<V2TIMConversation> listV2;
    private ListView listView;
    public messageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messageFragment newInstance(String param1, String param2) {
        messageFragment fragment = new messageFragment();
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initWidget(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oncLick();
    }

    public void initWidget(View view){
        listView = view.findViewById(R.id.chat_room_list);
        getActivity().runOnUiThread(()->{
            //设置会话监听
            V2TIMConversationManager manager = V2TIMManager.getConversationManager();
            manager.setConversationListener(new V2TIMConversationListener() {
                @Override
                public void onSyncServerStart() {
                    super.onSyncServerStart();
                    Log.d("TencentServeState", "开始同步");
                }
                @Override
                public void onSyncServerFinish() {
                    super.onSyncServerFinish();
                    Log.d("TencentServeState", "服务器同步完成");
                    //拉取50个会话用作展示
                    manager.getConversationList(0, 50, new V2TIMValueCallback<V2TIMConversationResult>() {
                        @Override
                        public void onError(int i, String s) {
                            Log.d("chatListState", "拉取失败！");
                        }
                        @Override
                        public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                            Log.d("chatListState", "拉取成功！");
                            List<V2TIMConversation> list;
                            list =v2TIMConversationResult.getConversationList();
                            listV2 = list;
                            Log.d("list的长度是", String.valueOf(list.size()));
                            chatRoomListAdapter adapter = new chatRoomListAdapter(getContext(), R.id.chat_room_list,R.layout.list_item_layout3,list);
                            listView.setAdapter(adapter);
                            //去掉边缘的拖影
                            listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        }
                    });
                }
                @Override
                public void onSyncServerFailed() {
                    super.onSyncServerFailed();
                    Log.d("TencentServeState", "服务器同步失败");
                }
            });


        });
    }
    public void oncLick(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), chatActivity.class);
            V2TIMConversation conversation = listV2.get(position);
            String toId = conversation.getUserID();
            intent.putExtra("toUser",toId);
            startActivity(intent);
        });
    }

}