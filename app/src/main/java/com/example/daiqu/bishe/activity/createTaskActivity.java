package com.example.daiqu.bishe.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.daiqu.R;
import com.example.daiqu.bishe.adapter.MyFragmentPagerAdapter2;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class createTaskActivity extends FragmentActivity  {
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    private LinearLayout layout_wash_task,layout_supermarket_task,layout_waimai_task,layout_kuaidi_task;
    private RadioGroup title_task2;
    private RadioButton wash_task,supermarket_task,waimai_task,kuaidi_task;
    private TextView wash_text,supermarket_text,waimai_text,kuaidi_text;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter2 mAdapter;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //隐藏标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_task);
        initWidget();
        mAdapter = new MyFragmentPagerAdapter2(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(PAGE_ONE);
        setLayoutSelected(true,false,false,false);
        setTextSelected(true,false,false,false);
        title_task2.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.wash_task:
                    viewPager.setCurrentItem(PAGE_ONE);
                    break;
                case R.id.supermarket_task:
                    viewPager.setCurrentItem(PAGE_TWO);
                    break;
                case R.id.waimai_task:
                    viewPager.setCurrentItem(PAGE_THREE);
                    break;
                case R.id.kuaidi_task:
                    viewPager.setCurrentItem(PAGE_FOUR);
                    break;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==2){
                    switch (viewPager.getCurrentItem()){
                        case PAGE_ONE:
                            setLayoutSelected(true,false,false,false);
                            setTextSelected(true,false,false,false);
                            break;
                        case PAGE_TWO:
                            setLayoutSelected(false,true,false,false);
                            setTextSelected(false,true,false,false);
                            break;
                        case PAGE_THREE:
                            setLayoutSelected(false,false,true,false);
                            setTextSelected(false,false,true,false);
                            break;
                        case PAGE_FOUR:
                            setLayoutSelected(false,false,false,true);
                            setTextSelected(false,false,false,true);
                            break;
                    }
                }
            }
        });


    }
    protected void initWidget(){
        layout_wash_task = findViewById(R.id.layout_wash_task);
        layout_supermarket_task = findViewById(R.id.layout_supermarket_task);
        layout_waimai_task = findViewById(R.id.layout_waimai_task);
        layout_kuaidi_task = findViewById(R.id.layout_kuaidi_task);
        viewPager = findViewById(R.id.vpager2);
        wash_task = findViewById(R.id.wash_task);
        supermarket_task = findViewById(R.id.supermarket_task);
        waimai_task = findViewById(R.id.waimai_task);
        kuaidi_task = findViewById(R.id.kuaidi_task);
        wash_text = findViewById(R.id.wash_text);
        supermarket_text = findViewById(R.id.supermarket_text);
        waimai_text = findViewById(R.id.waimai_text);
        kuaidi_text = findViewById(R.id.kuaidi_text);
        title_task2 = findViewById(R.id.title_task2);
    }
    protected void setLayoutSelected(boolean wash,boolean supermarket,boolean waimai,boolean kuaidi){
        layout_wash_task.setSelected(wash);
        layout_supermarket_task.setSelected(supermarket);
        layout_waimai_task.setSelected(waimai);
        layout_kuaidi_task.setSelected(kuaidi);
    }
    protected void setTextSelected(boolean wash,boolean supermarket,boolean waimai,boolean kuaidi){
        wash_text.setSelected(wash);
        supermarket_text.setSelected(supermarket);
        waimai_text.setSelected(waimai);
        kuaidi_text.setSelected(kuaidi);
    }
}