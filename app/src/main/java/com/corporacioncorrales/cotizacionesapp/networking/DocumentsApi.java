package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.DocumentDetailResponse;
import com.corporacioncorrales.cotizacionesapp.model.DocumentsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface DocumentsApi {

    @POST(Constants.url_documents_history)
    Call<ArrayList<DocumentsResponse>> getDocumentsHistory(
            @Header(Constants.idUsuarioHeader) String idUsuario,
            @Header(Constants.idClienteHeader) String idCliente,
            @Header(Constants.idRubroDocHeader) String idRubro,
            @Header(Constants.idEstadoDocHeader) String idEstadoDoc,
            @Header(Constants.idFechaInicioDocHeader) String fechaInicio,
            @Header(Constants.idFechaFinDocHeader) String fechaFin
    );

    @GET(Constants.url_documents_history_detail)
    Call<ArrayList<DocumentDetailResponse>> getProductsFromDocumentDetail(
            @Query("id") String id   //id del documento
    );
}
