package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface ProductsApi {

    //http://190.81.34.42:8080/api/articulos?id=124896&rubro=00
    // "/api/articulos?id={id}&rubro={rubro}"

    @GET(Constants.url_products_per_user)
    Call<ArrayList<ProductsResponse>> getProductsPerClient(
            @Query("id") String id,  //id del Cliente
            @Query("rubro") String rubro
    );

}
