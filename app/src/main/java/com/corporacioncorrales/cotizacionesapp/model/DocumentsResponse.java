package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 9/22/16.
 */

public class DocumentsResponse {

    @SerializedName("id")
    @Expose
    private int Id;

    @SerializedName("Fecha")
    @Expose
    private String Fecha;

    @SerializedName("mon")  //moneda
    @Expose
    private String Moneda;

    @SerializedName("total")
    @Expose
    private int Total;

    @SerializedName("rubro")
    @Expose
    private String Rubro;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getRubro() {
        return Rubro;
    }

    public void setRubro(String rubro) {
        Rubro = rubro;
    }

}
