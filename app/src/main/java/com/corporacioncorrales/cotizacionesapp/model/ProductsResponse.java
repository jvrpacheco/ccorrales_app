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
    private String Precio;

    @SerializedName("Pre_inferior")
    @Expose
    private String Pre_inferior;

    @SerializedName("Foto")
    @Expose
    private String Foto;

    @SerializedName("Cantidad")
    @Expose
    private String Cantidad;

    //********************************************

    private Boolean isSelected;

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

    public String getNuevoPrecio() {
        try{
            Double lineaCredito = Double.parseDouble(nuevoPrecio);
            nuevoPrecio = String.format(Constants.round_three_decimals, lineaCredito);
        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, ex.toString());
        }
        return nuevoPrecio;
    }

    public void setNuevoPrecio(String nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }

    private String nuevoPrecio;

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

    public String getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(String cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    private String cantidadSolicitada;


    //********************************************

    public String getId() {
        return Id.trim();
    }

    public String getNombre() {
        return Nombre;
    }

    public String getPrecio() {
        try{
            Double precioLista = Double.parseDouble(Precio);
            Precio = String.format(Constants.round_three_decimals, precioLista);
        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, ex.toString());
        }
        return Precio;
    }

    public String getPre_inferior() {
        return Pre_inferior;
    }

    public String getFoto() {
        return Foto;
    }

    public String getCantidad() {
        return Cantidad;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [Nombre = "+Nombre+", Id = "+Id+", Foto = "+Foto+" , Cantidad = "+Cantidad+" , Pre_inferior = "+Pre_inferior + ", Precio = "+Precio+"]";
    }

}
