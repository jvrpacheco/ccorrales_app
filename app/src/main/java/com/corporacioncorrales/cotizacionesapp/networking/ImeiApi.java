package com.corporacioncorrales.cotizacionesapp.networking;

import com.corporacioncorrales.cotizacionesapp.model.ImeiResponse;
import com.corporacioncorrales.cotizacionesapp.model.MensajeResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by victor on 8/10/16.
 */
public interface ImeiApi {

    @POST(Constants.url_check_imei_on_server)
    Call<ArrayList<ImeiResponse>> checkDeviceOnServer(
            @Header(Constants.imeiHeader) String deviceId
    );

}
