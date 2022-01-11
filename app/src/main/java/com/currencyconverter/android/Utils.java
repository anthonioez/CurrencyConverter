package com.currencyconverter.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Date;

public class Utils
{
    public static void openUrl(Context context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openMail(Context context, String email, String subject, String body)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = String.format("mailto:%s?subject=%s&body=%s", Uri.encode(email), Uri.encode(subject),  Uri.encode(body));
        intent.setData(Uri.parse(uriText));
        context.startActivity(Intent.createChooser(intent, "Send mail..."));
    }

    public static boolean getPurchased(Context context)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("purchased", false);
    }

    public static void setPurchased(Context context, boolean value)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("purchased", value);
        editor.commit();
    }

    public static String getVersion(Context context)
    {
        String versionCode = "";
        try
        {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return versionCode;
    }


    public static String getStamp(long stamp)
    {
        return (String) DateFormat.format("yy/MM/dd HH:mm", new Date(stamp));
    }

    public static void toast(Context context, String text)
    {
        if (context == null || text == null || text.length() == 0)
            return;

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void alert(Context context, String title, String msg)
    {
        if( ((Activity) context).isFinishing() ) return;

        if(TextUtils.isEmpty(msg)) return;

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void hideKeyboard(Context context, View vw)
    {
        if (context == null)
            return;

        if (vw == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(vw.getWindowToken(), 0);
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }

        return false;
    }

    public static float getFloat(String input)
    {
        float value = 0;

        try
        {
            value = Float.parseFloat(input);
        }
        catch (Exception e)
        {

        }

        return value;
    }
}
