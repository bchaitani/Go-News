package com.bassel.gonews;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.bassel.gonews.config.Constants;
import com.bassel.gonews.utils.LocaleHelper;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class App extends Application {

    private static App appContext;

    public App() {
        appContext = this;
    }

    public static Context getContext() {

        if (appContext == null) {
            appContext = new App();
        }

        return appContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        String currentLang = LocaleHelper.getLanguage(base);

        if (TextUtils.isEmpty(currentLang)) {
            currentLang = Constants.ENGLISH;
        }

        super.attachBaseContext(LocaleHelper.onAttach(base, currentLang));
    }
}