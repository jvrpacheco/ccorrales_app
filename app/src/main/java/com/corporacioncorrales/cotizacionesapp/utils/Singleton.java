package com.corporacioncorrales.cotizacionesapp.utils;

/**
 * Created by victor on 8/11/16.
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();
    private String user;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
