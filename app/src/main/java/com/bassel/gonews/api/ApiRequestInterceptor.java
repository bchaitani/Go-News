package com.bassel.gonews.api;

import android.support.annotation.NonNull;

import com.bassel.gonews.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by basselchaitani on 2/5/19.
 */

public class ApiRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String apiKey = BuildConfig.API_KEY;
        HttpUrl url = request.url().newBuilder().addQueryParameter("apiKey", apiKey).build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}