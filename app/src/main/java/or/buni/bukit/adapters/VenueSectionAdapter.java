package or.buni.bukit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import or.buni.bukit.fragments.VenueOverview;

/**
 * Created by yusuphwickama on 8/10/17.
 */

public class VenueSectionAdapter extends FragmentPagerAdapter {

    public VenueSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.

        switch (position) {
            case 0:
                return VenueOverview.getInstance();
            case 1:
                return new Fragment();
            case 2:
                return new Fragment();
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Overview";
            case 1:
                return "Gallery";
            case 2:
                return "Address";
        }
        return null;
    }

}
