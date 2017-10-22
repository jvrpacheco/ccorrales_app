package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.PaymentsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 21/11/16.
 */
public interface PaymentsApi {

    @GET(Constants.url_get_payment_options)
    Call<ArrayList<PaymentsResponse>> getPaymentOptions();

}
