package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsererQct extends Activity {

    int id_QSM , n_Qsts ;

    EditText Qst ,a,b,c,d,corr ;
   TextView NumeroDeQuestion ;
    Button next ;
    DataBaseManager db;
    int compt = 0 ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insererqst);
        NumeroDeQuestion=findViewById(R.id.TVtitreInsererQST);
        Bundle bun= getIntent().getExtras();
        id_QSM = bun.getInt("id_QSM");
        n_Qsts=bun.getInt("n_QSTs");
        db = new DataBaseManager(this,"QCMs",null , 1);
        //--------------------------
        Qst = findViewById(R.id.ETqst);
        a= findViewById(R.id.ETA);
        b= findViewById(R.id.ETB);
        c= findViewById(R.id.ETC);
        d= findViewById(R.id.ETD);
        corr= findViewById(R.id.ETcor);
        next =findViewById(R.id.Bnext);
        //------------------------

        //-----------------------------
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Question = Qst.getText().toString() ;
                String A =a.getText().toString();
                String B =b.getText().toString();
                String C = c.getText().toString();
                String D = d.getText().toString();
                String Correction = corr.getText().toString() ;
                if(Question.equals("") || A.equals("")||B.equals("")||C.equals("")||D.equals("")||Correction.equals("")){
                    Toast.makeText(InsererQct.this, "Remplire tous les champs !", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.InsererQST(Question,A,B,C,D,Correction,id_QSM);
                    compt++ ;
                    Qst.setText("");a.setText("");b.setText("");c.setText("");d.setText("");corr.setText("");
                    NumeroDeQuestion.setText("Question "+(compt+1));
                    if(compt ==n_Qsts ){
                        startActivity(new Intent(InsererQct.this,profspace.class));
                    }
                }
            }
        });

    }
}
