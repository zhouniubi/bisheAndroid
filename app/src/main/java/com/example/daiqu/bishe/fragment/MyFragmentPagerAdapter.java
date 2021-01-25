package com.example.daiqu.bishe.fragment;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.daiqu.bishe.activity.startActivity;

import org.jetbrains.annotations.NotNull;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private taskFragment myFragment1 = null;
    private messageFragment myFragment2 = null;
    private userFragment myFragment3 = null;

    @SuppressLint("WrongConstant")
    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        myFragment1 = new taskFragment();
        myFragment2 = new messageFragment();
        myFragment3 = new userFragment();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case startActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case startActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case startActivity.PAGE_THREE:
                fragment = myFragment3;
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
