package com.corporacioncorrales.cotizacionesapp.model;

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

    @SerializedName("Foto")
    @Expose
    private String Foto;



    public String getId() {
        return Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getFoto() {
        return Foto;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Nombre = "+Nombre+", Id = "+Id+", Foto = "+Foto+", Precio = "+Precio+"]";
    }

}
