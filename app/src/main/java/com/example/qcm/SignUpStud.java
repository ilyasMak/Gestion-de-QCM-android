package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpStud extends AppCompatActivity {
    EditText username , Spec , pass , passrep ;
    Button SignUpStud  ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signupstud);

        username= findViewById(R.id.ETusersignupStud);
        Spec = findViewById(R.id.ETspecSignUpStud) ;
        pass= findViewById(R.id.ETpasswordSingUpStud);
        passrep = findViewById(R.id.ETpasswordSingUpStudRep)    ;
        SignUpStud=findViewById(R.id.BsignUpStud);
        DataBaseManager db = new DataBaseManager(this,"QCMs",null , 1);
        //----------------------------------------------------------------
        SignUpStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = pass.getText().toString();
                String inputPasswordRep = passrep.getText().toString();
                if(username.getText().toString().equals("")|| Spec.getText().toString().equals("")||inputPassword.equals("")||inputPasswordRep.equals("")){
                    Toast.makeText(SignUpStud.this, getResources().getString(R.string.vide), Toast.LENGTH_LONG).show();
                }
                else {
                if (!inputPassword.equals(inputPasswordRep)) {
                    Toast.makeText(SignUpStud.this, getResources().getString(R.string.verfpass), Toast.LENGTH_LONG).show();
                }else {
                    Etudiant existingStud = db.getEtudiant(username.getText().toString(),inputPassword );
                    if (existingStud!= null) {
                        Toast.makeText(SignUpStud.this, getResources().getText(R.string.useother), Toast.LENGTH_LONG).show();
                    }else {
                        db.InsererEtudiant(username.getText().toString(), Spec.getText().toString(),inputPassword);
                        Intent it = new Intent(SignUpStud.this , loginStud.class);
                        startActivity(it);
                        finish();

                    }
                }}
            }
        });
    }
}
