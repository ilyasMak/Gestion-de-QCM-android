package com.example.qcm;
import android.Manifest ;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.qcm.databinding.ProfspaceBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class profspace extends AppCompatActivity {


    private ProfspaceBinding binding;
    static boolean fromwhre = false ;
    TextView NomProf, specProf;
    ImageView imageProf ;
    int id_Prof;
    ListView mesQcms ;
    DataBaseManager db ;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //----------------------------------------------------
        super.onCreate(savedInstanceState);
        db = new DataBaseManager(this,"QCMs",null , 1);

        binding = ProfspaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imageProf = headerView.findViewById(R.id.IVimageProf);
        NomProf = headerView.findViewById(R.id.TVnomprof);
        specProf = headerView.findViewById(R.id.TVspecProf);
        mesQcms = findViewById(R.id.LVmesQcms);
        if(fromwhre==true){
            Bundle b = getIntent().getExtras();
            id_Prof = b.getInt("id");
            NomProf.setText(b.getString("Nom"));
            specProf.setText(b.getString("Spec"));
        }else{
            id_Prof=loginProfs.shp.getInt("id",-1);
            NomProf.setText(loginProfs.shp.getString("Nom","**"));
            specProf.setText(loginProfs.shp.getString("Spec","**"));
        }
     //   byte[] image = db.getPhotoProf(id_Prof);
       /* if (imageprof!= null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageprof, 0, imageprof.length);
            imageProf.setImageBitmap(bitmap);
        }*/


        setSupportActionBar(binding.appBarProfspace.toolbar);

        binding.appBarProfspace.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createQCM = new Intent(profspace.this, CreateQCM.class);
                Bundle sendId = new Bundle();
                sendId.putInt("id_Prof", id_Prof);
                createQCM.putExtras(sendId);
                startActivity(createQCM);
                finish();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_slideshow) {
                    Intent deconnecter = new Intent(profspace.this, loginProfs.class);
                    loginProfs.shp.edit().clear().commit();
                    startActivity(deconnecter);
                    finish();
                    return true;
                }
                if( id == R.id.nav_homei){
                   // db.supprimerProfAvecQCM(id_Prof);
                    loginProfs.shp.edit().clear().commit();
                    startActivity(new Intent(profspace.this,loginProfs.class));
                    finish();
                    return true ;
                }
                return false;
            }
        });
        //------------------------------------------






        //-----------------------------------------
        ArrayList<QCM> qcms = db.getQCM(id_Prof);
        mesQcms.setAdapter(new listQCMs(qcms));
        System.out.println(id_Prof);

    }
    //-----------------------------------------------------------------
    private void checkStoragePermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Si la permission est déjà accordée, ouvrir la galerie d'images
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission READ_EXTERNAL_STORAGE accordée, ouvrir la galerie d'images
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission refusée. Vous ne pouvez pas choisir d'image sans cette permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                if (inputStream != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    byte[] imageBytes = outputStream.toByteArray();
                    if (imageBytes != null && imageBytes.length > 0) {
                        // Insertion de l'image dans la base de données
                        System.out.println(imageBytes);
                        db.insererPhotoProf(id_Prof, imageBytes);
                    } else {
                        // Gérer le cas où l'image est vide
                        Toast.makeText(this, "L'image sélectionnée est vide.", Toast.LENGTH_SHORT).show();
                    }
                    inputStream.close();
                } else {
                    // Gérer le cas où l'inputStream est null
                    Toast.makeText(this, "Impossible de charger l'image sélectionnée.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer les exceptions liées à la lecture de l'image
                Toast.makeText(this, "Une erreur s'est produite lors du traitement de l'image.", Toast.LENGTH_SHORT).show();
            }
        }
    }












    //-----------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.profspace, menu);
        return true;
    }
    class listQCMs extends BaseAdapter{
        ArrayList<QCM> QCMs=new ArrayList<>();
        public listQCMs(  ArrayList<QCM>  q){
            this.QCMs= q ;
        }


        @Override
        public int getCount() {
            return QCMs.size();
        }

        @Override
        public Object getItem(int position) {
            return QCMs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listqsms, null);
            TextView titre = convertView.findViewById(R.id.TVtitreQCM);
            TextView nQsts = convertView.findViewById(R.id.TVnQsts);
            TextView tQst = convertView.findViewById(R.id.TVtQst);
            TextView codePass = convertView.findViewById(R.id.TVcodeQCM);
            titre.setText(QCMs.get(position).Titre);
            nQsts.setText(QCMs.get(position).Nombre_qst+" questions");
            tQst.setText(QCMs.get(position).Temps_qst+"s par question");
            codePass.setText("code Pass : "+QCMs.get(position).id_QCM);
            //-------------------------------------
            ImageButton deleteQCM = convertView.findViewById(R.id.IBdeleteQCM);
            deleteQCM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = QCMs.get(position).id_QCM;
                    QCMs.remove(position);
                    notifyDataSetChanged();
                    db.SupprimerQSM(id);

                }
            });
            ImageButton voirEtud = convertView.findViewById(R.id.IBseeStudents);
            voirEtud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<Object[]> resu = db.getQCM_PasserInfo(QCMs.get(position).id_QCM);
                    if(resu==null){
                        Toast.makeText(profspace.this,"Aucun personne a passer le QCM",Toast.LENGTH_LONG).show();

                    }else {

                        Intent it = new Intent(profspace.this, StudensTheyPassedQCM.class) ;
                        Bundle bundl = new Bundle();
                        bundl.putInt("id_QCM",QCMs.get(position).id_QCM);
                        it.putExtras(bundl);
                        startActivity(it);
                    }


                }
            });


            return convertView;
        }

    }

}


