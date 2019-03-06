package jose.ideaware.com.fixturesandresults.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jose.ideaware.com.fixturesandresults.ui.FixtureFragment;

public class FixturesPageAdapter extends FragmentPagerAdapter {

    private static final int FIXTURE_PAGE_COUNT = 2;

    public FixturesPageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FixtureFragment.newInstance(FixtureFragment.FIXTURES);
        } else {
            return FixtureFragment.newInstance(FixtureFragment.RESULTS);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "FIXTURES";
        } else {
            return "RESULTS";
        }
    }

    @Override
    public int getCount() {
        return FIXTURE_PAGE_COUNT;
    }
}
