package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.activities.MainActivity;
import com.corporacioncorrales.cotizacionesapp.fragments.ClientsFragment;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.Client;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by victor on 8/10/16.
 */
public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder> {

    private Context mContext;
    private ArrayList<ClientsResponse> clientsList;
    private AdapterView.OnItemClickListener listener;

    public class ClientsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRazonSocial, tvRUC;
        private ImageView ivClient;

        public ClientsViewHolder(View view) {
            super(view);
            ivClient = (ImageView)view.findViewById(R.id.ivClient);
            tvRazonSocial = (TextView)view.findViewById(R.id.tv_razon_social);
            tvRUC = (TextView)view.findViewById(R.id.tv_RUC);
        }
    }

    public ClientsAdapter(Context mContext, ArrayList<ClientsResponse> clientsList) {
        this.mContext = mContext;
        this.clientsList = clientsList;

        myContext=(FragmentActivity) mContext;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_card_row_item, parent, false);

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(v, getItemId(parent.getId()));
            }
        });*/

        return new ClientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        final ClientsResponse client = clientsList.get(position);

        holder.tvRazonSocial.setText(client.getRazon_Social());
        holder.tvRUC.setText("RUC " + client.getRuc());

        if(!client.getFoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(client.getFoto())
                    .placeholder(R.drawable.client2)
                    .error(R.drawable.client2)
                    //.resize(120, 60)
                    .centerInside()
                    //.centerCrop()
                    .fit()
                    .into(holder.ivClient);
        } else {
            //holder.ivClient.setImageResource(R.drawable.client_100x100);
            Picasso.with(mContext)
                    .load(R.drawable.client2)
                    .centerInside()
                    .fit()
                    .into(holder.ivClient);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToastMessage(mContext, "Ir a articulos disponibles para " + client.getRazon_Social());
                goToFragment(myContext, client);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    private FragmentActivity myContext;

    private void goToFragment(FragmentActivity mContext, ClientsResponse client) {

        ProductsFragment pf = new ProductsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("cliente_id", client.getId());
        bundle.putString("cliente_razonSocial", client.getRazon_Social());
        pf.setArguments(bundle);

        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, pf);
        ft.addToBackStack("asds");
        ft.commit();
    }

}
