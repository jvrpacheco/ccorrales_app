package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 9/12/17.
 */
public class VirtualStockResponse {

    @SerializedName("Codigo")
    @Expose
    private String codigo;

    @SerializedName("Fecha")
    @Expose
    private String fecha;

    @SerializedName("Cantidad")
    @Expose
    private String cantidad;


    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
