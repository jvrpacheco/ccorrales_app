package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;

import java.util.ArrayList;

/**
 * Created by victor on 13/09/16.
 */
public class VirtualStockAdapter extends RecyclerView.Adapter<VirtualStockAdapter.ProductsViewHolder> {

    ArrayList<VirtualStockResponse> virtualStockList;
    Context mContext;

    public VirtualStockAdapter(Context mContext, ArrayList<VirtualStockResponse> virtualStockList) {
        this.mContext = mContext;
        this.virtualStockList = virtualStockList;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.virtual_stock_row_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        final VirtualStockResponse virtualStock = virtualStockList.get(position);

        holder.tvCodigo.setText(virtualStock.getCodigo());
        holder.tvFecha.setText(virtualStock.getFecha());

        /*if(price.getFecha().contains("T")) {
            String [] datetime = price.getFecha().split("T");

            holder.tvDatetimeHistory.setText(
                    //String.format("%s\n%s", datetime[0], datetime[1])
                    datetime[0]
            );
        }*/

        holder.tvCantidad.setText(virtualStock.getCantidad());
    }

    @Override
    public int getItemCount() {
        return virtualStockList.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvCodigo, tvFecha, tvCantidad;

        public ProductsViewHolder(View view) {
            super(view);
            tvCodigo = (TextView)view.findViewById(R.id.tvCodigo);
            tvFecha = (TextView)view.findViewById(R.id.tvFecha);
            tvCantidad = (TextView)view.findViewById(R.id.tvCantidad);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
