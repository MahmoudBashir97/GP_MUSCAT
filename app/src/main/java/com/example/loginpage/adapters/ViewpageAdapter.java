package com.example.loginpage.adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewpageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentlist=new ArrayList<>();
    private final List<String> fragmentlisttitels=new ArrayList<>();

    public ViewpageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlisttitels.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentlisttitels.get(position);
    }
    public void addFragment (Fragment fragment , String Title){
        fragmentlist.add(fragment);
        fragmentlisttitels.add(Title);
    }
}