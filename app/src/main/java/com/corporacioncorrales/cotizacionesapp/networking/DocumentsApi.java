package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface DocumentsApi {

    //http://190.81.34.42:8080/api/consultaproforma?id=224&rubro=01
    //http://190.81.34.42:8080/api/consultaproforma?id={id}&rubro={rubro}

    @GET(Constants.url_documents_history)
    Call<ArrayList<DocumentsResponse>> getDocumentsHistory(
            @Query("id") String id,
            @Query("rubro") String rubro
    );

}
