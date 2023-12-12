package com.example.qcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PasserQCM extends Activity {
    TextView Question , numeroQuestion ;

    CheckBox A,B,C,D ;
    Button next ;

    ArrayList<QST> Questions ;
    DataBaseManager db ;
    int i = 1 , Note=0 ,id_QCM, id_Etud;
    String answer="" ;
    ArrayList<String> answers = new ArrayList<>();
    private Handler handler = new Handler();
    boolean boolA= false ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passerqcm);
        db = new DataBaseManager(this,"QCMs",null , 1);
       Question = findViewById(R.id.ETqstpass);
        A=findViewById(R.id.CBA);
        B=findViewById(R.id.CBB);
        C=findViewById(R.id.CBC);
        D=findViewById(R.id.CBD);
        numeroQuestion= findViewById(R.id.TVtitrePass);
        next = findViewById(R.id.BNextpass);
        Bundle bun= getIntent().getExtras();

        id_QCM= bun.getInt("id_QCM");
        id_Etud= bun.getInt("id_Etud");
       Questions = db.getQSTs(id_QCM);
       numeroQuestion.setText("Question 1");
        Question.setText(Questions.get(0).Question);
        A.setText(Questions.get(0).A);
        B.setText(Questions.get(0).B);
        C.setText(Questions.get(0).C);
        D.setText(Questions.get(0).D);


        //-----------------------
        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (A.isChecked() && !answer.contains("A") ) {
                    answer=answer+"A";

                }else {
                    answer = answer.replace("A","");
                }
            }
        });
        //-----------------------
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (B.isChecked() && !answer.contains("B") ) {
                    answer=answer+"B";

                }else {
                    answer = answer.replace("B","");
                }
            }
        });
        //-----------------------
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (C.isChecked() && !answer.contains("C") ) {
                    answer=answer+"C";

                }else {
                    answer = answer.replace("C","");
                }
            }
        });
        //-----------------------
        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (D.isChecked() && !answer.contains("D") ) {
                    answer=answer+"D";

                }else {
                    answer = answer.replace("D","");
                }
            }
        });

        // ----------------------------
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next.performClick();
                    }
                }, db.getTempsQstForQCM(id_QCM)*1000);

                if(i<Questions.size()){
                    A.setChecked(false);    B.setChecked(false);    C.setChecked(false);    D.setChecked(false);

                    //-----------------------
                    char[] charArray = answer.toCharArray();
                    Arrays.sort(charArray);
                   answer = new String(charArray);
                    //----------------------
                    answers.add(answer);
                    answer="";



                    numeroQuestion.setText("Question "+(i+1));
                Question.setText(Questions.get(i).Question);
                A.setText(Questions.get(i).A);
                B.setText(Questions.get(i).B);
                C.setText(Questions.get(i).C);
                D.setText(Questions.get(i).D);

                i++ ;



                }else {
                    char[] charArray = answer.toCharArray();
                    Arrays.sort(charArray);
                    answer = new String(charArray);
                    answers.add(answer);

                    //------------------------------------------------------------


                    for(int comp =0 ; comp< answers.size();comp++){
                            if(answers.get(comp).equals(Questions.get(comp).Correction))  Note++ ;
                    }

                    //------------------------------------------------------------
                    db.chengerNoteIncrementerFois(id_Etud,id_QCM,Note);
                    System.out.println(Note);
                    startActivity(new Intent(PasserQCM.this, studentspace.class));
                    finish();
                }
            }
        });


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                next.performClick();
            }
        }, db.getTempsQstForQCM(id_QCM)*1000);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
