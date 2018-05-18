package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.UnitsResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 18/11/16.
 */
public interface UnitsApi {

    @POST(Constants.url_get_units)
    Call<ArrayList<UnitsResponse>> getUnits(
            @Header(Constants.articuloUnitsHeader) String idArticulo
    );

}
