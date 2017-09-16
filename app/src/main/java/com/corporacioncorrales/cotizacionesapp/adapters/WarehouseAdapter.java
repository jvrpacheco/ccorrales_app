package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.WarehouseResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 9/15/17.
 */
public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder> {

    ArrayList<WarehouseResponse> warehousesList;
    Context mContext;

    public WarehouseAdapter(Context mContext, ArrayList<WarehouseResponse> warehousesList) {
        this.mContext = mContext;
        this.warehousesList = warehousesList;
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

        holder.tvAlmacen.setText(warehouse.getDescripcion());
        holder.tvStock.setText(warehouse.getStock());

        if(Integer.valueOf(warehouse.getStock())<=0) {
            holder.ivSelect.setImageResource(R.drawable.disabled_checkbox_64);
        } else {
            holder.ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(warehouse.isSelected()) {
                        holder.ivSelect.setImageResource(R.drawable.unchecked_checkbox_50);
                        warehouse.setSelected(false);
                        holder.etCantidad.clearFocus();
                        holder.etCantidad.setText("");
                    } else {
                        holder.ivSelect.setImageResource(R.drawable.checked_checkbox_50);
                        warehouse.setSelected(true);
                        holder.etCantidad.requestFocus();
                    }

                }
            });
        }

        holder.etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String quantityInserted = editable.toString();

                if(!quantityInserted.isEmpty()) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return warehousesList.size();
    }

    public static class WarehouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivSelect)
        ImageView ivSelect;
        @BindView(R.id.tvAlmacen)
        TextView tvAlmacen;
        @BindView(R.id.tvStock)
        TextView tvStock;
        @BindView(R.id.etCantidad)
        EditText etCantidad;

        public WarehouseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
