package com.bassel.gonews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bassel.gonews.R;
import com.bassel.gonews.adapters.SourcesRecyclerViewAdapter;
import com.bassel.gonews.api.ApiManager;
import com.bassel.gonews.api.OnApiRequestListener;
import com.bassel.gonews.api.api_respones.SourcesApiResponse;
import com.bassel.gonews.api.model.Source;
import com.bassel.gonews.listeners.OnItemClickListener;
import com.bassel.gonews.utils.Logger;
import com.bassel.statefullayout.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class FragmentExplore extends BaseFragment implements OnItemClickListener<Source>, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = FragmentExplore.class.getSimpleName();

    public static final String BUNDLE_SOURCE = "source";

    private StatefulLayout mStatefulLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mSourcesRecyclerView;

    private List<Source> mSourcesList;
    private SourcesRecyclerViewAdapter mSourcesRecyclerViewAdapter;

    private boolean isLoading = false;

    public static FragmentExplore newInstance() {
        return new FragmentExplore();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void refreshData(Bundle bundle) {
        mSourcesList.clear();
        getSources(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSourcesList = new ArrayList<>();
        mSourcesRecyclerViewAdapter = new SourcesRecyclerViewAdapter(getActivity(), mSourcesList);
        mSourcesRecyclerViewAdapter.setOnItemClickListener(this);
        mSourcesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mSourcesRecyclerView.setAdapter(mSourcesRecyclerViewAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        getSources(false);
    }

    @Override
    public void onRefresh() {
        refreshData(null);
    }

    @Override
    public void onItemClick(Source source, int position) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_SOURCE, source.getSourceId());
            mFragmentNavigation.pushFragment(FragmentArticles.newInstance(bundle));
        }
    }

    private void getSources(boolean isRefresh) {
        if (isLoading) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        if (mSourcesList.size() == 0) {
            mStatefulLayout.showLoading();
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        isLoading = true;

        ApiManager.getInstance().getSources(new OnApiRequestListener<SourcesApiResponse>() {
            @Override
            public void onResultReady(SourcesApiResponse result) {
                Logger.i(TAG, "getTopHeadlines Success");

                mSourcesList.addAll(result.getSources());

                mSourcesRecyclerViewAdapter.notifyDataSetChanged();

                mStatefulLayout.showContent();
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onConnectionError() {
                Logger.w(TAG, "getTopHeadlines Connection Error");
                mStatefulLayout.showError(getResources().getString(R.string.error_connection_error), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSources(true);
                    }
                });
            }

            @Override
            public void onApiError(String code, String message) {
                Logger.e(TAG, "getTopHeadlines Error: " + message);
                mStatefulLayout.showError(message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSources(true);
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStatefulLayout = view.findViewById(R.id.sl);
        mSwipeRefreshLayout = view.findViewById(R.id.srl);
        mSourcesRecyclerView = view.findViewById(R.id.recycler_view);
    }
}