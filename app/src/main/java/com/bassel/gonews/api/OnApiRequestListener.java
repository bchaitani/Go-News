package com.bassel.gonews.api;

/**
 * Created by basselchaitani on 2/5/19.
 */

public interface OnApiRequestListener<T> {

    void onResultReady(T result);

    void onConnectionError();

    void onApiError(String code, String message);
}
