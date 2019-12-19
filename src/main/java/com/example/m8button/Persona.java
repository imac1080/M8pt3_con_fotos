package com.example.m8button;

import android.graphics.Bitmap;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.widget.ImageView;

public class Persona implements Comparable<Persona> {
    String nom;
    int qualificacio;
    Uri fotoRanking;
    ImageView imageView;


    public Persona(String nom, int qualificacio) {
        this.nom = nom;
        this.qualificacio = qualificacio;
    }
    public Persona(String nom, int qualificacio, Uri fotoRanking) {
        this.nom = nom;
        this.qualificacio = qualificacio;
        this.fotoRanking =fotoRanking;
    }
    public Uri getFotoRanking() {
        return fotoRanking;
    }
    public int getQualificacio() {
        return qualificacio;
    }
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom+" - "+qualificacio;
    }

    @Override
    public int compareTo(Persona o) {
        if (qualificacio < o.qualificacio){
            return -1;
        }
        if (qualificacio > o.qualificacio){
            return 1;
        }
        return 0;
    }
}
