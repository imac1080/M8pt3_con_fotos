package com.example.m8button;

import android.graphics.Bitmap;
import android.graphics.drawable.DrawableWrapper;
import android.widget.ImageView;

public class Persona {
    String nom;
    int qualificacio;
    ImageView imageView;


    public Persona(String nom, int qualificacio) {
        this.nom = nom;
        this.qualificacio = qualificacio;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom+" - "+qualificacio;
    }
}
