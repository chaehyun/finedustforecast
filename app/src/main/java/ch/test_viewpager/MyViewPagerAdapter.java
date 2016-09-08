package ch.test_viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by CH on 2016-07-03.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment[] fragments = new Fragment[4];

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new FragmentA();
        fragments[1] = new FragmentB();
        fragments[2] = new FragmentC();
        fragments[3] = new FragmentD();
    }

    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    public int getCount() {
        return fragments.length;
    }
}
