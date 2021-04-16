package com.example.daiqu.bishe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.daiqu.R;
import com.tencent.imsdk.v2.V2TIMConversation;

import java.util.List;

public class chatRoomListAdapter extends ArrayAdapter<V2TIMConversation> {
    private int textViewResourceId;
    private View view;
    private V2TIMConversation conversation;
    public chatRoomListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<V2TIMConversation> objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        this.conversation = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(textViewResourceId, parent, false);
        } else {
            view = convertView;
        }
        this.view = view;
        init();
        return view;
    }
    public void init(){
        ImageView imageView = view.findViewById(R.id.chat_member_pic);
        TextView member_name = view.findViewById(R.id.member_name);
        TextView msg_text_brief = view.findViewById(R.id.msg_text_brief);
        TextView msg_time = view.findViewById(R.id.msg_time);
        imageView.setImageResource(R.drawable.boy_img);
        String userId = conversation.getUserID();
        String text_brief =conversation.getLastMessage().getTextElem().getText();
        member_name.setText(userId);
        msg_text_brief.setText(text_brief);
    }

}
