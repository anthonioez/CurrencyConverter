package com.currencyconverter.android;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs
{
    private static final String KEY_REGISTERED      = "registered";
    private static final String KEY_PHONE           = "phone";
    private static final String KEY_PIN             = "pin";

    public static void setString(Context context, String mKey, String mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mKey, mValue);
        editor.commit();
    }

    public static String getString(Context context, String mKey, String mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getString(mKey, mDefValue);
    }

    public static void setBoolean(Context context, String mKey, boolean mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mKey, mValue);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String mKey, boolean mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(mKey, mDefValue);
    }

    public static void setInt(Context context, String mKey, int mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mKey, mValue);
        editor.commit();
    }

    public static int getInt(Context context, String mKey, int mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(mKey, mDefValue);
    }

    public static void setLong(Context context, String mKey, long mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(mKey, mValue);
        editor.commit();
    }

    public static long getLong(Context context, String mKey, long mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(mKey, mDefValue);
    }

    public static void setFloat(Context context, String mKey, float mValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(mKey, mValue);
        editor.commit();
    }

    public static float getFloat(Context context, String mKey, float mDefValue)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(mKey, mDefValue);
    }

    public static void Dummy()
    {

    }

    public static void setStamp(Context ctx, long value)
    {
        setLong(ctx, "stamp", value);
    }
    public static long getStamp(Context ctx)
    {
        return getLong(ctx, "stamp", 0);
    }

    public static void setBase(Context ctx, String base)
    {
        setString(ctx, "base", base);
    }
    public static String getBase(Context ctx)
    {
        return getString(ctx, "base", "USD");
    }


    public static void setRate(Context ctx, String key, float value)
    {
        setFloat(ctx, key, value);
    }
    public static float getRate(Context ctx, String key, float value)
    {
        return getFloat(ctx, key, value);
    }

    public static void setFrom(Context ctx, String value)
    {
        setString(ctx, "from", value);
    }
    public static String getFrom(Context ctx)
    {
        return getString(ctx, "from", "USD");
    }

    public static void setTo(Context ctx, String value)
    {
        setString(ctx, "to", value);
    }
    public static String getTo(Context ctx)
    {
        return getString(ctx, "to", "EUR");
    }

    public static void setFavs(Context ctx, String value)
    {
        setString(ctx, "favs", value);
    }
    public static String getFavs(Context ctx)
    {
        return getString(ctx, "favs", "");
    }
}
