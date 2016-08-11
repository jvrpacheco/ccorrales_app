package com.corporacioncorrales.cotizacionesapp.model;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by victor on 8/10/16.
 */
public class LoginRequest {

    private String CodUsu;

    public String getCodUsu() {
        return CodUsu;
    }

    public void setCodUsu(String codUsu) {
        CodUsu = codUsu;
    }


}
