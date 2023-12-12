package com.example.qcm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



import com.example.qcm.databinding.ProfspaceBinding;

import com.example.qcm.databinding.StudentspaceBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class studentspace extends AppCompatActivity {
   TextView NomEtud ,specEtud ;
    private AppBarConfiguration mAppBarConfiguration;
    private StudentspaceBinding binding;
    ArrayList<QCM> QCMsPasser =new ArrayList<>();
    DataBaseManager db ;
    int id_Etud;
    ListView mesQcmsApasser;
    listQCMsApasser adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StudentspaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DataBaseManager(this,"QCMs",null , 1);
       // db.clearDatabase();
        NavigationView navigationView = findViewById(R.id.nav_view2);
        View headerView = navigationView.getHeaderView(0);
        NomEtud = headerView.findViewById(R.id.TVnomEtudspace);
        specEtud = headerView.findViewById(R.id.TVspecEtudSpace);
       mesQcmsApasser= findViewById(R.id.LVmesQcmsApasser);
       id_Etud=loginStud.shp.getInt("id",-1);
        NomEtud.setText(loginStud.shp.getString("Nom","**"));
        specEtud.setText(loginStud.shp.getString("Spec","**"));


        setSupportActionBar(binding.appBarStudentspace.toolbar);
        binding.appBarStudentspace.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder demanceCodeQcm = new AlertDialog.Builder(studentspace.this)
                        .setTitle("Entrer le code passe de QCM");
                final EditText inputcodePass = new EditText(studentspace.this);
                inputcodePass.setInputType(InputType.TYPE_CLASS_NUMBER);
                demanceCodeQcm.setView(inputcodePass)
                        .setPositiveButton(getResources().getString(R.string.search), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if( inputcodePass.getText().toString().equals("")){
                                    Toast.makeText(studentspace.this, getResources().getString(R.string.passcode), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    int id_QCM = Integer.parseInt( inputcodePass.getText().toString()) ;

                                    QCM Q=db.getQCMparIdQcm(id_QCM);
                                    if(Q==null){
                                        Toast.makeText(studentspace.this, getResources().getString(R.string.MCQnotExist), Toast.LENGTH_SHORT).show();
                                    }else {

                                    //  QCM q=  db.getQCMparIdQcm(id_QCM);
                                         db.InsererQCM_Passer(id_Etud,id_QCM,0,0);

                                        QCMsPasser = db.getQCMdeEtudiant(id_Etud);
                                        adapter = new listQCMsApasser(QCMsPasser);

                                        mesQcmsApasser.setAdapter(adapter );


                                     // QCMsPasser.add(q);

                                    //ArrayList<QCM_Passer> qcms = db.getQCM_Passer(id_Etud);
                                        //System.out.println(id_Etud);
                                        //System.out.println(qcms.get(0).id_QCM);



                                    }

                                }

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });



       DrawerLayout drawer = binding.drawerLayout;

  navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_slideshow2) {
                    // C'est l'item "Deconnecter", vous pouvez démarrer une autre activité ici.
                    Intent deconnecter = new Intent(studentspace.this, loginStud.class);
                    loginStud.shp.edit().clear().commit();
                    startActivity(deconnecter);
                    finish();
                    return true; // Indique que l'événement a été géré
                }if(id==R.id.deleteEtud) {
                      db.supprimerEtudiant(id_Etud);
                    loginStud.shp.edit().clear().commit();
                    startActivity(new Intent(studentspace.this,loginStud.class));
                    finish();
                    return true ;
                }
                return false; // Indique que l'événement n'a pas été géré pour les autres éléments
            }
        });
        QCMsPasser = db.getQCMdeEtudiant(id_Etud);
        if(QCMsPasser!=null){
            adapter = new listQCMsApasser(QCMsPasser);

            mesQcmsApasser.setAdapter(adapter );
        }

    }
   //--------------------------------------------------------------------
    class listQCMsApasser extends BaseAdapter{
         ArrayList<QCM> QCMsApasser= new ArrayList<>() ;
         public listQCMsApasser(ArrayList<QCM> qs){
             this.QCMsApasser=qs ;
         }
       @Override
       public int getCount() {
          return  QCMsApasser.size();

       }

       @Override
       public Object getItem(int position) {
           return QCMsApasser.get(position);
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.listqsmsapasser, null);
           TextView titre = convertView.findViewById(R.id.TVtitreQCMEtud);
            TextView n_passer = convertView.findViewById(R.id.TVnPasser);
            TextView note = convertView.findViewById(R.id.TVnote);
           ImageButton passerQCM = convertView.findViewById(R.id.BpasserQCM);
           TextView id_prof = convertView.findViewById(R.id.TVidProfetudspace);
           TextView id_QCM = convertView.findViewById(R.id.TVcodeQCMEtud);
          titre.setText(QCMsApasser.get(position).Titre);
          id_QCM.setText("code QCM "+Integer.toString(QCMsApasser.get(position).id_QCM));
          id_prof.setText("Code Prof "+Integer.toString(QCMsApasser.get(position).id_Prof));
          int [] Note_fois = db.getNote_nP_QCMpasser(id_Etud,QCMsApasser.get(position).id_QCM);
           n_passer.setText("Note "+Note_fois[0]);
           note.setText("Passer "+Note_fois[1]+" fois");
          passerQCM.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 Bundle bun =new Bundle();
                  bun.putInt("id_QCM",QCMsApasser.get(position).id_QCM);
                  bun.putInt("id_Etud",id_Etud);
                 Intent passerQCM = new Intent(studentspace.this, PasserQCM.class);
                  passerQCM.putExtras(bun);
                  startActivity(passerQCM);
                  finish();
              }
          });
           return convertView;
       }

   }

    //-------------------------------------------------------------------

}

