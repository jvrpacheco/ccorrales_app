package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.LoginRequest;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by victor on 8/10/16.
 */
public interface LoginApi {

    /*@GET("/api/usuario/{user}")
    public String getUserAccess(@Path("user") String user, Callback<String> response);*/

    /*@GET("/api/usuario/{user}")
    public String getUserAccess(@Path("user") String user, Callback<String> response);*/

    /*@GET("/api/usuario/{user}")
    Call<LoginRequest> userCanLogin(@Path("user") String username);*/

    /*@GET("/api/usuario/{user}")
    public Boolean userCanLogin(
            @Path("user") String user,
            Callback<String> canLogin);*/

    /*@GET("/api/usuario/{user}")
     public LoginResponse userCanLogin(
            @Path("user") String user,
            Callback<LoginResponse> responseFromServer);*/

    @GET("/api/usuario/{user}")
    Call<LoginResponse> getUserAccess(
        @Path("user") String user
    );
}
