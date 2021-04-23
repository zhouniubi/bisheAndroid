package com.example.daiqu.bishe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daiqu.R;
import com.example.daiqu.bishe.TencentUtils.TencentIM;
import com.example.daiqu.bishe.tool.AES;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.List;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.ViewHolder> {
    private List<V2TIMMessage> msgList;
    //private V2TIMConversation conversation;
    private String sender;
    private View view;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout get_chat,post_chat;
        TextView get_msg,post_msg;
        ImageView geter_pic,poster_pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            get_chat = itemView.findViewById(R.id.get_chat);
            post_chat = itemView.findViewById(R.id.post_chat);
            get_msg = itemView.findViewById(R.id.get_msg);
            post_msg = itemView.findViewById(R.id.post_msg);
            geter_pic = itemView.findViewById(R.id.geter_pic);
            poster_pic = itemView.findViewById(R.id.poster_pic);
            geter_pic.setImageResource(R.drawable.girl_img);
            poster_pic.setImageResource(R.drawable.boy_img);
            get_chat.setVisibility(View.VISIBLE);
            post_chat.setVisibility(View.VISIBLE);
        }
    }
    public msgAdapter(List<V2TIMMessage> list, String sender){
        msgList = list;
        this.sender = sender;

    }
    @NonNull
    @Override
    public msgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_msg_item, parent, false);
        this.view = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull msgAdapter.ViewHolder holder, int position) {
        V2TIMMessage msg = msgList.get(position);
        //设置左右布局
        if(msg.getSender().equals(AES.decrypt(sender))){
            holder.get_chat.setVisibility(View.GONE);
            holder.post_chat.setVisibility(View.VISIBLE);
            holder.post_msg.setText(msg.getTextElem().getText());
            String id = msg.getSender();
            TencentIM.setPic(view, id, R.id.poster_pic);
        }else{
            holder.get_chat.setVisibility(View.VISIBLE);
            holder.post_chat.setVisibility(View.GONE);
            holder.get_msg.setText(msg.getTextElem().getText());
            String id = msg.getUserID();
            TencentIM.setPic(view, id, R.id.geter_pic);
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

}
