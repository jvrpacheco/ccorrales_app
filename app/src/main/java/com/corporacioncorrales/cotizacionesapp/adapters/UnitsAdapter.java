package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.UnitsResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;

import java.util.ArrayList;

/**
 * Created by victor on 18/11/16.
 */
public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.UnitsViewHolder> {

    ArrayList<UnitsResponse> unitsArrayList;
    Context mContext;
    TextView tvSelectedUnit;
    private UnitsResponse unitSelected = null;

    public UnitsAdapter(Context mContext, ArrayList<UnitsResponse> unitsArrayList, TextView tvSelectedUnit) {
        this.mContext = mContext;
        this.unitsArrayList = unitsArrayList;
        this.tvSelectedUnit = tvSelectedUnit;
    }

    @Override
    public UnitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_row_item, parent, false);
        UnitsViewHolder unitsViewHolder = new UnitsViewHolder(view);
        return unitsViewHolder;
    }

    @Override
    public void onBindViewHolder(final UnitsViewHolder holder, final int position) {
        final UnitsResponse unit = unitsArrayList.get(position);
        holder.tvCodUnidad.setText(unit.getUnidad());
        holder.tvPresentacionUnidad.setText(unit.getPresentacion());
        holder.tvStockUnidad.setText(unit.getStock());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitSelected = unit;
                tvSelectedUnit.setText(String.format("Codigo: %s, Presentacion: %s, Stock: %s", unit.getUnidad(), unit.getPresentacion(), unit.getStock()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return unitsArrayList.size();
    }

    public static class UnitsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvCodUnidad, tvPresentacionUnidad, tvStockUnidad;

        public UnitsViewHolder(View view) {
            super(view);
            tvCodUnidad = (TextView)view.findViewById(R.id.tvCodUnidad);
            tvPresentacionUnidad = (TextView)view.findViewById(R.id.tvPresentacionUnidad);
            tvStockUnidad = (TextView)view.findViewById(R.id.tvStockUnidad);

            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Log.d("TEST", "Item clicked at position " + getPosition());
        }
    }

    public UnitsResponse getUnitSelected() {
        UnitsResponse unit = null;

        if(unitSelected!=null) {
            unit = unitSelected;
        }

        return unit;
    }
}
