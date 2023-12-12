package com.example.qcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qcm.ui.home.HomeFragment;

public class loginStud extends AppCompatActivity {
    TextView TVSignUpStud , revenirMain ;
    EditText username , pass ;
    Intent SignUpstud ,SpaceStudent , Main;
    Button BlogiStud ;
    public static SharedPreferences shp ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginstud);
        TVSignUpStud=findViewById(R.id.TVSignUpStud);
        SignUpstud=new Intent(this,SignUpStud.class);
        BlogiStud=findViewById(R.id.BloginStud);
        SpaceStudent=new Intent(this , studentspace.class);
        revenirMain = findViewById(R.id.TVrevenirMainStud);
        username = findViewById(R.id.ETusernameEtudLogin);
        pass = findViewById(R.id.ETpasswordloginStud);
        DataBaseManager db = new DataBaseManager(this,"QCMs",null , 1);
        shp = getPreferences(MODE_PRIVATE);
        int etat = shp.getInt("id",-1);
        if(etat!=-1){
            startActivity(SpaceStudent);
            finish();
        }
        Main = new Intent (this,MainActivity.class);
        //-------------------------------------------------
        TVSignUpStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignUpstud);
            }
        });
        //---------------------------------------------------
        revenirMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Main); finish();
            }
        });
        //-----------------------------------------------
        BlogiStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.fromwhere=true ;
                Etudiant existingStudent = db.getEtudiant(username.getText().toString(),pass.getText().toString());


                if (existingStudent == null) {
                    Toast.makeText(loginStud.this, getResources().getString(R.string.incorrect), Toast.LENGTH_LONG).show();
                }else {
                    profspace.fromwhre=true ;
                    Bundle b= new Bundle() ;
                    b.putInt("id",existingStudent.id_Etud);
                    b.putString("Nom",existingStudent.Nom_complet);
                    b.putString("Spec",existingStudent.Specialite);
                    SpaceStudent.putExtras(b);
                    shp.edit().putInt("id", existingStudent.id_Etud).apply();
                    shp.edit().putString("Nom", existingStudent.Nom_complet).apply();
                    shp.edit().putString("Spec", existingStudent.Specialite).apply();


                    startActivity(SpaceStudent);
                    finish();
                }

            }
        });

    }
}
