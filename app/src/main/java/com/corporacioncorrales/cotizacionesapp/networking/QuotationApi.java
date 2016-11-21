package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.QuotationProductRequest;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface QuotationApi {

    @POST(Constants.url_send_quotation)
    Call<String> sendQuotation(
            @Header(Constants.idClienteHeader) String idCliente,
            @Header(Constants.idRubroHeader) String idRubro,
            @Header(Constants.idUsuarioHeader) String idUsuario,
            @Header(Constants.idSobregiroHeader) String sobregiro,
            @Header(Constants.idTipoDocHeader) String tipoDocumento,
            @Header(Constants.formaPagoHeader) String formaDePago,
            @Header(Constants.totalHeader) String montoTotal,
            @Body ArrayList<QuotationProductRequest> data
    );

}
