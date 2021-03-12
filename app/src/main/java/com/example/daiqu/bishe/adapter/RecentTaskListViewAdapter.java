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
import com.example.daiqu.bishe.data.TaskData;

import java.util.List;

public class RecentTaskListViewAdapter extends ArrayAdapter<TaskData> {

    private int textViewResourceId;
    private View view;
    private TaskData taskData;

    public RecentTaskListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<TaskData> objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this.taskData = getItem(position);
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

    private void init() {
        ImageView item_pic1 = view.findViewById(R.id.item_pic1);
        TextView item_name1 = view.findViewById(R.id.item_name1);
        TextView item_title1 = view.findViewById(R.id.item_title1);
        TextView item_time1 = view.findViewById(R.id.item_time1);
        TextView item_getPlace1 = view.findViewById(R.id.item_getPlace1);
        TextView item_postPlace1 = view.findViewById(R.id.item_postPlace1);
        TextView item_state1 = view.findViewById(R.id.item_state1);
        switch (taskData.getType()) {
            case "0":
                item_pic1.setImageResource(R.drawable.wash);
                item_name1.setText("洗衣");
                break;
            case "1":
                item_pic1.setImageResource(R.drawable.supermarket);
                item_name1.setText("超市");
                break;
            case "2":
                item_pic1.setImageResource(R.drawable.waimai);
                item_name1.setText("外卖");
                break;
            case "3":
                item_pic1.setImageResource(R.drawable.kuaidi3);
                item_name1.setText("外卖");
                break;
            default:
                break;
        }
        item_title1.setText(taskData.getTitle());
        item_time1.setText(taskData.getTime().substring(0,10));
        item_getPlace1.setText("接取地址："+taskData.getGetPlace());
        item_postPlace1.setText("配送地址："+taskData.getPostPlace());
        switch (taskData.getState()){
            case "00":
                item_state1.setText("未接取");
                item_state1.setTextColor(view.getResources().getColor(R.color.task_state1, null));
                break;
            case "01":
                item_state1.setText("派送中");
                item_state1.setTextColor(view.getResources().getColor(R.color.task_state2, null));
                break;
            case "11":
                item_state1.setText("派送完成");
                item_state1.setTextColor(view.getResources().getColor(R.color.task_state3, null));
                break;
            default:
                break;
        }
    }
}

