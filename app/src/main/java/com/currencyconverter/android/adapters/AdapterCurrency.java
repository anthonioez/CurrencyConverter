package com.currencyconverter.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.currencyconverter.android.App;
import com.currencyconverterdf.android.R;
import com.currencyconverter.android.objects.CurrencyItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterCurrency extends RecyclerView.Adapter<AdapterCurrency.ViewHolder> implements Filterable
{
    private Context mContext;
    private String search = "";
    private AdapterCurrencyListener mListener;

    private List<CurrencyItem> filts;
    private List<CurrencyItem> items;
    private CustomFilter filter;

    public AdapterCurrency(Context context, List<CurrencyItem> list, AdapterCurrencyListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
        items = new ArrayList<>(list);
        filts = new ArrayList<>(list);
    }

    public void update()
    {
        if(search.length() > 0)
            getFilter().filter(search);
        else
            notifyDataSetChanged();
    }

    public void search(String chars)
    {
        search = chars;
        getFilter().filter(chars);
    }


    @Override
    public Filter getFilter()
    {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.item = items.get(position);

        App.picasso.load(holder.item.flag).fit().into(holder.imageFlag);

        holder.textCode.setText(holder.item.code);
        holder.textName.setText(holder.item.name);

        holder.buttonFav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (null != mListener)
                {
                    mListener.onFavoriteSelect(holder.item);
                }
            }
        });

        holder.viewHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    mListener.onCurrencySelect(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CurrencyItem item;
        public final View viewHolder;
        public final TextView textCode;
        public final TextView textName;
        public final ImageButton buttonFav;
        public final ImageView imageFlag;

        public ViewHolder(View view)
        {
            super(view);

            viewHolder = view;

            imageFlag = (ImageView) view.findViewById(R.id.imageFlag);
            textName = (TextView) view.findViewById(R.id.textName);
            textCode = (TextView) view.findViewById(R.id.textCode);
            buttonFav = (ImageButton) view.findViewById(R.id.buttonFav);
        }
    }

    public interface AdapterCurrencyListener
    {
        void onCurrencySelect(CurrencyItem item);
        void onFavoriteSelect(CurrencyItem item);
    }


    private class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {

                String needle = constraint.toString().toLowerCase();

                ArrayList<CurrencyItem> filterList = new ArrayList<CurrencyItem>();
                for (int i = 0; i < filts.size(); i++) {

                    String code = filts.get(i).code.toLowerCase();
                    String name = filts.get(i).name.toLowerCase();

                    if(     code.contains(needle) || needle.contains(code) ||
                            name.contains(needle) || needle.contains(name) )
                    {
                        filterList.add(filts.get(i));
                    }
                }

                result.count = filterList.size();

                result.values = filterList;

            } else {
                result.count = filts.size();

                result.values = filts;
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            items = (ArrayList<CurrencyItem>) results.values;
            notifyDataSetChanged();
        }
    }

}
