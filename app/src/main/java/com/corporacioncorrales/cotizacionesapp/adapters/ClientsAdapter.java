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
public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder> {

    private Context mContext;
    private ArrayList<ClientsResponse> clientsList;
    private AdapterView.OnItemClickListener listener;
    private FragmentActivity myContext;

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
                    .centerInside()
                    .fit()
                    .into(holder.ivClient);
        } else {
            holder.ivClient.setImageResource(R.drawable.client2);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isOnline(mContext)) {
                    Common.showToastMessageShort(mContext, String.format("%s %s", mContext.getResources().getString(R.string.ir_a_articulos), client.getRazon_Social()));
                    goToProductsFragment(myContext, client);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    private void goToProductsFragment(FragmentActivity mContext, ClientsResponse client) {
        Singleton.getInstance().setIdclientSelected(client.getId());
        ProductsFragment pf = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cliente_id", client.getId());
        bundle.putString("cliente_razonSocial", client.getRazon_Social());
        bundle.putString("cliente_saldoTotal", client.getLinea());
        bundle.putString("cliente_saldoDisponible", client.getLinea_disponible());
        bundle.putString("rubroSeleccionado", Singleton.getInstance().getRubroSelected());
        bundle.putString("maxdias", client.getMaxDias());
        pf.setArguments(bundle);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, pf);
        ft.addToBackStack(Constants.fragmentTagClientes);
        ft.commit();
    }

}
