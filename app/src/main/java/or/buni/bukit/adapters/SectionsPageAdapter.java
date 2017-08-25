package or.buni.bukit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import or.buni.bukit.fragments.BrowseFragment;
import or.buni.bukit.fragments.Favorites;

/**
 * Created by yusuphwickama on 8/10/17.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.

        switch (position) {
            case 0:
                return BrowseFragment.getInstance();
            case 1:
                return Favorites.getInstance();
        }

        return new Fragment();
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return "";
        }
        return null;
    }

}
