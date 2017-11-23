package com.example.asus.trendhimapp.loginPage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;

public class TabbedActivity extends BaseActivity {

    public static ViewPager mViewPager;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.swiper_activity, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.*/
        mViewPager = findViewById(R.id.containerLogIn);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the selected sections - log in or sign up
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginActivity();
                case 1:
                    return new SignupActivity();
                default:
                    return null;
            }
        }

        /**
         * @return number of tabs
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * Set the page title depending on the fragment selected
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return Constants.PAGE_TITLE_MONTH;
                case 1:
                    return Constants.PAGE_TITLE_HOUR;
            }
            return null;
        }
    }
}
