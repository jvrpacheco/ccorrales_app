package com.corporacioncorrales.cotizacionesapp.model;

import android.util.Log;

import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 8/11/16.
 */
public class ProductsResponse {

    //obtenemos los campos de cada elemento del array de objetos de tipo ProductsResponse

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("Nombre")
    @Expose
    private String Nombre;

    @SerializedName("Precio")
    @Expose
    private String PrecioProductResponse;

    @SerializedName("Pre_inferior")
    @Expose
    private String Pre_inferior;

    @SerializedName("Unidad")
    @Expose
    private String idUnidad;   //id de la unidad de medida del producto

    @SerializedName("Presentacion")
    @Expose
    private String presentacionUnidad;   //descripcion de la unidad de medida del producto

    @SerializedName("Foto")
    @Expose
    private String Foto;

    @SerializedName("Cantidad")
    @Expose
    private String Cantidad;

    //********************************************

    private Boolean isSelected;
    //por defecto "0" que quiere decir "Todos"
    private String idAlmacenAsociado = "0";

    public String getIdAlmacenAsociado() {
        return idAlmacenAsociado;
    }

    public void setIdAlmacenAsociado(String idAlmacenAsociado) {
        this.idAlmacenAsociado = idAlmacenAsociado;
    }

    public Boolean getSelected() {
        Boolean flag;
        if(isSelected == null) {
            flag = false;
        } else {
            flag = isSelected;
        }
        return  flag;
    }
    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    //********************************************

    private String nuevoPrecio;

    public String getNuevoPrecio() {
        try{
            if(nuevoPrecio!=null) {
                Double xxx = Double.parseDouble(nuevoPrecio);
                nuevoPrecio = String.format(Constants.round_three_decimals, xxx);
            } else {
                nuevoPrecio = getPrecioProductResponse();
            }
        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, "nuevoprecio...." +  ex.toString());
        }
        return nuevoPrecio;
    }

    public void setNuevoPrecio(String nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }

    //********************************************

    private Boolean esPrecioMenorAlLimite;

    public Boolean getEsPrecioMenorAlLimite() {
        Boolean flag;
        if(esPrecioMenorAlLimite == null) {
            flag = false;
        } else {
            flag = esPrecioMenorAlLimite;
        }
        return flag;
    }

    public void setEsPrecioMenorAlLimite(Boolean esPrecioMenorAlLimite) {
        this.esPrecioMenorAlLimite = esPrecioMenorAlLimite;
    }

    //********************************************

    private String cantidadSolicitada;

    public String getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(String cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    //********************************************

    private String nuevaCantidad;  //nuevo Stock de acuerdo a la unidad seleccionada

    public String getNuevaCantidad() {
        return nuevaCantidad;
    }

    public void setNuevaCantidad(String nuevaCantidad) {
        this.nuevaCantidad = nuevaCantidad;
    }

    //********************************************

    private String nuevaUnidad;

    public String getNuevaUnidad() {
        String newUnit = "";

        if(nuevaUnidad==null) {
            newUnit = getIdUnidad();
        } else {
            newUnit = nuevaUnidad;
        }

        return newUnit;
    }

    public void setNuevaUnidad(String nuevaUnidad) {
        this.nuevaUnidad = nuevaUnidad;
    }

    //********************************************

    private String nuevaPresentacion;

    public String getNuevaPresentacion() {
        String newPresentation = "";

        if(nuevaPresentacion==null) {
            newPresentation = getPresentacionUnidad();
        } else {
            newPresentation = nuevaPresentacion;
        }

        return newPresentation;
    }

    public void setNuevaPresentacion(String nuevaPresentacion) {
        this.nuevaPresentacion = nuevaPresentacion;
    }

    //********************************************

    private String precioRecalculado;

    public String getPrecioRecalculado() {
        String precioRecal = "";

        if(precioRecalculado==null) {
            //precioRecal = getPrecioProductResponse();

            try{
                Double precio = Double.parseDouble(getPrecioProductResponse());
                precioRecal = String.format(Constants.round_three_decimals, precio);
            } catch (Exception ex) {
                Log.e(Constants.log_arrow_error, ex.toString());
            }
        } else {
            precioRecal = precioRecalculado;
        }

        return precioRecal;
        //return precioRecalculado;
    }

    public void setPrecioRecalculado(String precioRecalculado) {
        this.precioRecalculado = precioRecalculado;
    }

    //********************************************


    public String getId() {
        return Id.trim();
    }

    public String getNombre() {
        return Nombre;
    }

    public String getPrecioProductResponse() {
        try{
            Double precioLista = Double.parseDouble(PrecioProductResponse);
            PrecioProductResponse = String.format(Constants.round_three_decimals, precioLista);
        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, ex.toString());
        }
        return PrecioProductResponse;
    }


    public void setPrecioProductResponse(String precio) {
        PrecioProductResponse = precio;
    }

    public String getPre_inferior() {
        return Pre_inferior;
    }

    public void setPre_inferior(String pre_inferior) {
        Pre_inferior = pre_inferior;
    }

    public String getFoto() {
        return Foto;
    }

    public String getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(String unidad) {
        idUnidad = unidad;
    }

    public String getPresentacionUnidad() {
        return presentacionUnidad;
    }

    public void setPresentacionUnidad(String presentacion) {
        presentacionUnidad = presentacion;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    @Override
    public String toString()
    {
        return "Producto: [Nombre = "+Nombre+", Id = "+Id+", Foto = "+Foto+" , Pre_inferior = "+Pre_inferior + ", Precio = "+PrecioProductResponse+
               ", idUnidad = " + idUnidad + ", presentacionUnidad = "+ presentacionUnidad + ", Cantidad = " + Cantidad + "]";
    }

}
