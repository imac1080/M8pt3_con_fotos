package com.example.m8button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    File photoFile;
    int random = (int) (Math.random() * 50 + 1);
    static int intentos = 0;
    static String nom = null;
    static TextView textView2;
    static ArrayList<Persona> ListRanking = new ArrayList<Persona>();
    ImageView imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.button);
        final Button btnRanking = findViewById(R.id.btnranking);
        final EditText number = findViewById(R.id.textUserRanking);
        imageView2 = findViewById(R.id.imageViewFOTO);
        textView2 = findViewById(R.id.textView);
        LeerArray(MainActivity.this);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                //Log.d("-------","click");
                String textoProva = number.getText().toString();
                if (TextUtils.isEmpty(textoProva)) {
                    number.setError("No pots deixar-ho buit");
                    return;
                }
                if (button.getText().equals("TORNAR A COMENÇAR")) {
                    int random = (int) (Math.random() * 50 + 1);
                    button.setText("BUTTON");
                    number.setText("");
                } else {
                    intentos++;
                    if (Integer.parseInt(number.getText().toString()) == random) {
                        textView2.setText("HAS ENCERTAT EL NUMERO " + number.getText());
                        button.setText("TORNAR A COMENÇAR");
                        random = (int) (Math.random() * 50 + 1);
                        textView2.setText("");
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.dialog_signin);
                        dialog.setTitle("Title");
                        Button button = (Button) dialog.findViewById(R.id.button);
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                EditText edit = (EditText) dialog.findViewById(R.id.textUserRanking);
                                nom = edit.getText().toString();
                                if (IsOnArray(nom) == true) {
                                    if (TextUtils.isEmpty(nom)) {
                                        edit.setError("No pots deixar-ho buit");
                                        return;
                                    } else {
                                        dispatchTakePictureIntent();
                                        ListRanking.add(new Persona(nom, intentos, Uri.parse(photoFile.toString())));
                                        nom = null;
                                        intentos = 0;
                                        GuardarArray(MainActivity.this);
                                        dialog.dismiss();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "el nom " + nom + " ja esta utilitzat!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        Button buttonCancel = (Button) dialog.findViewById(R.id.btnranking);
                        buttonCancel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        String texto = "HAS FALLAT EL NUMERO(" + random + ") INTENT: " + intentos;
                        if (random < Integer.parseInt(String.valueOf(number.getText()))) {
                            texto = texto + "\n El numero es menor que " + number.getText();
                        } else {
                            texto = texto + "\n El numero es mayor que " + number.getText();
                        }
                        textView2.setText(texto);
                    }
                }

            }
        });
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RankingActivity.class));
            }
        });


    }

    public static boolean IsOnArray(String nombreTest) {
        for (int i = 0; i < ListRanking.size(); i++) {
            if (nombreTest.equals(ListRanking.get(i).getNom())) {
                return false;
            }
        }
        return true;
    }

    public void LeerArray(Context context) {

        try {
            File f = new File(context.getFilesDir(), "data.dat");
            if (f.exists()) {
                FileInputStream fout = new FileInputStream(f);
                ObjectInputStream oos = new ObjectInputStream(fout);
                ListRanking.clear();
                int i = 0;
                while (oos.available() > 0) {
                    String s = oos.readUTF();
                    int edat2 = oos.readInt();
                    String imagenStirng = oos.readUTF();


                    ListRanking.add(new Persona(s, edat2, Uri.parse(imagenStirng)));
                    Log.d("-------", "persona " + i + " puesta img: "+imagenStirng);
                    i++;

                }
                fout.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void GuardarArray(Context context) {

        try {
            File f = new File(context.getFilesDir(), "data.dat");
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            for (int i = 0; i < ListRanking.size(); i++) {
                oos.writeUTF(ListRanking.get(i).nom);
                oos.writeInt(ListRanking.get(i).qualificacio);
                oos.writeUTF(ListRanking.get(i).getFotoRanking().toString());
                Log.d("-------", "persona " + i + " puesta long de array: " + ListRanking.size());
            }
            oos.flush();
            fout.getFD().sync();
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() +
                                ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
