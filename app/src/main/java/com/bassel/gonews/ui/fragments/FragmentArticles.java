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
import com.bassel.gonews.config.Constants;
import com.bassel.gonews.listeners.OnItemClickListener;
import com.bassel.gonews.listeners.OnLoadMoreListener;
import com.bassel.gonews.utils.Logger;
import com.bassel.statefullayout.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class FragmentArticles extends BaseFragment implements
        OnApiRequestListener<ArticlesApiResponse>,
        OnItemClickListener<Article>,
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMoreListener {

    private static final String TAG = FragmentArticles.class.getSimpleName();

    public static final String BUNDLE_SEARCH_QUERY = "search_query";

    private StatefulLayout mStatefulLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mArticlesRecyclerView;

    private List<Article> mArticlesList;
    private ArticlesRecyclerViewAdapter mArticlesRecyclerViewAdapter;

    private boolean isLoading = false;
    private int currentPage = 1;

    private String sourceId, searchQuery;

    public static FragmentArticles newInstance(Bundle bundle) {
        FragmentArticles f = new FragmentArticles();
        if (bundle != null) {
            f.sourceId = bundle.getString(FragmentExplore.BUNDLE_SOURCE);
            f.searchQuery = bundle.getString(BUNDLE_SEARCH_QUERY);
        }

        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void refreshData(Bundle bundle) {
        getTopHeadlinesArticles(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArticlesList = new ArrayList<>();
        mArticlesRecyclerViewAdapter = new ArticlesRecyclerViewAdapter(getActivity(), mArticlesList);
        mArticlesRecyclerViewAdapter.setOnItemClickListener(this);
        mArticlesRecyclerViewAdapter.setOnLoadMoreListener(this);
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticlesRecyclerView.setAdapter(mArticlesRecyclerViewAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        getTopHeadlinesArticles(false);
    }

    @Override
    public void onRefresh() {
        refreshData(null);
    }

    @Override
    public void onLoadMore() {
        getTopHeadlinesArticles(false);
    }

    @Override
    public void onItemClick(Article article, int position) {
        if (mFragmentNavigation != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(FragmentArticleDetails.BUNDLE_ARTICLE, article);
            mFragmentNavigation.pushFragment(FragmentArticleDetails.newInstance(bundle));
        }
    }

    private void getTopHeadlinesArticles(boolean isRefresh) {
        if (isLoading) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        if (mArticlesList.size() == 0) {
            mStatefulLayout.showLoading();
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        if (isRefresh) {
            mArticlesList.clear();
        }

        currentPage = (mArticlesList.size() / Constants.PAGE_SIZE) + 1;

        isLoading = true;

        if (sourceId != null) {
            ApiManager.getInstance().getTopHeadlinesBySource(currentPage, sourceId, this);
        } else if (searchQuery != null) {
            ApiManager.getInstance().searchArticles(currentPage, searchQuery, this);
        } else {
            ApiManager.getInstance().getTopHeadlines(currentPage, this);
        }
    }

    @Override
    public void onResultReady(ArticlesApiResponse result) {
        Logger.i(TAG, "getTopHeadlines Success");

        mArticlesList.addAll(result.getArticles());

        Logger.e("currentPage", currentPage + "");
        Logger.e("result.getTotalResults()", result.getTotalResults() + "");
        Logger.e("mArticlesList.size()", mArticlesList.size() + "");

        if (result.getTotalResults() == mArticlesList.size()) {
            mArticlesRecyclerViewAdapter.setMoreDataAvailable(false);
        } else {
            mArticlesRecyclerViewAdapter.setMoreDataAvailable(true);
        }

        mArticlesRecyclerViewAdapter.notifyDataChanged();

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
                getTopHeadlinesArticles(true);
            }
        });
    }

    @Override
    public void onApiError(String code, String message) {
        Logger.e(TAG, "getTopHeadlines Error: " + message);
        mStatefulLayout.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTopHeadlinesArticles(true);
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