package com.bassel.gonews.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bassel.bottombar.BottomBar;
import com.bassel.bottombar.OnTabReselectListener;
import com.bassel.bottombar.OnTabSelectListener;
import com.bassel.gonews.R;
import com.bassel.gonews.ui.fragments.BaseFragment;
import com.bassel.gonews.ui.fragments.FragmentExplore;
import com.bassel.gonews.ui.fragments.FragmentSettings;
import com.bassel.gonews.ui.fragments.FragmentArticles;
import com.bassel.gonews.utils.navigation_utils.FragmentNavigationController;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        BaseFragment.FragmentNavigation,
        FragmentNavigationController.TransactionListener,
        FragmentNavigationController.RootFragmentListener,
        OnTabSelectListener,
        OnTabReselectListener {

    private static final int TABS_COUNT = 3;

    private static final int INDEX_TOP_HEADLINES = FragmentNavigationController.TAB1;
    private static final int INDEX_EXPLORE = FragmentNavigationController.TAB2;
    private static final int INDEX_SETTINGS = FragmentNavigationController.TAB3;

    private FragmentNavigationController mNavController;

    private BottomBar mBottomBar;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setIcon(R.drawable.ic_toolbar);
        }


        initViews();

        mNavController = FragmentNavigationController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, TABS_COUNT)
                .build();

        mBottomBar.setOnTabSelectListener(this);
        mBottomBar.setOnTabReselectListener(this);

        if (savedInstanceState == null) {
            mBottomBar.selectTabAtPosition(INDEX_TOP_HEADLINES);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(int tabId) {
        switch (tabId) {
            case R.id.navigation_top_head_lines:
                mNavController.switchTab(INDEX_TOP_HEADLINES);
                break;
            case R.id.navigation_explore:
                mNavController.switchTab(INDEX_EXPLORE);
                break;
            case R.id.navigation_settings:
                mNavController.switchTab(INDEX_SETTINGS);
                break;
        }

        invalidateOptionsMenu();
    }

    @Override
    public void onTabReSelected(int tabId) {
        mNavController.clearStack();
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_TOP_HEADLINES:
                return FragmentArticles.newInstance(null);
            case INDEX_EXPLORE:
                return FragmentExplore.newInstance();
            case INDEX_SETTINGS:
                return FragmentSettings.newInstance();
        }

        invalidateOptionsMenu();
        supportInvalidateOptionsMenu();
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());

            if (!mNavController.isRootFragment()) {
                getSupportActionBar().setIcon(0);
            } else {
                getSupportActionBar().setIcon(R.drawable.ic_toolbar);
            }
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragmentNavigationController.TransactionType transactionType) {
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());

            if (!mNavController.isRootFragment()) {
                getSupportActionBar().setIcon(0);
            } else {
                getSupportActionBar().setIcon(R.drawable.ic_toolbar);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.trim().length() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentArticles.BUNDLE_SEARCH_QUERY, s);
            mNavController.pushFragment(FragmentArticles.newInstance(bundle));

            if (mSearchView != null) {
                mSearchView.clearFocus();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getResources().getString(R.string.hint_search_articles));
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mBottomBar = findViewById(R.id.bottomBar);
    }
}
