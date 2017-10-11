package com.corporacioncorrales.cotizacionesapp.utils;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;
import com.corporacioncorrales.cotizacionesapp.model.PaymentsResponse;

import java.util.ArrayList;

/**
 * Created by victor on 8/11/16.
 */
public class Singleton {

    //private static Singleton ourInstance = new Singleton();
    private static Singleton ourInstance;
    private String user;
    private String userCode;
    private String rubroSelected;
    private String saldoDisponibleCliente;
    private String tipoDocumento;
    private ClientsResponse clientSelected;
    private String idclientSelected;
    private ArrayList<PaymentsResponse> paymentTypes;
    private String idPaymentTypeSelected;


    private Singleton() {
    }

    public ArrayList<PaymentsResponse> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(ArrayList<PaymentsResponse> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public String getIdPaymentTypeSelected() {
        return idPaymentTypeSelected;
    }

    public void setIdPaymentTypeSelected(String idPaymentTypeSelected) {
        this.idPaymentTypeSelected = idPaymentTypeSelected;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getSaldoDisponibleCliente() {
        return saldoDisponibleCliente;
    }

    public void setSaldoDisponibleCliente(String saldoDisponibleCliente) {
        this.saldoDisponibleCliente = saldoDisponibleCliente;
    }

    public String getRubroSelected() {
        return rubroSelected;
    }

    public void setRubroSelected(String rubroSelected) {
        this.rubroSelected = rubroSelected;
    }

    public static Singleton getInstance() {

        if(ourInstance==null) {
            ourInstance = new Singleton();
        }

        return ourInstance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public ClientsResponse getClientSelected() {
        return clientSelected;
    }

    public void setClientSelected(ClientsResponse clientSelected) {
        this.clientSelected = clientSelected;
    }

    public String getIdclientSelected() {
        return idclientSelected;
    }

    public void setIdclientSelected(String idclientSelected) {
        this.idclientSelected = idclientSelected;
    }

}
