package com.corporacioncorrales.cotizacionesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 8/11/16.
 */
public class ClientsResponse {

    //obtenemos los campos de cada elemento del array de objetos de tipo ClientsResponse

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("Ruc")
    @Expose
    private String Ruc;

    @SerializedName("Razon_Social")
    @Expose
    private String Razon_Social;

    @SerializedName("Foto")
    @Expose
    private String Foto;



    public String getId() {
        return Id;
    }

    public String getRuc() {
        return Ruc;
    }

    public String getRazon_Social() {
        return Razon_Social;
    }

    public String getFoto() {
        return Foto;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Ruc = "+Ruc+", Id = "+Id+", Foto = "+Foto+", Razon_Social = "+Razon_Social+"]";
    }

}
