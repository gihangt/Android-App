package app.testapplication.gihan.com.weatherapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Crate control fragment.
 */
public class ControlModuleFragment extends FragmentPagerAdapter {

    ArrayList<Fragment> frgments;
    Context context;
    FragmentManager fm;

    public ControlModuleFragment (FragmentManager fm,ArrayList<Fragment> fragment){
        super(fm);
        this.frgments =fragment;

    }

    @Override
    public Fragment getItem(int position) {
        return frgments.get(position);
    }

    @Override
    public int getCount() {
        return frgments.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {

        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }



}
