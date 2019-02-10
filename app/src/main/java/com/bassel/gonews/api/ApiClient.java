package com.bassel.gonews.api;

import com.bassel.gonews.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class ApiClient {

    private static final String TAG = ApiClient.class.getSimpleName();

    private static final boolean isDebugMode = BuildConfig.DEBUG;

    private static final int TIMEOUT_IN_MIN = 30;

    private static ApiClient apiClient;
    private ApiInterface apiService;

    private static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }

        return apiClient;
    }

    private ApiClient() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (isDebugMode) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }

        okHttpClientBuilder.readTimeout(TIMEOUT_IN_MIN, TimeUnit.MINUTES);
        okHttpClientBuilder.connectTimeout(TIMEOUT_IN_MIN, TimeUnit.MINUTES);
        okHttpClientBuilder.writeTimeout(TIMEOUT_IN_MIN, TimeUnit.MINUTES);

        okHttpClientBuilder.addInterceptor(new ApiRequestInterceptor());

        OkHttpClient httpClient = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApiService() {
        return apiService;
    }
}