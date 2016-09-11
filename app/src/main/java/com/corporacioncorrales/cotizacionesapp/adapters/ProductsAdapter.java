package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by victor on 8/20/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    ArrayList<ProductsResponse> productsList;
    ArrayList<ProductsResponse> productsSelectedList;
    QuotationAdapter quotationAdapter;
    Context mContext;

    public ProductsAdapter(Context mContext, ArrayList<ProductsResponse> productsList, QuotationAdapter quotationAdapter) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.quotationAdapter = quotationAdapter;
        productsSelectedList = new ArrayList<>();
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        final ProductsResponse product = productsList.get(position);

        holder.tvId.setText(product.getId());
        holder.tvCantidad.setText("Stock: " + product.getCantidad());

        if(!product.getFoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(product.getFoto())
                    .placeholder(R.drawable.package_96_gray)
                    .error(R.drawable.package_96_gray)
                    .centerInside()
                    .fit()
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.drawable.package_96_gray);
        }

        if(product.getSelected()) {
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(product.getSelected()) {
                    product.setSelected(false);
                    holder.ivCheck.setVisibility(View.GONE);
                    productsSelectedList.remove(product);
                    quotationAdapter.removeItem(product);
                } else {
                    product.setSelected(true);
                    holder.ivCheck.setVisibility(View.VISIBLE);
                    productsSelectedList.add(product);
                    quotationAdapter.addItem(0, product);
                }

            Log.d(Constants.log_arrow, String.valueOf(productsSelectedList.size()));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Common.showToastMessage(mContext, "Imagen con zoom");
                return true; // si esta en false luego de long click ejecutara onclick
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvCantidad;
        ImageView ivProduct, ivCheck;
        Context ctx;

        public ProductsViewHolder(View view) {
            super(view);
            ivProduct = (ImageView)view.findViewById(R.id.iv_Product);
            tvId = (TextView)view.findViewById(R.id.tv_id);
            tvCantidad = (TextView)view.findViewById(R.id.tv_cantidad);
            ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void refreshItem(ProductsResponse product, Boolean willCheck) {

        int index = -1;
        if(productsList.contains(product)) {
            index = productsList.indexOf(product);

            if(index != -1) {
                //notifyItemChanged(index, product); //when remove from right the last element, this dissapear from left list
                notifyDataSetChanged();
            }
        }
    }

}
