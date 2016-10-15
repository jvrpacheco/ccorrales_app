package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.InstrumentationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.CorreoResponse;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.model.MensajeResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ClientsApi;
import com.corporacioncorrales.cotizacionesapp.networking.CorreoApi;
import com.corporacioncorrales.cotizacionesapp.networking.MensajeApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
        private ImageView ivSendDocument;

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
            ivSendDocument = (ImageView) view.findViewById(R.id.ivSendDocument);
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

        holder.tvSiglaTipoDoc.setText(document.getLabelSiglasTipoDocumento());
        //holder.tvSiglaTipoDoc.setText(document.getIdTipoDocumento());
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

        //holder.tvEstado.setText(document.getEstadoDocumento());
        holder.tvEstado.setText(document.getLabelEstadoDocumento());
        holder.ivSendDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Common.showToastMessage(myContext, "Enviando...");
                showDialogSendDocumentToCLient(myContext, document.getIdCliente(), document.getIdDocumento());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToastMessage(myContext, "Ir al documento con Id. " + document.getIdDocumento());
                loadProductsOfDocument(myContext,
                        document.getIdCliente(),
                        document.getNombreCliente(),
                        document.getLinea_disponible(),
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
        bundle.putString("tipoDocumento", tipoDocumento);
        pf.setArguments(bundle);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, pf);
        ft.addToBackStack(Constants.fragmentTagHistorial);
        ft.commit();
    }

    public void showDialogSendDocumentToCLient(final Context context, final String idCliente, final String idDocumento) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage("Por que medio desea enviar el documento seleccionado?");
        builder.setCancelable(false);

        builder.setPositiveButton("Correo electronico",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        showDialogSendDocumentByEmail(idCliente);
                    }
                });

        builder.setNegativeButton("Otros",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        showDialogToGetDocumentUrl(idCliente, idDocumento);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showDialogSendDocumentByEmail(final String idCliente) {

        final Dialog dialog = new Dialog(myContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_send_data_to_email);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final EditText edtDestinatarios = (EditText) dialog.findViewById(R.id.edtDestinatarios);
        final EditText edtAsunto = (EditText) dialog.findViewById(R.id.edtAsunto);
        final EditText edtCuerpo = (EditText) dialog.findViewById(R.id.edtCuerpo);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);
        progressBar.setVisibility(View.GONE);

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtDestinatarios.getText().toString().isEmpty() && !edtAsunto.getText().toString().isEmpty() && !edtCuerpo.getText().toString().isEmpty()) {
                    sendEmail(progressBar,
                            dialog,
                            edtAsunto,
                            idCliente,
                            Singleton.getInstance().getUserCode(),
                            edtDestinatarios.getText().toString(),
                            edtAsunto.getText().toString(),
                            edtCuerpo.getText().toString().replaceAll("\n", "%0A"),
                            "1");
                } else {
                    Common.showToastMessage(myContext, "Por favor ingrese todos los campos.");
                }

            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void sendEmail(final ProgressBar progressBar, final Dialog dialog, final EditText edt, String idCliente, String idUsuario, String destinatarios, String asunto, String cuerpo, String adjunto) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CorreoApi request = retrofit.create(CorreoApi.class);
        Call<ArrayList<CorreoResponse>> call = request.sendDataToEmail(idCliente, idUsuario, destinatarios, asunto, cuerpo, adjunto); //("jsalazar", "00");

        call.enqueue(new Callback<ArrayList<CorreoResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<CorreoResponse>> call, Response<ArrayList<CorreoResponse>> response) {
                if (response != null) {

                    ArrayList<CorreoResponse> respuesta = response.body();

                    if(respuesta.get(0).getRespuesta().equals("ok")) {
                        Common.showToastMessage(myContext, "Mensaje enviado!");
                        Common.hideKeyboard(myContext, edt);
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    } else {
                        Common.showToastMessage(myContext, "Error en el servidor");
                        //Common.showToastMessage(myContext, respuesta.get(0).getRespuesta());
                        Log.e(Constants.log_arrow_failure, respuesta.get(0).getRespuesta());
                        progressBar.setVisibility(View.GONE);
                        Common.hideKeyboard(myContext, edt);
                    }

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.hideKeyboard(myContext, edt);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CorreoResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                Common.hideKeyboard(myContext, edt);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showDialogToGetDocumentUrl(final String idCliente, String idDocumento) {

        final Dialog dialog = new Dialog(myContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_copy_url_to_send);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final EditText edtMensaje = (EditText) dialog.findViewById(R.id.edtMensaje);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);
        progressBar.setVisibility(View.GONE);

        getDocumentUrl(progressBar, dialog, edtMensaje, idCliente, idDocumento);

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMensaje.getText().toString().isEmpty()) {
                    if(!messageToCopy.isEmpty()) {
                        Common.copyTextToClipboard(myContext, messageToCopy);
                        Common.showToastMessageShort(myContext, "Mensaje copiado");
                        dialog.dismiss();
                    }
                }
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    String messageToCopy = Constants.Empty;
    private void getDocumentUrl(final ProgressBar progressBar, final Dialog dialog, final EditText edt, String idCliente, String idDocumento) {

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MensajeApi request = retrofit.create(MensajeApi.class);
        Call<ArrayList<MensajeResponse>> call = request.getDocumentUrl(idCliente, idDocumento); //("jsalazar", "00");

        call.enqueue(new Callback<ArrayList<MensajeResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<MensajeResponse>> call, Response<ArrayList<MensajeResponse>> response) {
                if (response != null) {

                    ArrayList<MensajeResponse> respuesta = response.body();

                    if(!respuesta.get(0).getUrl().isEmpty()) {
                        Common.showToastMessageShort(myContext, "Mensaje obtenido");
                        messageToCopy = respuesta.get(0).getUrl();
                        edt.setText(messageToCopy);
                        Common.hideKeyboard(myContext, edt);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Common.showToastMessage(myContext, "Error en el servidor");
                        Log.e(Constants.log_arrow_failure, respuesta.get(0).getUrl());
                        progressBar.setVisibility(View.GONE);
                        Common.hideKeyboard(myContext, edt);
                    }

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.hideKeyboard(myContext, edt);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MensajeResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                Common.hideKeyboard(myContext, edt);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
