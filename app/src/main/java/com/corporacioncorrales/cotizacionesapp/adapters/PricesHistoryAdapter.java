package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by victor on 13/09/16.
 */
public class PricesHistoryAdapter extends RecyclerView.Adapter<PricesHistoryAdapter.ProductsViewHolder> {

    ArrayList<PricesHistoryResponse> pricesHistoryList;
    Context mContext;

    public PricesHistoryAdapter(Context mContext, ArrayList<PricesHistoryResponse> pricesHistoryList) {
        this.mContext = mContext;
        this.pricesHistoryList = pricesHistoryList;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prices_history_row_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        final PricesHistoryResponse price = pricesHistoryList.get(position);
        holder.tvPriceHistory.setText(price.getPrecio());

        if(price.getFecha().contains("T")) {
            String [] datetime = price.getFecha().split("T");

            holder.tvDatetimeHistory.setText(
                    String.format("%s\n%s", datetime[0], datetime[1])
            );
        }
    }

    @Override
    public int getItemCount() {
        return pricesHistoryList.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvPriceHistory, tvDatetimeHistory;

        public ProductsViewHolder(View view) {
            super(view);
            tvPriceHistory = (TextView)view.findViewById(R.id.tvPriceHistory);
            tvDatetimeHistory = (TextView)view.findViewById(R.id.tvDatetimeHistory);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
