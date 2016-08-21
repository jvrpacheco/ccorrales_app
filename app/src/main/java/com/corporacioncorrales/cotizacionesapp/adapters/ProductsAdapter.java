package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.content.res.Resources;
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

/**
 * Created by victor on 8/20/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private final Context mContext;
    private final ArrayList<ProductsResponse> productsList;

    public ProductsAdapter(Context mContext, ArrayList<ProductsResponse> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_item, parent, false);
        return new ProductsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
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
            //https://futurestud.io/blog/picasso-image-resizing-scaling-and-fit
            Picasso.with(mContext)
                    .load(R.drawable.package_96_gray)
                    .centerInside()
                    .fit()
                    .into(holder.ivProduct);
        }

        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToastMessage(mContext, "anadir producto a Cotizacion!");
                //holder.btnAddProduct.setText("Agregado");
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
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
