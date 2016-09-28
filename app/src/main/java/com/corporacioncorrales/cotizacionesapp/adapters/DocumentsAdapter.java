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
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by victor on 8/10/16.
 */
public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ClientsViewHolder> {

    private Context mContext;
    private ArrayList<DocumentsResponse> documentsList;
    private AdapterView.OnItemClickListener listener;
    private FragmentActivity myContext;

    public class ClientsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvId, tvFecha, tvMoneda, tvTotal, tvRubro;

        public ClientsViewHolder(View view) {
            super(view);
            tvId = (TextView)view.findViewById(R.id.tvId);
            tvFecha = (TextView)view.findViewById(R.id.tvFecha);
            tvMoneda = (TextView)view.findViewById(R.id.tvMoneda);
            tvTotal = (TextView)view.findViewById(R.id.tvTotal);
            tvRubro = (TextView)view.findViewById(R.id.tvRubro);
        }
    }

    public DocumentsAdapter(Context mContext, ArrayList<DocumentsResponse> documentsList) {
        //this.mContext = mContext;
        this.documentsList = documentsList;
        this.myContext = (FragmentActivity) mContext;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.documents_row_item, parent, false);

        return new ClientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        final DocumentsResponse document = documentsList.get(position);

        holder.tvId.setText(String.valueOf(document.getId()));

        if(document.getFecha().contains("T")) {
            String [] datetime = document.getFecha().split("T");
            holder.tvFecha.setText(
                    //String.format("%s\n%s", datetime[0], datetime[1])
                    datetime[0]
            );
        }

        holder.tvMoneda.setText(document.getMoneda());
        holder.tvTotal.setText(String.valueOf(document.getTotal()));
        holder.tvRubro.setText(document.getRubro().trim());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToastMessage(myContext, "Ir al documento con Id. " + document.getId());

                //para mostrar los productos necesitamos los clientes
                loadProductsOfDocument(myContext, getTestClient());
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    private void loadProductsOfDocument(FragmentActivity mContext, ClientsResponse client) {
        ProductsFragment pf = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cliente_id", client.getId());
        bundle.putString("cliente_razonSocial", client.getRazon_Social());
        bundle.putString("cliente_lineaDeCredito", client.getLinea());
        pf.setArguments(bundle);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, pf);
        ft.addToBackStack(Constants.fragmentTagProductos);
        ft.commit();
    }

    private ClientsResponse getTestClient() {
        ClientsResponse client = new ClientsResponse();
        client.setId("115257");
        client.setRazon_Social("APOLINARIO SALVA MARGARITA");
        client.setLinea("10000");
        return client;
    }

}
