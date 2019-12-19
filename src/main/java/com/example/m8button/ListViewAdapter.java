package com.example.m8button;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter {

    public ListViewAdapter(Context c, ArrayList<Persona> personas){
        super(c, 0, personas);
    }

    public View getView(int position, View view, ViewGroup parent) {

        final  Persona persona = (Persona) getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_second_adapter, parent, false);
        }

        TextView nomPersona = view.findViewById(R.id.nomTextView);
        TextView intentsPersona = view.findViewById(R.id.intentosTextView);
        ImageView fotoPersona = view.findViewById(R.id.perfilImageView);


        fotoPersona.setImageURI(persona.getFotoRanking());
        nomPersona.setText("Nom: " + persona.getNom());
        intentsPersona.setText("Intents: " + persona.getQualificacio());


        return view;

    }
}
