package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupProf extends AppCompatActivity {

    EditText username , spec , password , passwordRep ;
    Button signUp ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuprof);
        username = findViewById(R.id.ETsuUsername);
        spec = findViewById(R.id.ETsuSpeci);
        password= findViewById(R.id.ETsupass);
        passwordRep = findViewById(R.id.ETsupassRep);
        signUp = findViewById(R.id.BsignUpProf);
        DataBaseManager db = new DataBaseManager(this,"QCMs",null , 1);
        //------------------------------------------------------------------------
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = password.getText().toString();
                String inputPasswordRep = passwordRep.getText().toString();

                if (!inputPassword.equals(inputPasswordRep)) {
                    Toast.makeText(SignupProf.this, getResources().getString(R.string.verfpass), Toast.LENGTH_LONG).show();
                } else {
                    // Vérifier si le mot de passe existe déjà dans la base de données
                    Prof existingProf = db.getProf(username.getText().toString(), inputPassword);

                    if (existingProf != null) {
                        Toast.makeText(SignupProf.this, getResources().getText(R.string.useother), Toast.LENGTH_LONG).show();
                    } else {
                       db.InsererProf(username.getText().toString(), spec.getText().toString(),inputPassword);
                       Intent it = new Intent(SignupProf.this , loginProfs.class);
                       startActivity(it);


                    }
                }
            }
        });
        //-----------------------------------------------------------
    }


}
