package com.corporacioncorrales.cotizacionesapp.model;

/**
 * Created by victor on 8/10/16.
 */
public class Client {

    //Conver json response to model: http://pojo.sodhanalibrary.com/

    public int Id;
    public String Ruc;
    public String Razon_Social;
    public String Foto;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }

    public String getRazon_Social() {
        return Razon_Social;
    }

    public void setRazon_Social(String razon_Social) {
        Razon_Social = razon_Social;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [Ruc = "+Ruc+", Id = "+Id+", Foto = "+Foto+", Razon_Social = "+Razon_Social+"]";
    }

}
