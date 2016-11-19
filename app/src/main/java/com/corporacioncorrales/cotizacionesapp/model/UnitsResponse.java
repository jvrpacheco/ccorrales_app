package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 18/11/16.
 */
public class UnitsResponse {

    @SerializedName("Unidad")
    @Expose
    private String unidad;

    @SerializedName("Presentacion")
    @Expose
    private String presentacion;

    @SerializedName("Stock")
    @Expose
    private String stock;



    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getStock() {
        return stock;
        //return "55";
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
