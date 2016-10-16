package com.github.dgsc_fav.duckvsfox.duckvsfox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.dgsc_fav.duckvsfox.duckvsfox.R;
import com.github.dgsc_fav.duckvsfox.duckvsfox.model.Duck;
import com.github.dgsc_fav.duckvsfox.duckvsfox.model.Fox;
import com.github.dgsc_fav.duckvsfox.duckvsfox.util.Constants;
import com.github.dgsc_fav.duckvsfox.duckvsfox.view.view.LakeView;


/**
 * Created by DG on 15.10.2016.
 */
public class MainFragment extends Fragment {
    LakeView           mLakeView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mLakeView = (LakeView) root.findViewById(R.id.lake_view);
        mLakeView.setLake(Constants.DEFAULT_GOOD_RADIUS, Constants.DEFAULT_LAKE_RADIUS);
        mLakeView.setAnimals(new Duck(getContext()), new Fox(getContext()));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reset) {
            mLakeView.reset();
            return true;
        }
        return false;
    }
}
