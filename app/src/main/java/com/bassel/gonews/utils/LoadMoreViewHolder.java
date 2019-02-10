package com.bassel.gonews.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bassel.gonews.R;

/**
 * Created by basselchaitani on 2/6/19.
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
        ProgressBar mProgressBar = itemView.findViewById(R.id.load_more_progress_bar);
        GeneralFunctions.changeProgressBarColor(itemView.getContext(), mProgressBar, R.color.colorPrimary);
    }
}
