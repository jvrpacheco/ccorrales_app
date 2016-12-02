package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.CorreoResponse;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by victor on 8/10/16.
 */
public interface CorreoApi {

    @POST(Constants.url_enviar_datos_correo)
    Call<ArrayList<CorreoResponse>> sendDataToEmail(
            @Header(Constants.idClienteHeader) String idCliente,
            @Header(Constants.idUsuarioHeader) String idUsuario,
            @Header(Constants.correosDestinatariosHeader) String idCorreoDestinatariosHeader,
            @Header(Constants.correoAsuntoHeader) String asunto,
            @Header(Constants.correoCuerpoHeader) String cuerpo,
            @Header(Constants.correoAdjuntoHeader) String adjunto,
            @Header(Constants.idProformaHeader) String idProforma
    );

}
