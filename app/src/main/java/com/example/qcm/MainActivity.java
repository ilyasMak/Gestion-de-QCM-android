package com.example.qcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button YouAreProf,YouAreStud ;
    Intent prof , student ;
    DataBaseManager db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBaseManager(this,"QCMs",null , 1);
       // loginStud.shp.edit().clear().commit();
       // loginProfs.shp.edit().clear().commit();
        // db.clearDatabase();
        YouAreProf = findViewById(R.id.YouAreProf);
        YouAreStud= findViewById(R.id.YouStud);
        prof = new Intent(this , loginProfs.class);
        student = new Intent(this , loginStud.class);

        //------------------------------------------------------------------------------------
        YouAreProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(prof);
                finish();
            }
        });
        //------------------------------------------------------------------------------------
        YouAreStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(student);
               finish();
            }
        });
    }
}