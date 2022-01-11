package com.currencyconverter.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.currencyconverter.android.App;
import com.currencyconverterdf.android.R;
import com.currencyconverter.android.objects.CurrencyItem;

public class AdapterFavorites extends RecyclerView.Adapter<AdapterFavorites.ViewHolder>
{
    private Context mContext;
    private AdapterFavoritesListener mListener;

    public AdapterFavorites(Context context, AdapterFavoritesListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_favs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.item = App.favorites.get(position);

        App.picasso.load(holder.item.flag).fit().into(holder.imageFlag);

        holder.textCode.setText(holder.item.code);
        holder.textName.setText(holder.item.name);

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
        return App.favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CurrencyItem item;
        public final View viewHolder;
        public final TextView textCode;
        public final TextView textName;
        public final ImageView imageFlag;

        public ViewHolder(View view)
        {
            super(view);

            viewHolder = view;

            imageFlag = (ImageView) view.findViewById(R.id.imageFlag);
            textName = (TextView) view.findViewById(R.id.textName);
            textCode = (TextView) view.findViewById(R.id.textCode);
        }
    }

    public interface AdapterFavoritesListener
    {
        void onCurrencySelect(CurrencyItem item);
    }
}
