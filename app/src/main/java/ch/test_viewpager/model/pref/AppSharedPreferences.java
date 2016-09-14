package ch.test_viewpager.model.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ChaeHyun on 2016. 9. 13..
 */
public class AppSharedPreferences {
    private final String PREF_NAME = "pref";

    public final static String PREF_INTRO_USER_AGREEMENT = "PREF_USER_AGREEMENT";
    public final static String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";

    static Context mContext;

    public AppSharedPreferences(Context c) {
        mContext = c;
    }

    public void put(String key, String value)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getValue(String key, String value)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, value);
        }
        catch(Exception e)
        {
            return value;
        }
    }

    public boolean getValue(String key, boolean value)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, value);
        }
        catch(Exception e)
        {
            return value;
        }
    }

    public void removeValue(String key)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(key);
        editor.commit();
    }

    public void clear(String key)
    {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.commit();
    }

}
