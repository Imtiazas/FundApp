package com.example.fundapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {

            case 0:
                WeeklyFragment weeklyFragment = new WeeklyFragment();
                return weeklyFragment;
            case 1:
                MonthlyFragment monthlyFragment = new MonthlyFragment();
                return monthlyFragment;

            case 2:
                YearlyFragment yearlyFragment= new YearlyFragment();
                return yearlyFragment;
                default:
                    return null;

        }


    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position)
    {

        switch (position)
        {

            case 0:
                return "WEEKLY";
            case 1:
                return "MONTHLY";
            case 2:
                return "YEARLY";
                default:
                    return null;

        }

    }
}
