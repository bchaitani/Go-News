package com.bassel.gonews.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.format.DateFormat;
import android.widget.ProgressBar;

import com.bassel.gonews.R;
import com.bassel.gonews.ui.activities.SplashScreenActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralFunctions {

    public static void changeProgressBarColor(Context context, ProgressBar progressBar, @ColorRes int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
        }
    }

    public static void shareTextUrl(Context context, String title, String url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.title_share_article)));
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static void restartApp(@NonNull Activity activity) {
        Intent i = new Intent(activity, SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
        activity.finish();
    }

    public static ColorDrawable getPlaceholderColorDrawable(Context context) {
        return new ColorDrawable(ContextCompat.getColor(context, R.color.place_holder));
    }

    public static String dateFormat(String oldDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(oldDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldDate;
        }

        return newDate;
    }

    public static String dateToTimeFormat(String oldStringDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            Date date = sdf.parse(oldStringDate);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());

            Calendar now = Calendar.getInstance();

            final String timeFormatString = "h:mm aa";
            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
            final long HOURS = 60 * 60 * 60;

            if (now.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
                return "Today " + DateFormat.format(timeFormatString, cal);
            } else if (now.get(Calendar.DATE) - cal.get(Calendar.DATE) == 1) {
                return "Yesterday " + DateFormat.format(timeFormatString, cal);
            } else if (now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                return DateFormat.format(dateTimeFormatString, cal).toString();
            } else {
                return DateFormat.format("MMMM dd yyyy, h:mm aa", cal).toString();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
