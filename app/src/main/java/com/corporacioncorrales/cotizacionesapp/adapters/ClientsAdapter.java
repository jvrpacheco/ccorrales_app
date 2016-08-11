package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.model.Client;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 8/10/16.
 */
public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder> {

    private Context mContext;
    private ArrayList<ClientsResponse> clientsList;


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
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_card_row_item, parent, false);

        return new ClientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        ClientsResponse client = clientsList.get(position);

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

    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }


}
