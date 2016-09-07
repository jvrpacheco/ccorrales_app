package com.corporacioncorrales.cotizacionesapp.model;

/**
 * Created by victor on 8/10/16.
 */
public class QuotationProductRequest {

    private String articulo;
    private String precio;
    private String precio_real;
    private String cantidad;

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPrecio_real() {
        return precio_real;
    }

    public void setPrecio_real(String precio_real) {
        this.precio_real = precio_real;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

}
