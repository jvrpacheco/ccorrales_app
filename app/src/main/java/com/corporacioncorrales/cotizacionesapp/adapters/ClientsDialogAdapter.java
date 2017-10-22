package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by victor on 8/10/16.
 */
public class ClientsDialogAdapter extends RecyclerView.Adapter<ClientsDialogAdapter.ClientsViewHolder> {

    private Context mContext;
    private ArrayList<ClientsResponse> clientsList;
    private AdapterView.OnItemClickListener listener;
    private FragmentActivity myContext;
    private TextView tvSelectedClient;
    private ClientsResponse clientSelected = null;
    private Button btnAccept;
    private CheckBox chkTodos;


    public ClientsDialogAdapter(Context mContext, ArrayList<ClientsResponse> clientsList, TextView tvSelectedClient, Button btnAccept, CheckBox chkTodos) {
        this.mContext = mContext;
        this.clientsList = clientsList;
        this.tvSelectedClient = tvSelectedClient;
        this.btnAccept = btnAccept;
        this.chkTodos = chkTodos;
        myContext=(FragmentActivity) mContext;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_historial_clientes_row_item, parent, false);
        return new ClientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        final ClientsResponse client = clientsList.get(position);
        holder.tvClient.setText(client.getRazon_Social());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSelected = client;
                tvSelectedClient.setText(client.getRazon_Social());
                btnAccept.setEnabled(true);
                chkTodos.setChecked(false);
            }
        });
    }

    public class ClientsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvClient;

        public ClientsViewHolder(View view) {
            super(view);
            tvClient = (TextView)view.findViewById(R.id.tvDialogClient);
        }
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    public ClientsResponse getClientSelected() {
        ClientsResponse client = null;

        if(clientSelected!=null) {
            client = clientSelected;
        }

        return client;
    }
}
