package com.bassel.gonews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bassel.gonews.R;
import com.bassel.gonews.adapters.ArticlesRecyclerViewAdapter;
import com.bassel.gonews.api.ApiManager;
import com.bassel.gonews.api.OnApiRequestListener;
import com.bassel.gonews.api.api_respones.ArticlesApiResponse;
import com.bassel.gonews.api.model.Article;
import com.bassel.gonews.utils.Logger;
import com.bassel.statefullayout.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class FragmentTopHeadlines extends BaseFragment {

    private static final String TAG = FragmentTopHeadlines.class.getSimpleName();

    private StatefulLayout mStatefulLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mArticlesRecyclerView;

    private List<Article> mArticlesList;
    private ArticlesRecyclerViewAdapter mArticlesRecyclerViewAdapter;

    private boolean isLoading = false;

    public static FragmentTopHeadlines newInstance() {
        return new FragmentTopHeadlines();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void refreshData(Bundle bundle) {
        mArticlesList.clear();
        getTopHeadlinesArticles();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArticlesList = new ArrayList<>();
        mArticlesRecyclerViewAdapter = new ArticlesRecyclerViewAdapter(getActivity(), mArticlesList);
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticlesRecyclerView.setAdapter(mArticlesRecyclerViewAdapter);

        getTopHeadlinesArticles();
    }

    private void getTopHeadlinesArticles() {
        if (isLoading) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        if (mArticlesList.size() == 0) {
            mStatefulLayout.showLoading();
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        isLoading = true;

        ApiManager.getInstance().getTopHeadlines(new OnApiRequestListener<ArticlesApiResponse>() {
            @Override
            public void onResultReady(ArticlesApiResponse result) {
                Logger.i(TAG, "getTopHeadlines Success");

                mArticlesList.addAll(result.getArticles());

                mArticlesRecyclerViewAdapter.notifyDataSetChanged();

                mStatefulLayout.showContent();
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onConnectionError() {
                Logger.w(TAG, "getTopHeadlines Connection Error");
                // TODO
            }

            @Override
            public void onApiError(String code, String message) {
                Logger.e(TAG, "getTopHeadlines Error: " + message);
                // TODO
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStatefulLayout = view.findViewById(R.id.sl);
        mSwipeRefreshLayout = view.findViewById(R.id.srl);
        mArticlesRecyclerView = view.findViewById(R.id.recycler_view);
    }
}