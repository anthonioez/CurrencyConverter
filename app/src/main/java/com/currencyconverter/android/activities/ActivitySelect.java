package com.currencyconverter.android.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.currencyconverter.android.App;
import com.currencyconverter.android.Loader;
import com.currencyconverter.android.Prefs;
import com.currencyconverterdf.android.R;
import com.currencyconverter.android.Utils;
import com.currencyconverter.android.adapters.AdapterCurrency;
import com.currencyconverter.android.objects.CurrencyItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ActivitySelect extends AppCompatActivity implements AdapterCurrency.AdapterCurrencyListener, View.OnClickListener, Loader.LoaderListener
{
    private static int PAGE_SIZE = 20;

    private RecyclerView recyclerView;
    private Button buttonRefresh;
    private TextView textStatus;
    private ProgressBar progressBar;
    private EditText editSearch;
    private AdView adView;

    private LinearLayoutManager layoutManager;

    private AdapterCurrency adapterList;

    private Loader loader = null;

    private int slot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        slot = getIntent().getIntExtra("slot", 0);

        setContentView(R.layout.activity_select);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Choose Currency");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        adapterList = new AdapterCurrency(this, App.currencies, this);

        layoutManager = new LinearLayoutManager(this);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.listItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.GONE);
        buttonRefresh.setOnClickListener(this);

        editSearch = (EditText) findViewById(R.id.editSearch);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    adapterList.search(editSearch.getText().toString());
                    return true;
                }

                return false;
            }
        });

        editSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                adapterList.search(editSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        Utils.hideKeyboard(this, editSearch);

        adView = (AdView) findViewById(R.id.adView);

        updateStamp();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        unrefresh();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        setupAds(Utils.getPurchased(this));

        if(Prefs.getStamp(this) == 0)
        {
            refresh();
        }
        else
        {
            adapterList.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        if(editSearch.getText().toString().length() > 0)
        {
            editSearch.setText("");
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCurrencySelect(CurrencyItem item)
    {
        if(slot == 0)
        {
            App.setFrom(this, item);
        }
        else if(slot == 1)
        {
            App.setTo(this, item);
        }
        finish();
    }

    @Override
    public void onFavoriteSelect(CurrencyItem fav)
    {
        for(CurrencyItem item : App.favorites)
        {
            if(fav.code.equals(item.code))
            {
                Utils.alert(this, App.appName, "Favorite already added!");
                return;
            }
        }

        App.favorites.add(fav);

        App.saveFavorites(this);

        Utils.alert(this, App.appName, "Favorite added!");
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonRefresh)
        {
            refresh();
        }
    }

    private void updateStamp()
    {
        long stamp = Prefs.getStamp(this);
        textStatus.setText(stamp == 0 ? "" : String.format("Last updated: %s", Utils.getStamp(stamp)));
    }

    private void setupAds(boolean purchased)
    {
        if(purchased)
        {
            adView.setVisibility(View.GONE);
        }
        else
        {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    private void unrefresh()
    {
        if(loader != null)
        {
            loader.unload();
            loader = null;
        }
    }

    private void refresh()
    {
        loader = new Loader(this, this);
        loader.load();
    }

    private void refreshUI(boolean state)
    {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
        buttonRefresh.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onLoaderStarted()
    {
        refreshUI(true);
    }

    @Override
    public void onLoaderLoaded()
    {
        refreshUI(false);

        updateStamp();
    }

    @Override
    public void onLoaderErrored(String error)
    {
        refreshUI(false);

        Utils.toast(this, error);
    }
}
