package com.bassel.gonews.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.bassel.gonews.utils.LocaleHelper;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
