package com.corporacioncorrales.cotizacionesapp.networking;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by victor on 8/10/16.
 */
public interface LoginApi {

    /*@GET("/api/usuario/{user}")
    public String getUserAccess(@Path("user") String user, Callback<String> response);*/

    @GET("/api/usuario/{user}")
    public String getUserAccess(@Path("user") String user, Callback<String> response);
}
