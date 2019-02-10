package com.bassel.gonews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bassel.gonews.R;
import com.bassel.gonews.api.model.Article;
import com.bassel.gonews.utils.GeneralFunctions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by basselchaitani on 2/6/19.
 */
public class FragmentArticleDetails extends BaseFragment {

    private static final String TAG = FragmentArticleDetails.class.getSimpleName();

    public static final String BUNDLE_ARTICLE = "article";

    private ImageView ivArticle;
    private TextView tvAuthorName, tvPublishedAt, tvTitle, tvContent;

    private Article mArticle;

    public static FragmentArticleDetails newInstance(Bundle bundle) {
        FragmentArticleDetails f = new FragmentArticleDetails();
        f.mArticle = bundle.getParcelable(BUNDLE_ARTICLE);
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_article_details;
    }

    @Override
    public void refreshData(Bundle bundle) {
        // DO NOTHING
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mArticle != null) {
            fillArticleDetailsInfo();
        }
    }

    /**
     * Populate article details data
     */
    private void fillArticleDetailsInfo() {
        if (getActivity() != null && mArticle != null) {
            Glide.with(getActivity())
                    .load(mArticle.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(GeneralFunctions.getPlaceholderColorDrawable(getActivity()))
                            .error(GeneralFunctions.getPlaceholderColorDrawable(getActivity()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop())
                    .into(ivArticle);

            tvTitle.setText(mArticle.getTitle());
            tvContent.setText(mArticle.getContent());
            tvPublishedAt.setText(GeneralFunctions.dateFormat(mArticle.getPublishedAt()));
            String author = mArticle.getAuthor();

            if (TextUtils.isEmpty(author)) {
                tvAuthorName.setVisibility(View.GONE);
            } else {
                tvAuthorName.setVisibility(View.VISIBLE);
                tvAuthorName.setText(author);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (mArticle != null && getActivity() != null) {
                GeneralFunctions.shareTextUrl(getActivity(), mArticle.getTitle(), mArticle.getUrl());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivArticle = view.findViewById(R.id.iv);
        tvAuthorName = view.findViewById(R.id.tv_author_name);
        tvPublishedAt = view.findViewById(R.id.tv_published_at);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
    }
}