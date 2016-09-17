package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 8/10/16.
 */
public class PricesHistoryResponse {

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("precio")
    @Expose
    private String precio;


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
