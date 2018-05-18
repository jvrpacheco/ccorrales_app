package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 02/10/16.
 */

public class DocumentDetailResponse {

    @SerializedName("Articulo")
    @Expose
    private String idProducto;

    @SerializedName("Descripcion")
    @Expose
    private String nombreProducto;

    @SerializedName("Precio")
    @Expose
    private String precioIngresadoPorVendedor;

    @SerializedName("Precio_actual")
    @Expose
    private String precioListaActual; //precio lista al momento de la visualizacion del detalle del documento

    @SerializedName("precio_anterior")
    @Expose
    private String precioListaAnterior; //precio lista al momento de ingreso por el vendedor en la generacion del documento

    @SerializedName("Cantidad")
    @Expose
    private String cantidadSolicitada;

    @SerializedName("Total")
    @Expose
    private String montoTotalEnDocumento;

    @SerializedName("Unidad")
    @Expose
    private String idUnidad;

    @SerializedName("Unidadtexto")
    @Expose
    private String nombreUnidad;

    @SerializedName("stock")
    @Expose
    private String stockUnidad;

    @SerializedName("precioxunidad")
    @Expose
    private String precioPorUnidad;

    public String getIdProducto() {
        return idProducto.trim();
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getPrecioIngresadoPorVendedor() {
        return precioIngresadoPorVendedor;
    }

    public void setPrecioIngresadoPorVendedor(String precioIngresadoPorVendedor) {
        this.precioIngresadoPorVendedor = precioIngresadoPorVendedor;
    }

    public String getPrecioListaActual() {
        return precioListaActual;
    }

    public void setPrecioListaActual(String precioListaActual) {
        this.precioListaActual = precioListaActual;
    }

    public String getPrecioListaAnterior() {
        return precioListaAnterior;
    }

    public void setPrecioListaAnterior(String precioListaAnterior) {
        this.precioListaAnterior = precioListaAnterior;
    }

    public String getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(String cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getMontoTotalEnDocumento() {
        return montoTotalEnDocumento;
    }

    public void setMontoTotalEnDocumento(String montoTotalEnDocumento) {
        this.montoTotalEnDocumento = montoTotalEnDocumento;
    }

    public String getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(String idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getStockUnidad() {
        return stockUnidad;
    }

    public void setStockUnidad(String stockUnidad) {
        this.stockUnidad = stockUnidad;
    }

    public String getPrecioPorUnidad() {
        return precioPorUnidad;
    }

    public void setPrecioPorUnidad(String precioPorUnidad) {
        this.precioPorUnidad = precioPorUnidad;
    }
}
