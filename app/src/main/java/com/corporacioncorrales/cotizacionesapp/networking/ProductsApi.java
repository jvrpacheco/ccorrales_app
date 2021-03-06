package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
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
public interface ProductsApi {

    @GET(Constants.url_products_per_user)
    Call<ArrayList<ProductsResponse>> getProductsPerClient(
            @Query("id") String id,
            @Query("rubro") String rubro
    );

    @GET(Constants.url_image_zoom)
    Call<ProductsResponse> getProductImageZoom(
            @Query("id") String id
    );

    @POST(Constants.url_prices_history)
    Call<ArrayList<PricesHistoryResponse>> getPricesHistory(
            @Header(Constants.idClienteHeader) String idCliente,
            @Header(Constants.idArticuloHeader) String idArticulo
    );
}
