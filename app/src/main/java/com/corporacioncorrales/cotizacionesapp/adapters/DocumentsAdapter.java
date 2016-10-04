package com.corporacioncorrales.cotizacionesapp.adapters;

import android.content.Context;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

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
        private TextView tvSiglaTipoDoc, tvSerie, tvNumero, tvFecha, tvRazonSocial, tvMoneda, tvTotal, tvRubro, tvEstado;

        public ClientsViewHolder(View view) {
            super(view);
            tvSiglaTipoDoc = (TextView)view.findViewById(R.id.tvSiglaTipoDoc);
            tvSerie = (TextView)view.findViewById(R.id.tvSerie);
            tvNumero = (TextView)view.findViewById(R.id.tvNumero);
            tvFecha = (TextView)view.findViewById(R.id.tvFecha);
            tvRazonSocial = (TextView)view.findViewById(R.id.tvRazonSocial);
            tvMoneda = (TextView)view.findViewById(R.id.tvMoneda);
            tvTotal = (TextView)view.findViewById(R.id.tvTotal);
            tvRubro = (TextView)view.findViewById(R.id.tvRubro);
            tvEstado = (TextView)view.findViewById(R.id.tvEstado);
        }
    }

    public DocumentsAdapter(Context mContext, ArrayList<DocumentsResponse> documentsList) {
        //this.mContext = mContext;
        this.documentsList = documentsList;
        this.myContext = (FragmentActivity) mContext;
        //mainProgressBar = myContext.getApplicationContext().getApplicationContext()
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

        //holder.tvSiglaTipoDoc.setText(document.getLabelSiglasTipoDocumento());
        holder.tvSiglaTipoDoc.setText(document.getIdTipoDocumento());
        holder.tvSerie.setText(document.getNroSerieDocumento());
        holder.tvNumero.setText(document.getNroDocumento());
        holder.tvFecha.setText(document.getFechaEmisionDocumento().trim());
        holder.tvRazonSocial.setText(document.getNombreCliente());
        holder.tvMoneda.setText(document.getMonedaDocumento());
        holder.tvTotal.setText(document.getMontoTotalDocumento());

        String idRubro = document.getIdRubroDocumento();
        if(idRubro.equals(Constants.rubro_vidrio)) {
            holder.tvRubro.setText(Constants.rubro_vidrio_label);
        } else if(idRubro.equals(Constants.rubro_aluminio)) {
            holder.tvRubro.setText(Constants.rubro_aluminio_label);
        } else if(idRubro.equals(Constants.rubro_accesorio)) {
            holder.tvRubro.setText(Constants.rubro_accesorio_label);
        } else if(idRubro.equals(Constants.rubro_plastico)) {
            holder.tvRubro.setText(Constants.rubro_plastico_label);
        }

        holder.tvEstado.setText(document.getEstadoDocumento());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToastMessage(myContext, "Ir al documento con Id. " + document.getIdDocumento());
                loadProductsOfDocument(myContext,
                        document.getIdCliente(),
                        document.getNombreCliente(),
                        "10500.00",
                        document.getIdRubroDocumento(),
                        document.getIdDocumento(),
                        document.getIdTipoDocumento());
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    private void loadProductsOfDocument(FragmentActivity mContext, String idCliente, String razonSocial, String saldoDisponible, String rubroSeleccionado, String idDocumento, String tipoDocumento) {
        ProductsFragment pf = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cliente_id", idCliente);
        bundle.putString("cliente_razonSocial", razonSocial);
        bundle.putString("cliente_saldoDisponible", saldoDisponible);
        bundle.putString("rubroSeleccionado", rubroSeleccionado);
        bundle.putString("idDocumento", idDocumento);
        bundle.putString("tipoDocumento", tipoDocumento); //test:"2"
        pf.setArguments(bundle);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, pf);
        ft.addToBackStack(Constants.fragmentTagProductos);
        ft.commit();
    }


}
