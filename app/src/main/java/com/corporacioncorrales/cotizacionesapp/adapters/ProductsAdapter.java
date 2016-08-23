package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 8/20/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    ArrayList<ProductsResponse> productsList;
    Context mContext;

    public ProductsAdapter(Context mContext, ArrayList<ProductsResponse> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, int position) {
        final ProductsResponse product = productsList.get(position);
        holder.tvId.setText("Id: " + product.getId());
        holder.tvNombre.setText(product.getNombre());
        holder.tvPrecio.setText("Precio: " + product.getPrecio());
        holder.tvCantidad.setText("Cantidad: " + product.getCantidad());

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

        // Enviar a la lista de productos anadidos a la Cotizacion
        holder.chbAddProduct.setOnCheckedChangeListener(null);
        holder.chbAddProduct.setChecked(product.getSelected());
        holder.chbAddProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                product.setSelected(isChecked);
                holder.chbAddProduct.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvNombre, tvPrecio, tvCantidad;
        ImageView ivProduct;
        CheckBox chbAddProduct;
        Context ctx;

        public ProductsViewHolder(View view) {
            super(view);
            ivProduct = (ImageView)view.findViewById(R.id.iv_Product);
            tvId = (TextView)view.findViewById(R.id.tv_id);
            tvNombre = (TextView)view.findViewById(R.id.tv_nombre);
            tvPrecio = (TextView)view.findViewById(R.id.tv_precio);
            tvCantidad = (TextView)view.findViewById(R.id.tv_cantidad);

            chbAddProduct = (CheckBox) view.findViewById(R.id.checkBox);
            chbAddProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
