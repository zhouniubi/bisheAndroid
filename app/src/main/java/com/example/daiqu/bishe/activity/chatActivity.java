package com.example.daiqu.bishe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daiqu.R;
import com.example.daiqu.bishe.TencentUtils.TencentIM;
import com.example.daiqu.bishe.adapter.msgAdapter;
import com.example.daiqu.bishe.data.msgData;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class chatActivity extends Activity {
    private RecyclerView recyclerView;
    private EditText input_chat_msg;
    private TextView title_name;
    private Button chat_msg_commit;
    private msgAdapter adapter;
    private List<msgData> list = new ArrayList<>();
    private List<V2TIMMessage> v2TIMMessages1;
    private String toUser,sender;
    private V2TIMConversation conversation;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_chat);
        toUser = getIntent().getStringExtra("toUser");
        sender = startActivity.phone;
        initWidget();
        getHistoryMsg();
        onSendMsg();
        getNewMasg();
        //onGetMsg();
    }
    protected void initWidget(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.chat_msg_item);
        input_chat_msg = findViewById(R.id.input_chat_msg);
        chat_msg_commit = findViewById(R.id.chat_msg_commit);
        title_name = findViewById(R.id.title_name);
        title_name.setText(toUser);
        //setMsgAdapter();
    }
    protected void onSendMsg(){
        input_chat_msg.setOnClickListener(v -> {
            recyclerView.scrollToPosition(v2TIMMessages1.size()-1);
        });
        chat_msg_commit.setOnClickListener(v -> {
            String msg = input_chat_msg.getText().toString();
            Log.d("msgdata", msg);
            if(!"".equals(msg)){
                TencentIM.sendMsg(toUser, msg);
                getHistoryMsg();
                input_chat_msg.setText("");
            }
        });
    }

    //设置消息监听
    protected void getNewMasg(){
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                super.onRecvNewMessage(msg);
                v2TIMMessages1.add(msg);
                getHistoryMsg();
            }
        });
    }
    //获得历史消息
    protected void getHistoryMsg(){
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(toUser, 20, null, new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("historyLog", "拉取失败");
            }
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                Collections.reverse(v2TIMMessages);
                v2TIMMessages1 = v2TIMMessages;
                Log.d("msgSize", String.valueOf(v2TIMMessages1.size()));
                Log.d("historyLog", "拉取成功");
                //设置消息适配器
                adapter = new msgAdapter(v2TIMMessages1,sender);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(v2TIMMessages1.size()-1);
            }
        });
    }
    //删除历史消息
    protected void deleteHistoryMsg(V2TIMMessage message ,V2TIMCallback callback ){
        V2TIMManager.getMessageManager().deleteMessageFromLocalStorage(message, callback);
    }

}