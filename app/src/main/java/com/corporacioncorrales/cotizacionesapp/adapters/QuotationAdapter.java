package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;

import java.util.ArrayList;

/**
 * Created by victor on 8/20/16.
 */
public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.QuotationViewHolder> {

    Context mContext;
    ArrayList<ProductsResponse> productsList;

    public QuotationAdapter(Context mContext, ArrayList<ProductsResponse> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    @Override
    public QuotationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_row_item, parent, false);
        QuotationViewHolder productsViewHolder = new QuotationViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final QuotationViewHolder holder, final int position) {
        final ProductsResponse product = productsList.get(position);
        holder.tvId.setText(product.getId());
        holder.tvDescription.setText(product.getNombre());
        holder.tvPrice.setText(product.getPrecio());
        holder.tvQuantity.setText(product.getCantidad());

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product.setSelected(false);
                removeItem(product);
                ProductsFragment.productsAdapter.refreshItem(product, false);  // update the left list!
            }
        });

        holder.ivArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class QuotationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvDescription, tvPrice, tvQuantity;
        ImageView ivRemove, ivArrival;

        public QuotationViewHolder(View view) {
            super(view);
            tvId = (TextView)view.findViewById(R.id.tvId);
            tvDescription = (TextView)view.findViewById(R.id.tvDescription);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvQuantity = (TextView)view.findViewById(R.id.tvQuantity);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            ivArrival = (ImageView) view.findViewById(R.id.ivArrival);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeItem(ProductsResponse product) {
        int currPosition = productsList.indexOf(product);
        productsList.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    public void addItem(int position, ProductsResponse product) {
        productsList.add(position, product);
        notifyItemInserted(position);
    }
}