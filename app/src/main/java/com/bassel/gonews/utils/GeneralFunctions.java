package com.bassel.gonews.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;

import com.bassel.gonews.R;
import com.bassel.gonews.ui.activities.SplashScreenActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralFunctions {

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static void restartApp(Context context) {
        Intent i = new Intent(context, SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
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
