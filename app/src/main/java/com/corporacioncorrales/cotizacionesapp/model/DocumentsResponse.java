package com.corporacioncorrales.cotizacionesapp.model;

import android.util.Log;

import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 9/22/16.
 */

public class DocumentsResponse {

    @SerializedName("id")
    @Expose
    private String idDocumento;

    @SerializedName("tipdoc")
    @Expose
    private String idTipoDocumento;

    @SerializedName("doc")
    @Expose
    private String labelSiglasTipoDocumento;

    @SerializedName("serie")
    @Expose
    private String nroSerieDocumento;

    @SerializedName("numero")
    @Expose
    private String nroDocumento;

    @SerializedName("fecha")
    @Expose
    private String fechaEmisionDocumento;

    @SerializedName("idcliente")
    @Expose
    private String idCliente;

    @SerializedName("nombre")
    @Expose
    private String nombreCliente;

    @SerializedName("mon")
    @Expose
    private String monedaDocumento;

    @SerializedName("total")
    @Expose
    private String montoTotalDocumento;

    @SerializedName("idrubro")
    @Expose
    private String idRubroDocumento;

    @SerializedName("rubro")
    @Expose
    private String labelRubroDocumento;

    @SerializedName("estado_doc")
    @Expose
    private String estadoDocumento;

    @SerializedName("estado")
    @Expose
    private String labelEstadoDocumento;

    @SerializedName("linea_disponible")
    @Expose
    private String linea_disponible;



    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(String idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getLabelSiglasTipoDocumento() {
        return labelSiglasTipoDocumento;
    }

    public void setLabelSiglasTipoDocumento(String labelSiglasTipoDocumento) {
        this.labelSiglasTipoDocumento = labelSiglasTipoDocumento;
    }

    public String getNroSerieDocumento() {
        return nroSerieDocumento;
    }

    public void setNroSerieDocumento(String nroSerieDocumento) {
        this.nroSerieDocumento = nroSerieDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getFechaEmisionDocumento() {
        return fechaEmisionDocumento;
    }

    public void setFechaEmisionDocumento(String fechaEmisionDocumento) {
        this.fechaEmisionDocumento = fechaEmisionDocumento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getMonedaDocumento() {
        return monedaDocumento;
    }

    public void setMonedaDocumento(String monedaDocumento) {
        this.monedaDocumento = monedaDocumento;
    }

    public String getMontoTotalDocumento() {
        try{
            Double precioLista = Double.parseDouble(montoTotalDocumento);
            montoTotalDocumento = String.format(Constants.round_two_decimals, precioLista);
        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, ex.toString());
        }
        return montoTotalDocumento;
    }

    public void setMontoTotalDocumento(String montoTotalDocumento) {
        this.montoTotalDocumento = montoTotalDocumento;
    }

    public String getIdRubroDocumento() {
        return idRubroDocumento;
    }

    public void setIdRubroDocumento(String idRubroDocumento) {
        this.idRubroDocumento = idRubroDocumento;
    }

    public String getLabelRubroDocumento() {
        return labelRubroDocumento;
    }

    public void setLabelRubroDocumento(String labelRubroDocumento) {
        this.labelRubroDocumento = labelRubroDocumento;
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getLabelEstadoDocumento() {
        return labelEstadoDocumento;
    }

    public void setLabelEstadoDocumento(String labelEstadoDocumento) {
        this.labelEstadoDocumento = labelEstadoDocumento;
    }

    public String getLinea_disponible() {
        String linea = Constants.Empty;
        try{
            if(linea_disponible.contains(",")) {
                linea_disponible = linea_disponible.replace(",", ".");
                linea = String.format(Constants.round_two_decimals, Double.valueOf(linea_disponible));
            }
        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }
        return linea;
    }

    public void setLinea_disponible(String linea_disponible) {
        this.linea_disponible = linea_disponible;
    }

}
