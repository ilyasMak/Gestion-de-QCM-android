package com.example.qcm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class StudensTheyPassedQCM extends Activity {
    DataBaseManager db ;
    int id_QCM , Note , N_passer ,Nombre_QSTs;
    ListView student ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studenstheypassedqcm);
        student = findViewById(R.id.LVStudentTheyPassedQCM);
        db = new DataBaseManager(this,"QCMs",null , 1);
        Bundle b= getIntent().getExtras();
         id_QCM = b.getInt("id_QCM");
        ArrayList<Object[]> resu = db.getQCM_PasserInfo(id_QCM);
        Nombre_QSTs = db.getNombreQstForQCM(id_QCM);
       student.setAdapter(new listStudents(resu));

       //  System.out.println("sds"+resu.get(0)[0].toString());

    }


    class listStudents extends BaseAdapter{
        ArrayList<Object[]> infosStud1 ;
          public listStudents(ArrayList<Object[]> studs){
              this.infosStud1 = studs ;
          }
        @Override
        public int getCount() {
            return infosStud1.size();
        }

        @Override
        public Object getItem(int position) {
            return infosStud1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.liststudent, null);
            TextView NomEtud = convertView.findViewById(R.id.TVnomStudPassqcm);
            TextView id_etud = convertView.findViewById(R.id.TVidStudentPassQCM);
            TextView spec = convertView.findViewById(R.id.TVspecStudentPassQCM);
            TextView Note = convertView.findViewById(R.id.TVNoteStudentPassQCM);
            TextView n_passer = convertView.findViewById(R.id.TVNombrefoisStudentPassQCM);
            id_etud.setText("ID "+infosStud1.get(position)[0].toString());
            Note.setText("Note "+infosStud1.get(position)[1].toString()+"/"+Nombre_QSTs);
            //
            int nt = Integer.parseInt(infosStud1.get(position)[1].toString());


            if (nt / Nombre_QSTs < 0.5) {
                // Background vert pour les éléments pairs
                convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.purple_700));
            } else {
                // Background rouge pour les éléments impairs
                convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.purple_500));
            }

            //-----------------------------------
            n_passer.setText("Passer "+infosStud1.get(position)[2].toString()+" fois");

            String[] supInfos = db.getEtudiantInfo(Integer.parseInt(infosStud1.get(position)[0].toString()));
            NomEtud.setText(supInfos[0]);
            spec.setText(supInfos[1]);


            return convertView;
        }
    }
}
