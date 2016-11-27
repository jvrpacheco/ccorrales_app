package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 21/11/16.
 */
public class PaymentsResponse {

    @SerializedName("id")
    @Expose
    private String idPaymentType;

    @SerializedName("nombre")
    @Expose
    private String paymentType;


    public String getIdPaymentType() {
        return idPaymentType;
    }

    public void setIdPaymentType(String idPaymentType) {
        this.idPaymentType = idPaymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

}
