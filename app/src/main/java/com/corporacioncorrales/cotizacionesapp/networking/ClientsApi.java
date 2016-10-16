package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface ClientsApi {

    @GET(Constants.url_clients_per_user)
    Call<ArrayList<ClientsResponse>> getClientsPerUser(
            @Query("id") String id,
            @Query("rubro") String rubro,
            @Query("orden") String orden
    );

}
