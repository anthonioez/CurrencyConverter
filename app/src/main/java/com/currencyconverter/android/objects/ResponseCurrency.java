package com.currencyconverter.android.objects;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class ResponseCurrency
{
    @SerializedName("base")
    public String   base;

    @SerializedName("rates")
    public HashMap<String, Float> rates;
}
