package com.currencyconverter.android;

import com.currencyconverter.android.objects.ResponseCurrency;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyService
{
    @GET("latest.json")
    Call<ResponseCurrency> load(@Query("app_id") String app_id);

    @GET("latest.json")
    Call<ResponseBody> load2(@Query("app_id") String app_id);
}

