package com.bassel.gonews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bassel.gonews.R;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class FragmentTopHeadlines extends BaseFragment {

    private static final String TAG = FragmentTopHeadlines.class.getSimpleName();

    public static FragmentTopHeadlines newInstance() {
        return new FragmentTopHeadlines();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void refreshData(Bundle bundle) {
        getData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}