package com.currencyconverter.android.objects;

import android.content.Context;

import com.currencyconverter.android.App;

import java.text.DecimalFormat;

public class CurrencyItem
{
    public String symbol;
    public String code;
    public String name;
    public int flag;

    public CurrencyItem(Context context, String symbol, String code, String name)
    {
        this.symbol = symbol;
        this.code = code;
        this.name = name;
        this.flag = context.getResources().getIdentifier(flag(), "drawable", context.getPackageName());
    }

    public CurrencyItem()
    {
        this.symbol = "";
        this.code = "";
        this.name = "";
    }

    public String amount(Context context)
    {
        if (App.value == 0)
            return "";
        else
            return symbol + value(context);
    }

    public String flag()
    {
        String flg = code.toLowerCase();

        if(flg.equals("try"))
            return "tryy";

        return flg;
    }

    public String value(Context context)
    {
        float value = (App.rate(context, App.slotFr.code, code) * App.value);

        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return formatter.format(value);
    }
}

