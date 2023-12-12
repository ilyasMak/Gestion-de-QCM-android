package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.qcm.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginProfs extends AppCompatActivity {
    TextView TVSignUpProf,TVrevenirMain ;
    EditText username , password  ;
    Intent SignUpProf ,profSpace,Main ;
    Button BloginProf ;
    public static SharedPreferences shp ;
    private DataBaseManager dataBaseManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginprofs);
        TVSignUpProf=findViewById(R.id.TVSignUpProf);
        SignUpProf=new Intent(this ,SignupProf.class) ;
        BloginProf=findViewById(R.id.BloginProf);
        profSpace=new Intent(this , profspace.class);
        username = findViewById(R.id.ETloginUsernameP);
        password= findViewById(R.id.ETlginPasswordP) ;
        TVrevenirMain=findViewById(R.id.TVrevenirMain);
        DataBaseManager db = new DataBaseManager(this,"QCMs",null , 1);
        Main = new Intent (this,MainActivity.class);
        shp = getPreferences(MODE_PRIVATE);
        int etat = shp.getInt("id",-1);
        if(etat!=-1){
            startActivity(profSpace);
            finish();
        }
        //------------------------------------------
        TVSignUpProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignUpProf);
            }
        });
        //-------------------------------------------
        TVrevenirMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(Main); finish(); }
        });
        //----------------------------------------------------
        BloginProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.fromwhere = false;
                Prof existingProf = db.getProf(username.getText().toString(), password.getText().toString());

                if (existingProf == null) {
                    Toast.makeText(loginProfs.this, getResources().getString(R.string.incorrect), Toast.LENGTH_LONG).show();
                } else {
                    profspace.fromwhre=true ;
                  Bundle b= new Bundle() ;
                    b.putInt("id",existingProf.id_prof);
                    b.putString("Nom",existingProf.Nom_complet);
                    b.putString("Spec",existingProf.Specialite);
                    profSpace.putExtras(b);
                    shp.edit().putInt("id", existingProf.id_prof).apply();
                    shp.edit().putString("Nom", existingProf.Nom_complet).apply();
                    shp.edit().putString("Spec", existingProf.Specialite).apply();


                    startActivity(profSpace);
                    finish();
                }
            }  });
    }

}

