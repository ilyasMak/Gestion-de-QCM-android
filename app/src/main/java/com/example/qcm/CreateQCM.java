package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateQCM extends Activity {
    Button BcreateQCM ;
    Intent insererQct ;
    EditText titre , n_qst , t_qst ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creeqcm);
        BcreateQCM = findViewById(R.id.BcreateQCM);
        insererQct= new Intent(this , InsererQct.class);
        Bundle recepId= getIntent().getExtras();
        int id_Prof = recepId.getInt("id_Prof");
        DataBaseManager db = new DataBaseManager(this,"QCMs",null , 1);
        titre =findViewById(R.id.ETtitreQCMcree);
        n_qst = findViewById(R.id.ETnQSTscree);
        t_qst = findViewById(R.id.ETtQSTsCree);
        //-------------------------------------------------
        BcreateQCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String title = titre.getText().toString();



               if(titre.equals("") || n_qst.getText().toString().equals("") || t_qst.getText().toString().equals("")){
                    Toast.makeText(CreateQCM.this,"remplir tous les champs !",Toast.LENGTH_LONG).show();
                }else {
                  int n = Integer.parseInt(n_qst.getText().toString());
                  int t = Integer.parseInt(t_qst.getText().toString());
                  //profspace.fromwhre=false ;
                  db.InsererQCM(n,title,t,id_Prof);
                 Bundle sendtoqst= new Bundle() ;
                  int idqsm = db.getIdQCM(title , n,t,id_Prof);
                 sendtoqst.putInt("id_QSM",idqsm);
                  sendtoqst.putInt("n_QSTs",n);
                  insererQct.putExtras(sendtoqst);
                  startActivity(insererQct);
                  finish();

                }
            }
        });
    }
}

