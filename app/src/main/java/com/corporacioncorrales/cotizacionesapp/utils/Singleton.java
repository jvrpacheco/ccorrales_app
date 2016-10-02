package com.corporacioncorrales.cotizacionesapp.utils;

import com.corporacioncorrales.cotizacionesapp.model.ClientsResponse;

/**
 * Created by victor on 8/11/16.
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();
    private String user;
    private String userCode;
    private String rubroSelected;
    private String saldoDisponibleCliente;
    private String tipoDocumento; //Factura/Proforma
    private ClientsResponse clientSelected;
    private String idclientSelected;


    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    private Singleton() {
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
