package com.bassel.gonews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bassel.gonews.BuildConfig;
import com.bassel.gonews.R;
import com.bassel.gonews.config.Constants;
import com.bassel.gonews.utils.GeneralFunctions;
import com.bassel.gonews.utils.LocaleHelper;

/**
 * Created by basselchaitani on 2/6/19.
 */
public class FragmentSettings extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = FragmentSettings.class.getSimpleName();

    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    private RadioGroup rgLanguage;
    private TextView tvAppVersion;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void refreshData(Bundle bundle) {
        // DO NOTHING
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            if (LocaleHelper.getLanguage(getActivity()).equals(Constants.ARABCIC)) {
                rgLanguage.check(R.id.rb_arabic);
            } else {
                rgLanguage.check(R.id.rb_english);
            }
        }

        rgLanguage.setOnCheckedChangeListener(this);
        tvAppVersion.setText(BuildConfig.VERSION_NAME);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        String locale = Constants.ENGLISH;
        switch (checkedId) {
            case R.id.rb_arabic:
                locale = Constants.ARABCIC;
                break;
            case R.id.rb_english:
                locale = Constants.ENGLISH;
                break;
        }

        if (!LocaleHelper.getLanguage(getActivity()).equals(locale)) {
            LocaleHelper.setLocale(getActivity(), locale);
            GeneralFunctions.restartApp(getActivity());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rgLanguage = view.findViewById(R.id.rg_language);
        tvAppVersion = view.findViewById(R.id.tv_app_version);
    }
}