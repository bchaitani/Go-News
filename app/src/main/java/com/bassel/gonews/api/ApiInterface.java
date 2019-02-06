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
    Call<ArticlesApiResponse> getTopHeadlines(@Query("country") String country, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("top-headlines")
    Call<ArticlesApiResponse> getTopHeadlinesBySource(@Query("sources") String source, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("sources")
    Call<SourcesApiResponse> getSources();

}
