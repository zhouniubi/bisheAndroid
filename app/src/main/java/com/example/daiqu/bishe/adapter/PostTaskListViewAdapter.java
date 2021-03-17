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
import com.example.daiqu.bishe.data.TaskDataWithName;
import com.example.daiqu.bishe.tool.AES;

import java.util.List;

public class PostTaskListViewAdapter extends ArrayAdapter<TaskDataWithName> {
    private int textViewResourceId;
    private View view;
    private TaskDataWithName taskDataWithName;

    public PostTaskListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<TaskDataWithName> objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this.taskDataWithName = getItem(position);
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

    public void init() {
        ImageView item_pic2 = view.findViewById(R.id.item_pic2);
        TextView useTime = view.findViewById(R.id.useTime);
        TextView item_title2 = view.findViewById(R.id.item_title2);
        TextView item_name2 = view.findViewById(R.id.item_name2);
        TextView item_poster = view.findViewById(R.id.item_poster);
        TextView item_time2 = view.findViewById(R.id.item_time2);
        TextView item_time22 = view.findViewById(R.id.item_time22);
        TextView item_getPlace2 = view.findViewById(R.id.item_getPlace2);
        TextView item_postPlace2 = view.findViewById(R.id.item_postPlace2);
        TextView item_state2 = view.findViewById(R.id.item_state2);
        TextView item_introduce = view.findViewById(R.id.item_introduce);
        switch (taskDataWithName.getType()) {
            case "0":
                item_pic2.setImageResource(R.drawable.wash);
                item_name2.setText("洗衣");
                break;
            case "1":
                item_pic2.setImageResource(R.drawable.supermarket);
                item_name2.setText("超市");
                break;
            case "2":
                item_pic2.setImageResource(R.drawable.waimai);
                item_name2.setText("外卖");
                break;
            case "3":
                item_pic2.setImageResource(R.drawable.kuaidi3);
                item_name2.setText("外卖");
                break;
            default:
                break;
        }
        useTime.setText(taskDataWithName.getNeedTime());
        item_title2.setText(taskDataWithName.getTitle());
        item_time2.setText("发布时间：" + taskDataWithName.getTime().substring(0, 16));
        if (!taskDataWithName.getTime2().equals("null")) {
            item_time22.setText("接收时间：" + taskDataWithName.getTime().substring(0, 16));
        } else {
            item_time22.setText("接收时间：" + "暂无");
        }
        item_getPlace2.setText("接取地址：" + taskDataWithName.getGetPlace());
        item_postPlace2.setText("配送地址：" + taskDataWithName.getPostPlace());
        if (!taskDataWithName.getAccpterName().equals("null")) {
            item_poster.setText("派送者：" + AES.decrypt(taskDataWithName.getPublisherName()));
        }else{
            item_poster.setText("派送者：" + "暂无");
        }
        item_introduce.setText("详情：" + taskDataWithName.getInfomation());
        switch (taskDataWithName.getState()) {
            case "00":
                item_state2.setText("无人接取");
                item_state2.setTextColor(view.getResources().getColor(R.color.task_state1, null));
                break;
            case "01":
                item_state2.setText("派送中");
                item_state2.setTextColor(view.getResources().getColor(R.color.task_state2, null));
                break;
            case "11":
                item_state2.setText("派送完成");
                item_state2.setTextColor(view.getResources().getColor(R.color.task_state3, null));
                break;
            default:
                break;
        }
    }

}
