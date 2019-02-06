package com.bassel.gonews.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bassel.gonews.App;
import com.bassel.gonews.BuildConfig;
import com.bassel.gonews.R;
import com.bassel.gonews.api.api_respones.ArticlesApiResponse;
import com.bassel.gonews.api.api_respones.SourcesApiResponse;
import com.bassel.gonews.config.Constants;
import com.bassel.gonews.utils.GeneralFunctions;
import com.bassel.gonews.utils.Logger;
import com.bassel.gonews.utils.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class ApiManager {

    private static ApiManager mInstance = new ApiManager();

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String STATUS_OK = "ok";

    public static ApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }

        return mInstance;
    }

    public void getSources(OnApiRequestListener<SourcesApiResponse> listener) {
        Call<SourcesApiResponse> call = ApiClient.getInstance().getApiService().getSources();
        requestSourcesCall(call, listener);
    }

    public void getTopHeadlines(int currentPage, OnApiRequestListener<ArticlesApiResponse> listener) {
        Call<ArticlesApiResponse> call = ApiClient.getInstance().getApiService().getTopHeadlines(GeneralFunctions.getCountry(), currentPage, Constants.PAGE_SIZE);
        requestTopHeadlinesCall(call, listener);
    }

    public void getTopHeadlinesBySource(int currentPage, String source, OnApiRequestListener<ArticlesApiResponse> listener) {
        Call<ArticlesApiResponse> call = ApiClient.getInstance().getApiService().getTopHeadlinesBySource(source, currentPage, Constants.PAGE_SIZE);
        requestTopHeadlinesCall(call, listener);
    }

    private void requestSourcesCall(@NonNull Call<SourcesApiResponse> call, final OnApiRequestListener<SourcesApiResponse> listener) {
        final Context context = App.getContext();
        if (context != null && listener != null) {
            if (!NetworkHelper.isNetworkAvailable(context)) {
                listener.onConnectionError();
            } else {
                call.enqueue(new Callback<SourcesApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        try {
                            SourcesApiResponse result = (SourcesApiResponse) response.body();
                            if (result != null) {
                                if (response.isSuccessful() && result.getStatus().equals(STATUS_OK)) {
                                    listener.onResultReady(result);
                                } else {
                                    listener.onApiError(result.getCode(), result.getMessage());
                                }
                            } else {
                                listener.onApiError(context.getString(R.string.title_general_error), context.getString(R.string.title_general_error));
                            }
                        } catch (ClassCastException e) {
                            Logger.e("requestCall", e.getMessage());
                            listener.onApiError(context.getString(R.string.title_general_error), e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        listener.onApiError(context.getString(R.string.title_general_error), t.getMessage());
                    }
                });
            }
        } else {
            throw new NullPointerException("OnApiRequestFinished must not be null");
        }
    }

    private void requestTopHeadlinesCall(@NonNull Call<ArticlesApiResponse> call, final OnApiRequestListener<ArticlesApiResponse> listener) {
        final Context context = App.getContext();
        if (context != null && listener != null) {
            if (!NetworkHelper.isNetworkAvailable(context)) {
                listener.onConnectionError();
            } else {
                call.enqueue(new Callback<ArticlesApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        try {
                            ArticlesApiResponse result = (ArticlesApiResponse) response.body();
                            if (result != null) {
                                if (response.isSuccessful() && result.getStatus().equals(STATUS_OK)) {
                                    listener.onResultReady(result);
                                } else {
                                    listener.onApiError(result.getCode(), result.getMessage());
                                }
                            } else {
                                listener.onApiError(context.getString(R.string.title_general_error), context.getString(R.string.title_general_error));
                            }
                        } catch (ClassCastException e) {
                            Logger.e("requestCall", e.getMessage());
                            listener.onApiError(context.getString(R.string.title_general_error), e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        listener.onApiError(context.getString(R.string.title_general_error), t.getMessage());
                    }
                });
            }
        } else {
            throw new NullPointerException("OnApiRequestFinished must not be null");
        }
    }
}