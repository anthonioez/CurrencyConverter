package com.currencyconverter.android;

import android.content.Context;
import android.util.Log;

import com.currencyconverter.android.objects.ResponseCurrency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Loader
{
    public static final String TAG = Loader.class.getSimpleName();

    private Context context;
    private LoaderListener listener = null;
    private Call<ResponseCurrency> response = null;

    public Loader(Context context, LoaderListener listener)
    {
        this.listener = listener;
        this.context = context;

    }

    public void unload()
    {
        if(response != null)
        {
            response.cancel();
            response = null;
        }
    }

    public void load()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openexchangerates.org/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        if(listener != null) listener.onLoaderStarted();

        CurrencyService service = retrofit.create(CurrencyService.class);
        response = service.load(App.API_KEY);
        response.enqueue(new Callback<ResponseCurrency>()
        {
            @Override
            public void onResponse(Call<ResponseCurrency> call, Response<ResponseCurrency> response)
            {
                Log.i(TAG, response.message());

                if(response != null)
                {
                    ResponseCurrency result = response.body();
                    if(result != null)
                    {
                        if(result.base != null)
                        {
                            Prefs.setBase(context, result.base);

                            if(result.rates != null)
                            {
                                Log.i(TAG, "rates: " + result.rates.size());

                                Set<String> keys = result.rates.keySet();
                                for(String key : keys)
                                {
                                    Log.i(TAG, "key: " + key + ": " + result.rates.get(key));

                                    Prefs.setRate(context, key.toUpperCase(), result.rates.get(key));
                                }
                                Prefs.setStamp(context, System.currentTimeMillis());

                                if(listener != null) listener.onLoaderLoaded();

                                return;
                            }

                        }

                        if(listener != null) listener.onLoaderErrored("Invalid data!");
                    }
                    else
                    {
                        if(listener != null) listener.onLoaderErrored("Unable to load currency rates!");
                    }
                }
                else
                {
                    if(listener != null) listener.onLoaderErrored("Invalid response!");
                }
            }

            @Override
            public void onFailure(Call<ResponseCurrency> call, Throwable t)
            {
                //Utils.alert(ActivityLogin.this, getString(R.string.app_name), );

                if(listener != null) listener.onLoaderErrored("An error occurred!");
            }
        });
    }


    public interface LoaderListener
    {
        void onLoaderErrored(String str);
        void onLoaderLoaded();
        void onLoaderStarted();
    }
}
