package com.example.photostudio.controllers;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.photostudio.fragment.RoomBookingFragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return RoomBookingFragment.getInstance();
                //Добавить еще кейс для конфирм
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
