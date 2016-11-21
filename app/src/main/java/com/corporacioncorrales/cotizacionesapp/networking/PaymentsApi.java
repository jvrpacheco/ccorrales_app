package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.LoginResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 20/11/16.
 */
public interface PaymentsApi {

    @GET(Constants.url_get_payment_options)
    Call<LoginResponse> getPaymentOptions();

}
