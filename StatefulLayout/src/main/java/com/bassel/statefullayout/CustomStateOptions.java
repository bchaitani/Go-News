package com.bassel.statefullayout;

import android.support.annotation.DrawableRes;
import android.view.View;

import java.io.Serializable;

/**
 * Created by basselchaitani on 5/10/18
 */
public class CustomStateOptions implements Serializable {

    @DrawableRes
    private int imageRes;
    private boolean isLoading;
    private String message;
    private String buttonText;
    private View.OnClickListener buttonClickListener;

    public CustomStateOptions image(@DrawableRes int val) {
        imageRes = val;
        return this;
    }

    public CustomStateOptions loading() {
        isLoading = true;
        return this;
    }

    public CustomStateOptions message(String val) {
        message = val;
        return this;
    }

    public CustomStateOptions buttonText(String val) {
        buttonText = val;
        return this;
    }

    public CustomStateOptions buttonClickListener(View.OnClickListener val) {
        buttonClickListener = val;
        return this;
    }

    public int getImageRes() {
        return imageRes;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getMessage() {
        return message;
    }

    public String getButtonText() {
        return buttonText;
    }

    public View.OnClickListener getClickListener() {
        return buttonClickListener;
    }
}
