package com.bassel.gonews.api;

import com.bassel.gonews.api.api_respones.ArticlesApiResponse;
import com.bassel.gonews.api.api_respones.SourcesApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by basselchaitani on 2/5/19.
 */

public interface ApiInterface {

    @GET("top-headlines")
    Call<ArticlesApiResponse> getTopHeadlines(@Query("country") String country);

    @GET("sources")
    Call<SourcesApiResponse> getSouces();

}
