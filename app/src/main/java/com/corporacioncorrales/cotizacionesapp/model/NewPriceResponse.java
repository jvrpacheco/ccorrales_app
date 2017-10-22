package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 18/11/16.
 */
public class NewPriceResponse {

    @SerializedName("Importe_nuevo")
    @Expose
    private String importeRecalculado;

    public String getImporteRecalculado() {
        return importeRecalculado;
    }

    public void setImporteRecalculado(String importeRecalculado) {
        this.importeRecalculado = importeRecalculado;
    }

}
