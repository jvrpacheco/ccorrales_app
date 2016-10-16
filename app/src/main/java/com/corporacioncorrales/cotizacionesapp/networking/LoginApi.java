package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.LoginRequest;
import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

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

    @GET(Constants.url_user_login)
    Call<LoginResponse> getUserAccess(
        @Path("user") String user
    );

}
