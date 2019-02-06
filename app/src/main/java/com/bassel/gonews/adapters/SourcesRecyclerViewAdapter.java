package com.bassel.gonews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bassel.gonews.R;
import com.bassel.gonews.api.model.Source;
import com.bassel.gonews.listeners.OnItemClickListener;

import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class SourcesRecyclerViewAdapter extends RecyclerView.Adapter<SourcesRecyclerViewAdapter.SourceViewHolder> {

    private LayoutInflater mInflater;

    private List<Source> mSourcesList;
    private OnItemClickListener<Source> mOnItemClickListener;

    public SourcesRecyclerViewAdapter(Context context, List<Source> articles) {
        this.mSourcesList = articles;

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_source, parent, false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        Source source = mSourcesList.get(position);
        holder.bindSource(source);

    }

    @Override
    public int getItemCount() {
        return mSourcesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<Source> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSourceName, tvSourceDescription, tvSourceCategory;

        SourceViewHolder(final View itemView) {
            super(itemView);

            tvSourceName = itemView.findViewById(R.id.tv_source_name);
            tvSourceDescription = itemView.findViewById(R.id.tv_source_description);
            tvSourceCategory = itemView.findViewById(R.id.tv_source_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mSourcesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

        }

        void bindSource(Source source) {

            tvSourceName.setText(source.getName());
            tvSourceDescription.setText(source.getDescription());
            tvSourceCategory.setText(source.getCategory().toUpperCase());
        }
    }
}