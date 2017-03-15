package com.example.pakoandrade.plannerland.objects;


public class UserPlanner {
    private int imagen;
    private String nombre;
    private String habilidades;
    private String lat;
    private String lon;
    private String userMember;

    public UserPlanner(int imagen, String nombre, String habilidades, String lat, String lon, String userMember) {
        this.userMember = userMember;
        this.lon = lon;
        this.lat = lat;
        this.imagen = imagen;
        this.nombre = nombre;
        this.habilidades = habilidades;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public int getImagen() {
        return imagen;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getUserMember() {
        return userMember;
    }
}


