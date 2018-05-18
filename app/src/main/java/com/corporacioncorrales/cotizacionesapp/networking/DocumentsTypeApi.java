package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.DocumentsTypeResponse;
import com.corporacioncorrales.cotizacionesapp.model.WarehouseResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 22/11/17.
 */
public interface DocumentsTypeApi {

    @POST(Constants.url_get_documents_types)
    Call<ArrayList<DocumentsTypeResponse>> getDocumentTypes(
            @Header(Constants.tipoDocumentoUserId) String idTipoDocumento
    );

}
