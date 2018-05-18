package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.MensajeResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 14/10/16.
 */
public interface VirtualStockApi {

    @POST(Constants.url_get_virtual_stock)
    Call<ArrayList<VirtualStockResponse>> getVirtualStock(
            @Header(Constants.stockVirtualUsuario) String idUsuario,
            @Header(Constants.stockVirtualRubro) String idRubro,
            @Header(Constants.stockVirtualArticulo) String idArticulo
    );

}
