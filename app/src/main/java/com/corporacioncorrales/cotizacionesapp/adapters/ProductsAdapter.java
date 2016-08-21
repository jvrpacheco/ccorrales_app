package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private final Context mContext;
    private final ArrayList<ProductsResponse> productsList;
    private ArrayList<Integer> addedList;

    public ProductsAdapter(Context mContext, ArrayList<ProductsResponse> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;

        addedList = new ArrayList<Integer>();
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_item, parent, false);
        return new ProductsViewHolder(itemView);
    }

    /*
    * http://stackoverflow.com/questions/34570041/recyclerview-change-item-layout-on-click
    * I recommend you to store the expanded state of item not in ViewHolder but in Adapter. Like making a map or arrayList
    * (referencing your model Comanda or position, respectively), because viewHolder would be reused when you scroll
    * recyclerview and so the item would be expanded not only the one you clicked on, but also the one that was reused :) – aelimill Jan 2 at 21:04
    * */
    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {
        final ProductsResponse product = productsList.get(position);

        holder.tvId.setText("Id: " + product.getId());
        holder.tvPrecio.setText("Precio: " + product.getPrecio());
        holder.tvNombre.setText(product.getNombre());
        holder.tvPrecio.setText("Precio: " + product.getPrecio());

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

        //CADA VEZ QUE SE HACE SCROLL ENTRA A onBindViewHolder, VERIFICAMOS SI YA FUE ANADIDO ANTERIORMENTE
        if(addedList.contains(position)) {
            //Si ya fue anadido
            holder.btnAddProduct.setText("AGREGADO");
            holder.btnAddProduct.setBackgroundColor(ContextCompat.getColor(mContext, R.color.naranja));
        } else {
            holder.btnAddProduct.setText("AGREGAR");
            holder.btnAddProduct.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gris_disable));
        }

        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Common.showToastMessage(mContext, "anadir producto a Cotizacion!, anadidos hasta el momento: " + (addedList.size()+1));
                holder.btnAddProduct.setText("AGREGADO");
                holder.btnAddProduct.setBackgroundColor(ContextCompat.getColor(mContext, R.color.naranja));
                addedList.add(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvId, tvNombre, tvPrecio, tvCantidad;
        private ImageView ivProduct;
        private Button btnAddProduct;

        public ProductsViewHolder(View view) {
            super(view);
            ivProduct = (ImageView)view.findViewById(R.id.iv_Product);
            tvId = (TextView)view.findViewById(R.id.tv_id);
            tvNombre = (TextView)view.findViewById(R.id.tv_nombre);
            tvPrecio = (TextView)view.findViewById(R.id.tv_precio);
            tvCantidad = (TextView)view.findViewById(R.id.tv_cantidad);
            btnAddProduct = (Button)view.findViewById(R.id.btnAddProduct);
        }
    }
}
