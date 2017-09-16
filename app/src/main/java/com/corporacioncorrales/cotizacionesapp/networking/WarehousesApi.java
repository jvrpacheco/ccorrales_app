package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;
import com.corporacioncorrales.cotizacionesapp.model.WarehouseResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 14/10/16.
 */
public interface WarehousesApi {

    @POST(Constants.url_get_warehouses_stock)
    Call<ArrayList<WarehouseResponse>> getWarehousesStock(
            @Header(Constants.stockAlmacenIdArticulo) String idArticulo,
            @Header(Constants.stockAlmacenUnidad) String unidad
    );

}
