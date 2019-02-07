package com.bassel.gonews;

import android.app.Application;
import android.content.Context;

import com.bassel.gonews.config.Constants;
import com.bassel.gonews.utils.GeneralFunctions;
import com.bassel.gonews.utils.LocaleHelper;
import com.bassel.gonews.utils.Logger;

/**
 * Created by basselchaitani on 2/5/19.
 */

public class App extends Application {

    private static App appContext;

    private Thread.UncaughtExceptionHandler defaultUEH;

    public App() {
        appContext = this;

        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // here I do logging of exception to a db
                Logger.e("unCaughtException", ex.getMessage());
                GeneralFunctions.restartApp(getContext());

                // re-throw critical exception further to the os (important)
                defaultUEH.uncaughtException(thread, ex);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    public static Context getContext() {

        if (appContext == null) {
            appContext = new App();
        }

        return appContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Constants.ENGLISH));
    }
}