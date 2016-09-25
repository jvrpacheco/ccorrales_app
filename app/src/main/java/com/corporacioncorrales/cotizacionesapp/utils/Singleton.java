package com.corporacioncorrales.cotizacionesapp.utils;

/**
 * Created by victor on 8/11/16.
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();
    private String user;
    private String userCode;
    private String rubroSelected;
    private String lineaDeCreditoCliente;
    private String tipoDocumento; //Factura/Proforma


    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    private Singleton() {
    }

    public String getLineaDeCreditoCliente() {
        return lineaDeCreditoCliente;
    }

    public void setLineaDeCreditoCliente(String lineaDeCreditoCliente) {
        this.lineaDeCreditoCliente = lineaDeCreditoCliente;
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

}
