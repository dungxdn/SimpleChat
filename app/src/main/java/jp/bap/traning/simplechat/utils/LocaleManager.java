package jp.bap.traning.simplechat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Admin on 7/23/2018.
 */

public class LocaleManager {
    private static final String LANGUAGE_KEY = "CHOOSE_LANGUAGE";

    public static Context setLocale(Context c, String language) {
        SharedPrefs.getInstance().putData(LANGUAGE_KEY, language);
        Log.d("LocaleManager", "setNewLocale: " + language);
        return updateResources(c, language);
    }

    public static Context setLocale(Context c, Locale newLocale) {
        SharedPrefs.getInstance().putData(LANGUAGE_KEY, newLocale.toString());
        return updateResources(c, newLocale);
    }


    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        return updateResources(context, locale);
    }

    private static Context updateResources(Context context, Locale locale) {
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
            // I need to set this so that recall setText(R.string.xxxx) works
            res.updateConfiguration(config, res.getDisplayMetrics());
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
}
