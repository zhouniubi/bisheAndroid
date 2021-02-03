package com.example.daiqu.bishe.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.daiqu.bishe.activity.createTaskActivity;
import com.example.daiqu.bishe.fragment.kuaidiTaskFragment;
import com.example.daiqu.bishe.fragment.supermarketTaskFragment;
import com.example.daiqu.bishe.fragment.waimaiTaskFragment;
import com.example.daiqu.bishe.fragment.washTaskFragment;

import org.jetbrains.annotations.NotNull;

public class MyFragmentPagerAdapter2 extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    private washTaskFragment myTaskFragment1 = null;
    private supermarketTaskFragment myTaskFragment2 = null;
    private waimaiTaskFragment myTaskFragment3 = null;
    private kuaidiTaskFragment myTaskFragment4 = null;

    @SuppressLint("WrongConstant")
    public MyFragmentPagerAdapter2(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        myTaskFragment1 = new washTaskFragment();
        myTaskFragment2 = new supermarketTaskFragment();
        myTaskFragment3 = new waimaiTaskFragment();
        myTaskFragment4 = new kuaidiTaskFragment();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case createTaskActivity.PAGE_ONE:
                fragment = myTaskFragment1;
                break;
            case createTaskActivity.PAGE_TWO:
                fragment = myTaskFragment2;
                break;
            case createTaskActivity.PAGE_THREE:
                fragment = myTaskFragment3;
                break;
            case createTaskActivity.PAGE_FOUR:
                fragment = myTaskFragment4;
                break;
        }
        return fragment;
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
