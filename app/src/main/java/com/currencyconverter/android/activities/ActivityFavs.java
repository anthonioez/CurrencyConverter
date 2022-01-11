package com.currencyconverter.android.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.currencyconverter.android.App;
import com.currencyconverterdf.android.R;
import com.currencyconverter.android.Utils;
import com.currencyconverter.android.adapters.AdapterFavorites;
import com.currencyconverter.android.objects.CurrencyItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ActivityFavs extends AppCompatActivity implements AdapterFavorites.AdapterFavoritesListener
{
    private RecyclerView recyclerView;
    private AdView adView;

    private LinearLayoutManager layoutManager;

    private AdapterFavorites adapterList;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favorite Currencies");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        adapterList = new AdapterFavorites(this, this);

        layoutManager = new LinearLayoutManager(this);

        textEmpty = (TextView) findViewById(R.id.textEmpty);

        recyclerView = (RecyclerView) findViewById(R.id.listItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        adView = (AdView) findViewById(R.id.adView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        setupAds(Utils.getPurchased(this));

        App.loadFavorites(this);

        adapterList.notifyDataSetChanged();
        updateEmpty();
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
    public void onCurrencySelect(final CurrencyItem item)
    {
        CurrencyItem fav = null;
        for(CurrencyItem i : App.favorites)
        {
            if(i.code.equals(item.code))
            {
                fav = i;
                break;
            }
        }

        CharSequence[] items = {};
        if(fav == null)
            items = new CharSequence[] {"Set as From", "Set as To"};
        else
            items = new CharSequence[] {"Set as From", "Set as To", "Remove from Favorites"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CurrencyItem favorite = fav;
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                if(pos == 0)
                {
                    App.setFrom(ActivityFavs.this, item);
                    finish();
                }
                else if(pos == 1)
                {
                    App.setTo(ActivityFavs.this, item);
                    finish();
                }
                else if(favorite != null)
                {
                    App.favorites.remove(favorite);
                    App.saveFavorites(ActivityFavs.this);
                    adapterList.notifyDataSetChanged();

                    updateEmpty();
                }
            }
        });
        builder.show();
    }

    private void updateEmpty()
    {
        textEmpty.setVisibility(adapterList.getItemCount() == 0 ? View.VISIBLE : View.GONE);
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
}
