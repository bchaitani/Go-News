package com.bassel.gonews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bassel.gonews.R;
import com.bassel.gonews.api.model.Article;
import com.bassel.gonews.listeners.OnItemClickListener;
import com.bassel.gonews.listeners.OnLoadMoreListener;
import com.bassel.gonews.utils.GeneralFunctions;
import com.bassel.gonews.utils.LoadMoreViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOAD_MORE = 1;

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Article> mArticlesList;
    private OnItemClickListener<Article> mOnItemClickListener;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public ArticlesRecyclerViewAdapter(Context context, List<Article> articles) {
        this.mContext = context;
        this.mArticlesList = articles;

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            return new LoadMoreViewHolder(mInflater.inflate(R.layout.view_load_more, parent, false));
        } else {
            return new ArticleViewHolder(mInflater.inflate(R.layout.row_article, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && onLoadMoreListener != null) {
            isLoading = true;
            onLoadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            Article article = mArticlesList.get(position);
            ((ArticleViewHolder) holder).bindArticle(article);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isMoreDataAvailable && mArticlesList.size() > 0 && (position == mArticlesList.size() - 1)) ? VIEW_TYPE_LOAD_MORE : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mArticlesList == null ? 0 : mArticlesList.size();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener<Article> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivArticle;
        private TextView tvAuthorName, tvPublishedAt, tvTitle, tvDescription, tvSource, tvTime;

        ArticleViewHolder(final View itemView) {
            super(itemView);

            ivArticle = itemView.findViewById(R.id.iv);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvPublishedAt = itemView.findViewById(R.id.tv_published_at);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvSource = itemView.findViewById(R.id.tv_source);
            tvTime = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mArticlesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

        }

        void bindArticle(Article article) {

            Glide.with(mContext)
                    .load(article.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(GeneralFunctions.getPlaceholderColorDrawable(mContext))
                            .error(GeneralFunctions.getPlaceholderColorDrawable(mContext))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop())
                    .into(ivArticle);

            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvSource.setText(article.getSource().getName());
            tvTime.setText(String.format("%s %s", " \u2022 ", GeneralFunctions.dateToTimeFormat(article.getPublishedAt())));
            tvPublishedAt.setText(GeneralFunctions.dateFormat(article.getPublishedAt()));
            String author = article.getAuthor();

            if (TextUtils.isEmpty(author)) {
                tvAuthorName.setVisibility(View.GONE);
            } else {
                tvAuthorName.setVisibility(View.VISIBLE);
                tvAuthorName.setText(author);
            }
        }
    }
}