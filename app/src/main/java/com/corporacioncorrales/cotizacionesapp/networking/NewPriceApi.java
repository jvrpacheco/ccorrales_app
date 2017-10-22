package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.NewPriceResponse;
import com.corporacioncorrales.cotizacionesapp.model.UnitsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 18/11/16.
 */
public interface NewPriceApi {

    @POST(Constants.url_get_new_price)
    Call<ArrayList<NewPriceResponse>> getNewPriceRecalculated(
            @Header(Constants.articuloUnitsHeader) String idArticulo,
            @Header(Constants.idUnidadVentaHeader) String unidadVenta,
            @Header(Constants.precioIngresadoHeader) String precioIngresado
    );

}
