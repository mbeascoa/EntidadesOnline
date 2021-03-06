package com.beastek.entidadesonline;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 *
 */

public class doctorCategoryAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public doctorCategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new hospitalListFragment();
        } else {
            return new com.beastek.entidadesonline.patientAppointmentsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Hospitals";
        } else {
           return "Appointments";
        }
    }
}

