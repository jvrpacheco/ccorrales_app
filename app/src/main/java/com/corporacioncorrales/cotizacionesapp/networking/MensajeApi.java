package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.MensajeResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 8/10/16.
 */
public interface MensajeApi {

    @POST(Constants.url_obtener_url_documento)
    Call<ArrayList<MensajeResponse>> getDocumentUrl(
            @Header(Constants.idClienteHeader) String idCliente,
            @Header(Constants.idDocumentoHeader) String idDocumento
    );

}
