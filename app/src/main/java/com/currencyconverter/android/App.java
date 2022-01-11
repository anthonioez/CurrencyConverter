package com.currencyconverter.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.currencyconverterdf.android.R;
import com.currencyconverter.android.objects.CurrencyItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class App extends Application
{
    public static final String TAG = App.class.getSimpleName();
    public static String API_KEY = "";

    public static List<CurrencyItem> favorites = new ArrayList<>();
    public static List<CurrencyItem> currencies = new ArrayList<>();
    public static CurrencyItem slotFr = new CurrencyItem();
    public static CurrencyItem slotTo = new CurrencyItem();

    public static float value = 0;
    public static String appName;

    public static Picasso picasso;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "Starting...");

        appName = getResources().getString(R.string.app_name);

        picasso = Picasso.get();

        loadCurrencies(this);
        loadFavorites(this);
        loadSlots(this);

        Prefs.setRate(this, "USD", 1);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();

        Log.d(TAG, "Terminating...");
    }

    public static String getAppName()
    {
        return appName;
    }

    // Returns the exchange rate to `target` currency from `base` currency
    public static float rate(Context context, String from, String to)
    {
        String root = Prefs.getBase(context);
        float toRate = Prefs.getRate(context, to, 0);
        float fromRate = Prefs.getRate(context, from, 0);

        if (to == root)
        {
            if (fromRate == 0)
                return 0;
            else
                return 1 / fromRate;
        }

        if (fromRate == 0)
            return 0;
        else
            return toRate * (1 / fromRate);
    }

    public static CurrencyItem find(String code)
    {
        for(CurrencyItem item : App.currencies)
        {
            if (item.code.equals(code))
            {
                return item;
            }
        }

        return null;    // new Currency("", "", "");
    }

    public static void loadSlots(Context context)
    {
        CurrencyItem currency;

        currency = find(Prefs.getFrom(context));
        if (currency != null)
        {
            App.slotFr = currency;
        }

        currency = find(Prefs.getTo(context));
        if (currency != null)
        {
            App.slotTo = currency;
        }
    }

    public static void saveSlots(Context context)
    {
        Prefs.setFrom(context, App.slotFr.code);
        Prefs.setTo(context, App.slotTo.code);
    }

    public static void loadFavorites(Context context)
    {
        App.favorites.clear();

        String favs = Prefs.getFavs(context);

        String[] list = favs.split(",");
        for (String name : list)
        {
            CurrencyItem currency = find(name);
            if (currency == null) continue;

            App.favorites.add(currency);
        }
    }

    public static void saveFavorites(Context context)
    {
        String fav = "";

        for(CurrencyItem currency : App.favorites)
        {
            fav = fav + (fav.length() == 0 ? "" : ",") + currency.code;
        }

        Prefs.setFavs(context, fav);
    }

    public static void loadCurrencies(Context context)
    {
        App.currencies.clear();

        App.currencies.add(new CurrencyItem(context, "د.إ",  "AED", "United Arab Emirates Dirham"));
        App.currencies.add(new CurrencyItem(context, "؋",    "AFN", "Afghan Afghani"));
        App.currencies.add(new CurrencyItem(context, "Lek",  "ALL", "Albanian Lek"));
        App.currencies.add(new CurrencyItem(context, "դր",   "AMD", "Armenian Dram"));
        App.currencies.add(new CurrencyItem(context, "NAƒ",  "ANG", "Netherlands Antillean Guilder"));
        App.currencies.add(new CurrencyItem(context, "Kz",   "AOA", "Angolan Kwanza"));
        App.currencies.add(new CurrencyItem(context, "$",    "ARS", "Argentine Peso"));
        App.currencies.add(new CurrencyItem(context, "$",    "AUD", "Australian Dollar"));
        App.currencies.add(new CurrencyItem(context, "ƒ",    "AWG", "Aruban Florin"));
        App.currencies.add(new CurrencyItem(context, "ман",  "AZN", "Azerbaijani Manat"));
        App.currencies.add(new CurrencyItem(context, "KM",   "BAM", "Bosnia-Herzegovina Convertible Mark"));
        App.currencies.add(new CurrencyItem(context, "$",    "BBD", "Barbadian Dollar"));
        App.currencies.add(new CurrencyItem(context, "Tk",   "BDT", "Bangladeshi Taka"));
        App.currencies.add(new CurrencyItem(context, "лв",   "BGN", "Bulgarian Lev"));
        App.currencies.add(new CurrencyItem(context, ".د.ب", "BHD", "Bahraini Dinar"));
        App.currencies.add(new CurrencyItem(context, "FBu",  "BIF", "Burundian Franc"));
        App.currencies.add(new CurrencyItem(context, "$",    "BMD", "Bermudan Dollar"));
        App.currencies.add(new CurrencyItem(context, "$",    "BND", "Brunei Dollar"));
        App.currencies.add(new CurrencyItem(context, "$b",   "BOB", "Bolivian Boliviano"));
        App.currencies.add(new CurrencyItem(context, "R$",   "BRL", "Brazilian Real"));
        App.currencies.add(new CurrencyItem(context, "$",    "BSD", "Bahamian Dollar"));
        //App.currencies.add(new CurrencyItem(context, "", "BTC", "Bitcoin"));
        App.currencies.add(new CurrencyItem(context, "Nu.",  "BTN", "Bhutanese Ngultrum"));
        App.currencies.add(new CurrencyItem(context, "P",    "BWP", "Botswanan Pula"));
        App.currencies.add(new CurrencyItem(context, "p.",   "BYN", "Belarusian Ruble"));
        App.currencies.add(new CurrencyItem(context, "p.",   "BYR", "Belarusian Ruble (pre-2016)"));
        App.currencies.add(new CurrencyItem(context, "BZ$",  "BZD", "Belize Dollar"));
        App.currencies.add(new CurrencyItem(context, "$",    "CAD", "Canadian Dollar"));
        App.currencies.add(new CurrencyItem(context, "CDFr", "CDF", "Congolese Franc"));
        App.currencies.add(new CurrencyItem(context, "CHF",  "CHF", "Swiss Franc"));
        //App.currencies.add(new CurrencyItem(context, "", "CLF", "Chilean Unit of Account (UF)"));
        App.currencies.add(new CurrencyItem(context, "$",    "CLP", "Chilean Peso"));
        App.currencies.add(new CurrencyItem(context, "¥",    "CNY", "Chinese Yuan"));
        App.currencies.add(new CurrencyItem(context, "$",    "COP", "Colombian Peso"));
        App.currencies.add(new CurrencyItem(context, "₡",    "CRC", "Costa Rican Colón"));
        App.currencies.add(new CurrencyItem(context, "₱",    "CUC", "Cuban Convertible Peso"));
        App.currencies.add(new CurrencyItem(context, "₱",    "CUP", "Cuban Peso"));
        App.currencies.add(new CurrencyItem(context, "Esc",  "CVE", "Cape Verdean Escudo"));
        App.currencies.add(new CurrencyItem(context, "Kč",   "CZK", "Czech Republic Koruna"));
        App.currencies.add(new CurrencyItem(context, "Fdj",  "DJF", "Djiboutian Franc"));
        App.currencies.add(new CurrencyItem(context, "kr",   "DKK", "Danish Krone"));
        App.currencies.add(new CurrencyItem(context, "RD$",  "DOP", "Dominican Peso"));
        App.currencies.add(new CurrencyItem(context, "دج",   "DZD", "Algerian Dinar"));
        App.currencies.add(new CurrencyItem(context, "EEK",  "EEK", "Estonian Kroon"));
        App.currencies.add(new CurrencyItem(context, "£",    "EGP", "Egyptian Pound"));
        App.currencies.add(new CurrencyItem(context, "Nfk",  "ERN", "Eritrean Nakfa"));
        App.currencies.add(new CurrencyItem(context, "Br",   "ETB", "Ethiopian Birr"));
        App.currencies.add(new CurrencyItem(context, "€",    "EUR", "Euro"));
        App.currencies.add(new CurrencyItem(context, "$",    "FJD", "Fijian Dollar"));
        App.currencies.add(new CurrencyItem(context, "£",    "FKP", "Falkland Islands Pound"));
        App.currencies.add(new CurrencyItem(context, "£",    "GBP", "British Pound Sterling"));
        App.currencies.add(new CurrencyItem(context, "₾",    "GEL", "Georgian Lari"));
        App.currencies.add(new CurrencyItem(context, "£",    "GGP", "Guernsey Pound"));
        App.currencies.add(new CurrencyItem(context, "¢",    "GHS", "Ghanaian Cedi"));
        App.currencies.add(new CurrencyItem(context, "£",    "GIP", "Gibraltar Pound"));
        App.currencies.add(new CurrencyItem(context, "D",    "GMD", "Gambian Dalasi"));
        App.currencies.add(new CurrencyItem(context, "FG",   "GNF", "Guinean Franc"));
        App.currencies.add(new CurrencyItem(context, "Q",    "GTQ", "Guatemalan Quetzal"));
        App.currencies.add(new CurrencyItem(context, "$",    "GYD", "Guyanaese Dollar"));
        App.currencies.add(new CurrencyItem(context, "$",    "HKD", "Hong Kong Dollar"));
        App.currencies.add(new CurrencyItem(context, "L",    "HNL", "Honduran Lempira"));
        App.currencies.add(new CurrencyItem(context, "kn",   "HRK", "Croatian Kuna"));
        App.currencies.add(new CurrencyItem(context, "G",    "HTG", "Haitian Gourde"));
        App.currencies.add(new CurrencyItem(context, "Ft",   "HUF", "Hungarian Forint"));
        App.currencies.add(new CurrencyItem(context, "Rp",   "IDR", "Indonesian Rupiah"));
        App.currencies.add(new CurrencyItem(context, "₪",    "ILS", "Israeli New Sheqel"));
        App.currencies.add(new CurrencyItem(context, "£",    "IMP", "Manx pound"));
        App.currencies.add(new CurrencyItem(context, "₹",    "INR", "Indian Rupee"));
        App.currencies.add(new CurrencyItem(context, "ع.د",  "IQD", "Iraqi Dinar"));
        App.currencies.add(new CurrencyItem(context, "﷼",    "IRR", "Iranian Rial"));
        App.currencies.add(new CurrencyItem(context, "kr",   "ISK", "Icelandic Króna"));
        App.currencies.add(new CurrencyItem(context, "£",    "JEP", "Jersey Pound"));
        App.currencies.add(new CurrencyItem(context, "J$",   "JMD", "Jamaican Dollar"));
        App.currencies.add(new CurrencyItem(context, "JD",   "JOD", "Jordanian Dinar"));
        App.currencies.add(new CurrencyItem(context, "¥",    "JPY", "Japanese Yen"));
        App.currencies.add(new CurrencyItem(context, "KSh",  "KES", "Kenyan Shilling"));
        App.currencies.add(new CurrencyItem(context, "лв",   "KGS", "Kyrgystani Som"));
        App.currencies.add(new CurrencyItem(context, "៛",    "KHR", "Cambodian Riel"));
        App.currencies.add(new CurrencyItem(context, "KMF",  "KMF", "Comorian Franc"));
        App.currencies.add(new CurrencyItem(context, "₩",    "KPW", "North Korean Won"));
        App.currencies.add(new CurrencyItem(context, "₩",    "KRW", "South Korean Won"));
        App.currencies.add(new CurrencyItem(context, "د.ك",  "KWD", "Kuwaiti Dinar"));
        App.currencies.add(new CurrencyItem(context, "$",    "KYD", "Cayman Islands Dollar"));
        App.currencies.add(new CurrencyItem(context, "KZT",  "KZT", "Kazakhstani Tenge"));
        App.currencies.add(new CurrencyItem(context, "₭",    "LAK", "Laotian Kip"));
        App.currencies.add(new CurrencyItem(context, "£",    "LBP", "Lebanese Pound"));
        App.currencies.add(new CurrencyItem(context, "₨",    "LKR", "Sri Lankan Rupee"));
        App.currencies.add(new CurrencyItem(context, "$",    "LRD", "Liberian Dollar"));
        App.currencies.add(new CurrencyItem(context, "L",    "LSL", "Lesotho Loti"));
        App.currencies.add(new CurrencyItem(context, "Lt",   "LTL", "Lithuanian Litas"));
        App.currencies.add(new CurrencyItem(context, "Ls",   "LVL", "Latvian Lats"));
        App.currencies.add(new CurrencyItem(context, "ل.د",  "LYD", "Libyan Dinar"));
        App.currencies.add(new CurrencyItem(context, "د.م",  "MAD", "Moroccan Dirham"));
        App.currencies.add(new CurrencyItem(context, "MDL",  "MDL", "Moldovan Leu"));
        App.currencies.add(new CurrencyItem(context, "MGA",  "MGA", "Malagasy Ariary"));
        App.currencies.add(new CurrencyItem(context, "ден",  "MKD", "Macedonian Denar"));
        App.currencies.add(new CurrencyItem(context, "K",    "MMK", "Myanma Kyat"));
        App.currencies.add(new CurrencyItem(context, "₮",    "MNT", "Mongolian Tugrik"));
        App.currencies.add(new CurrencyItem(context, "$",    "MOP", "Macanese Pataca"));
        App.currencies.add(new CurrencyItem(context, "UM",   "MRO", "Mauritanian Ouguiya"));
        App.currencies.add(new CurrencyItem(context, "Lm",   "MTL", "Maltese Lira"));
        App.currencies.add(new CurrencyItem(context, "₨",    "MUR", "Mauritian Rupee"));
        App.currencies.add(new CurrencyItem(context, "Rf",   "MVR", "Maldivian Rufiyaa"));
        App.currencies.add(new CurrencyItem(context, "MK",   "MWK", "Malawian Kwacha"));
        App.currencies.add(new CurrencyItem(context, "$",    "MXN", "Mexican Peso"));
        App.currencies.add(new CurrencyItem(context, "RM",   "MYR", "Malaysian Ringgit"));
        App.currencies.add(new CurrencyItem(context, "MT",   "MZN", "Mozambican Metical"));
        App.currencies.add(new CurrencyItem(context, "$",    "NAD", "Namibian Dollar"));
        App.currencies.add(new CurrencyItem(context, "₦",    "NGN", "Nigerian Naira"));
        App.currencies.add(new CurrencyItem(context, "C$",   "NIO", "Nicaraguan Córdoba"));
        App.currencies.add(new CurrencyItem(context, "kr",   "NOK", "Norwegian Krone"));
        App.currencies.add(new CurrencyItem(context, "₨",    "NPR", "Nepalese Rupee"));
        App.currencies.add(new CurrencyItem(context, "$",    "NZD", "New Zealand Dollar"));
        App.currencies.add(new CurrencyItem(context, "ر.ع.", "OMR", "Omani Rial"));
        App.currencies.add(new CurrencyItem(context, "B/.",  "PAB", "Panamanian Balboa"));
        App.currencies.add(new CurrencyItem(context, "S/.",  "PEN", "Peruvian Nuevo Sol"));
        App.currencies.add(new CurrencyItem(context, "K",    "PGK", "Papua New Guinean Kina"));
        App.currencies.add(new CurrencyItem(context, "₱",    "PHP", "Philippine Peso"));
        App.currencies.add(new CurrencyItem(context, "₨",    "PKR", "Pakistani Rupee"));
        App.currencies.add(new CurrencyItem(context, "zł",   "PLN", "Polish Zloty"));
        App.currencies.add(new CurrencyItem(context, "Gs",   "PYG", "Paraguayan Guarani"));
        App.currencies.add(new CurrencyItem(context, "﷼",    "QAR", "Qatari Rial"));
        App.currencies.add(new CurrencyItem(context, "lei",  "RON", "Romanian Leu"));
        App.currencies.add(new CurrencyItem(context, "Дин.", "RSD", "Serbian Dinar"));
        App.currencies.add(new CurrencyItem(context, "₽",    "RUB", "Russian Ruble"));
        App.currencies.add(new CurrencyItem(context, "RF",   "RWF", "Rwandan Franc"));
        App.currencies.add(new CurrencyItem(context, "﷼",    "SAR", "Saudi Riyal"));
        App.currencies.add(new CurrencyItem(context, "$",    "SBD", "Solomon Islands Dollar"));
        App.currencies.add(new CurrencyItem(context, "SR",   "SCR", "Seychellois Rupee"));
        App.currencies.add(new CurrencyItem(context, "£",    "SDG", "Sudanese Pound"));
        App.currencies.add(new CurrencyItem(context, "kr",   "SEK", "Swedish Krona"));
        App.currencies.add(new CurrencyItem(context, "$",    "SGD", "Singapore Dollar"));
        App.currencies.add(new CurrencyItem(context, "£",    "SHP", "Saint Helena Pound"));
        App.currencies.add(new CurrencyItem(context, "Le",   "SLL", "Sierra Leonean Leone"));
        App.currencies.add(new CurrencyItem(context, "S",    "SOS", "Somali Shilling"));
        App.currencies.add(new CurrencyItem(context, "$",    "SRD", "Surinamese Dollar"));
        App.currencies.add(new CurrencyItem(context, "Db",   "STD", "São Tomé and Príncipe Dobra"));
        App.currencies.add(new CurrencyItem(context, "₡",    "SVC", "Salvadoran Colón"));
        App.currencies.add(new CurrencyItem(context, "£",    "SYP", "Syrian Pound"));
        App.currencies.add(new CurrencyItem(context, "SZL",  "SZL", "Swazi Lilangeni"));
        App.currencies.add(new CurrencyItem(context, "฿",    "THB", "Thai Baht"));
        App.currencies.add(new CurrencyItem(context, "TJS",  "TJS", "Tajikistani Somoni"));
        App.currencies.add(new CurrencyItem(context, "T",    "TMT", "Turkmenistani Manat"));
        App.currencies.add(new CurrencyItem(context, "د.ت",  "TND", "Tunisian Dinar"));
        App.currencies.add(new CurrencyItem(context, "T$",   "TOP", "Tongan Paʻanga"));
        App.currencies.add(new CurrencyItem(context, "₺",    "TRY", "Turkish Lira"));
        App.currencies.add(new CurrencyItem(context, "TT$",  "TTD", "Trinidad and Tobago Dollar"));
        App.currencies.add(new CurrencyItem(context, "NT$",  "TWD", "New Taiwan Dollar"));
        App.currencies.add(new CurrencyItem(context, "x",    "TZS", "Tanzanian Shilling"));
        App.currencies.add(new CurrencyItem(context, "₴",    "UAH", "Ukrainian Hryvnia"));
        App.currencies.add(new CurrencyItem(context, "USh",  "UGX", "Ugandan Shilling"));
        App.currencies.add(new CurrencyItem(context, "$",    "USD", "United States Dollar"));
        App.currencies.add(new CurrencyItem(context, "$U",   "UYU", "Uruguayan Peso"));
        App.currencies.add(new CurrencyItem(context, "лв",   "UZS", "Uzbekistan Som"));
        App.currencies.add(new CurrencyItem(context, "Bs",   "VEF", "Venezuelan Bolívar Fuerte"));
        App.currencies.add(new CurrencyItem(context, "₫",    "VND", "Vietnamese Dong"));
        App.currencies.add(new CurrencyItem(context, "Vt",   "VUV", "Vanuatu Vatu"));
        App.currencies.add(new CurrencyItem(context, "WS$",  "WST", "Samoan Tala"));
        App.currencies.add(new CurrencyItem(context, "franc","XAF", "CFA Franc BEAC"));
        App.currencies.add(new CurrencyItem(context, "$",    "XCD", "East Caribbean Dollar"));
        App.currencies.add(new CurrencyItem(context, "franc","XOF", "CFA Franc BCEAO"));
        App.currencies.add(new CurrencyItem(context, "CFP",  "XPF", "CFP Franc"));
        App.currencies.add(new CurrencyItem(context, "﷼",    "YER", "Yemeni Rial"));
        App.currencies.add(new CurrencyItem(context, "R",    "ZAR", "South African Rand"));
        App.currencies.add(new CurrencyItem(context, "ZMK",  "ZMK", "Zambian Kwacha (pre-2013)"));
        App.currencies.add(new CurrencyItem(context, "ZMK",  "ZMW", "Zambian Kwacha"));
        App.currencies.add(new CurrencyItem(context, "Z$",   "ZWL", "Zimbabwean Dollar"));
    }

    public static void setFrom(Context context, CurrencyItem item)
    {
        if (App.slotFr.code.equals(item.code) || App.slotTo.code.equals(item.code))
        {
            Utils.alert(context, App.appName, "Currency already set!");
            return;
        }

        App.slotFr = item;
        App.saveSlots(context);
    }

    public static void setTo(Context context, CurrencyItem item)
    {
        if (App.slotTo.code.equals(item.code) || App.slotFr.code.equals(item.code))
        {
            Utils.alert(context, App.appName, "Currency already set!");
            return;
        }

        App.slotTo = item;
        App.saveSlots(context);
    }
}