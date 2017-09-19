package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.WarehouseResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 9/15/17.
 */
public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder> {

    ArrayList<WarehouseResponse> warehousesList;
    Context mContext;
    ProductsResponse mProduct;

    public WarehouseAdapter(Context mContext, ArrayList<WarehouseResponse> warehousesList, ProductsResponse product) {
        this.mContext = mContext;
        this.warehousesList = warehousesList;
        this.mProduct = product;
    }

    @Override
    public WarehouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_selection_row_item, parent, false);
        WarehouseViewHolder warehousesViewHolder = new WarehouseViewHolder(view);
        return warehousesViewHolder;
    }

    @Override
    public void onBindViewHolder(final WarehouseViewHolder holder, final int position) {
        final WarehouseResponse warehouse = warehousesList.get(position);

        holder.tvCodigo.setText(warehouse.getCodigo());
        holder.tvAlmacen.setText(warehouse.getDescripcion());
        holder.tvStock.setText(warehouse.getStock());

        if (Integer.valueOf(warehouse.getStock()) <= 0) {
            holder.ivSelect.setImageResource(R.drawable.disabled_checkbox_64);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (warehouse.isSelected()) {
                        holder.ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                        warehouse.setSelected(false);
                        holder.llHeader.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        holder.ivSelect.setImageResource(R.drawable.checked_checkbox_50);
                        holder.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.turquesa_claro));
                        warehouse.setSelected(true);
                    }

                    /*for(WarehouseResponse otherWarehouse : warehousesList) {
                        if(otherWarehouse == warehouse) {
                            if(warehouse.isSelected()) {
                                holder.ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                                warehouse.setSelected(false);
                                holder.llHeader.setBackgroundColor(Color.TRANSPARENT);
                            } else {
                                holder.ivSelect.setImageResource(R.drawable.checked_checkbox_50);
                                warehouse.setSelected(true);
                                holder.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.turquesa_claro));
                            }
                        } else {
                            holder.ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                            otherWarehouse.setSelected(false);
                            holder.llHeader.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }*/

                    //setWarehousesSelection(warehouse, holder.ivSelect, holder.llHeader);
                }
            });
        }
    }

    private void setWarehousesSelection(WarehouseResponse warehouseSelected, ImageView ivSelect, LinearLayout row) {
        for(WarehouseResponse otherWarehouse : warehousesList) {
            if(otherWarehouse == warehouseSelected) {

                if(warehouseSelected.isSelected()) {
                    ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                    warehouseSelected.setSelected(false);
                    row.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    ivSelect.setImageResource(R.drawable.checked_checkbox_50);
                    warehouseSelected.setSelected(true);
                    row.setBackgroundColor(mContext.getResources().getColor(R.color.turquesa_claro));
                }

            } else {
                ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                otherWarehouse.setSelected(false);
                row.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return warehousesList.size();
    }

    public static class WarehouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_header)
        LinearLayout llHeader;
        @BindView(R.id.ivSelect)
        ImageView ivSelect;
        @BindView(R.id.tvAlmacen)
        TextView tvAlmacen;
        @BindView(R.id.tvStock)
        TextView tvStock;
        @BindView(R.id.tvCodigo)
        TextView tvCodigo;

        public WarehouseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
