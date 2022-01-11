package com.currencyconverter.android.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.currencyconverter.android.App;
import com.currencyconverter.android.Keypad;
import com.currencyconverter.android.Loader;
import com.currencyconverter.android.Prefs;
import com.currencyconverterdf.android.R;
import com.currencyconverter.android.Utils;
import com.currencyconverter.android.objects.CurrencyItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler, Keypad.KeypadListener, Loader.LoaderListener
{
    private String productID = "android.test.purchased";

    private String licenseKey = "";

    private BillingProcessor biller;

    private LinearLayout layoutTo;
    private ImageView imageFlagTo;
    private TextView textCodeTo;
    private TextView textValueTo;

    private LinearLayout layoutFrom;
    private ImageView imageFlagFrom;
    private TextView textCodeFrom;
    private TextView textValueFrom;

    private ImageButton buttonSwap;
    private FrameLayout layoutKeypad;
    private ProgressBar progressBar;
    private AdView adView;

    private Keypad keypad;
    private String input = "";
    private MenuItem payItem = null;
    private Loader loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), null);

        biller = new BillingProcessor(this, licenseKey, this);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(App.appName);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        layoutFrom = (LinearLayout)findViewById(R.id.layoutFrom);
        layoutFrom.setOnClickListener(this);
        imageFlagFrom = (ImageView) findViewById(R.id.imageFlagFrom);
        textCodeFrom = (TextView)findViewById(R.id.textCodeFrom);
        textValueFrom = (TextView)findViewById(R.id.textValueFrom);

        layoutTo = (LinearLayout)findViewById(R.id.layoutTo);
        layoutTo.setOnClickListener(this);
        imageFlagTo = (ImageView) findViewById(R.id.imageFlagTo);
        textCodeTo = (TextView)findViewById(R.id.textCodeTo);
        textValueTo = (TextView)findViewById(R.id.textValueTo);

        buttonSwap = (ImageButton)findViewById(R.id.buttonSwap);
        buttonSwap.setOnClickListener(this);

        layoutKeypad = (FrameLayout)findViewById(R.id.layoutKeypad);
        keypad = new Keypad(this, layoutKeypad, this);

        adView = (AdView) findViewById(R.id.adView);

        App.value = 0;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        setupAds(Utils.getPurchased(this));

        updateStatics();
        updateData();

        long now = System.currentTimeMillis();
        long diff = now - Prefs.getStamp(this);

        if (now == 0 || diff > 30*60 * 1000)     //1 hour
        {
            refresh();
        }
    }

    @Override
    protected void onDestroy()
    {
        if (biller != null)
        {
            biller.release();
        }

        unrefresh();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        payItem = menu.findItem(R.id.action_pay);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(payItem != null) payItem.setVisible(!Utils.getPurchased(this));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_favorites:
                favorites();
                return true;

            case R.id.action_share:
                share();
                return true;

            case R.id.action_contact:
                contact();
                return true;

            case R.id.action_rate:
                rate();
                return true;

            case R.id.action_more:
                more();
                return true;

            case R.id.action_pay:
                pay();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /*if (!biller..handleActivityResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view)
    {
        if(view == layoutFrom)
        {
            select(0);
        }
        else if(view == layoutTo)
        {
            select(1);
        }
        else if(view == buttonSwap)
        {
            swap();
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
    }

    private void favorites()
    {
        Intent intent = new Intent(this, ActivityFavs.class);
        startActivity(intent);
    }

    private void select(int slot)
    {
        Intent intent = new Intent(this, ActivitySelect.class);
        intent.putExtra("slot", slot);
        startActivity(intent);
    }

    private void swap()
    {
        CurrencyItem temp = App.slotFr;
        App.slotFr = App.slotTo;
        App.slotTo = temp;

        updateData();
        updateStatics();

        App.saveSlots(this);
    }

    private void share()
    {
        String share = String.format("Download %s on Google Play Store at \n\nhttps://play.google.com/store/apps/details?id=%s", getString(R.string.app_name), getPackageName());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, share);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        Intent openin = Intent.createChooser(intent, "Share To...");
        startActivity(openin);
    }

    private void contact()
    {
        String body = "";
        body += "\r\nVersion: " + Utils.getVersion(this);
        body += "\r\nOS: " + Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")";
        body += "\r\nPhone: " + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL;

        Utils.openMail(this, getString(R.string.app_email), String.format("%s for Android", getString(R.string.app_name)), body);
    }

    private void rate()
    {
        Utils.openUrl(this, "market://details?id=" + getPackageName());
    }

    private void more()
    {
        Utils.openUrl(this, "market://search?q=pub:Currency");
    }

    private void pay()
    {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if(isAvailable)
        {
            boolean isOneTimePurchaseSupported = biller.isOneTimePurchaseSupported();
            if(isOneTimePurchaseSupported)
            {
                biller.purchase(this, productID);
                return;
            }
        }

        if(!Utils.getPurchased(this))
        {
            Utils.toast(this, "In-app purchase not available!");
        }
    }

    @Override
    public void onBillingInitialized()
    {
        /*if(biller.loadOwnedPurchasesFromGoogle())
        {
            restore();
        }*/
    }



    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error)
    {
        /*
        if(errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) return;

        String msg = "An error occurred!";
        if(error != null) msg = error.getMessage();

        Utils.alert(this, "Error", msg);*/
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        boolean purchased = biller.isPurchased(productID);
        Utils.setPurchased(this, purchased);

        setupAds(purchased);
    }

    @Override
    public void onPurchaseHistoryRestored()
    {
        restore();
    }


    private void restore()
    {
        boolean purchased = biller.isPurchased(productID);
        Utils.setPurchased(this, purchased);

        setupAds(purchased);
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

    @Override
    public void onKeypadBack()
    {
        int count = input.length();
        if (count > 0)
        {
            input = input.substring(0, count - 1);
            show();
        }
    }

    @Override
    public void onKeypadKey(String key)
    {
        if (key.equals(".") && input.contains(key)) return;

        input += key;

        show();
    }

    private void show()
    {
        App.value = Utils.getFloat(input);
        updateData();
    }

    private void updateData()
    {
        SpannableString content = new SpannableString(App.slotFr.symbol + input);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textValueFrom.setText(content);

        textValueTo.setText(App.slotTo.symbol + App.slotTo.value(this));


    }

    private void updateStatics()
    {
        textCodeFrom.setText(App.slotFr.code);
        textCodeTo.setText(App.slotTo.code);

        App.picasso.load(App.slotFr.flag).fit().into(imageFlagFrom);
        App.picasso.load(App.slotTo.flag).fit().into(imageFlagTo);
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

        updateStatics();
    }

    @Override
    public void onLoaderErrored(String error)
    {
        refreshUI(false);

        Utils.toast(this, error);
    }
}
