package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 8/10/16.
 */
public class LoginResponse {

    @SerializedName("CodUsu")
    @Expose
    private String CodUsu;

    @SerializedName("FocoUsu")
    @Expose
    private String FocoUsu;



    public String getCodUsu() {
        return CodUsu;
    }

    public String getFocoUsu() {
        return FocoUsu;
    }

}
