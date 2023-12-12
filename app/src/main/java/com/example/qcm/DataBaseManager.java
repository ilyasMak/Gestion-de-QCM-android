package com.example.qcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataBaseManager extends SQLiteOpenHelper {


    public DataBaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Prof (id_prof INTEGER PRIMARY KEY AUTOINCREMENT, Password TEXT NOT NULL, Nom_complet TEXT NOT NULL, Specialite TEXT NOT NULL, Image BLOB);");


        db.execSQL("CREATE TABLE QCM (id_QCM INTEGER PRIMARY KEY AUTOINCREMENT, Nombre_qst INTEGER NOT NULL, Titre TEXT NOT NULL, Temps_qst INTEGER NOT NULL, id_Prof INTEGER, FOREIGN KEY (id_Prof) REFERENCES Prof(id_prof) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE QST (id_QST INTEGER PRIMARY KEY AUTOINCREMENT, Question TEXT NOT NULL, A TEXT NOT NULL, B TEXT NOT NULL, C TEXT NOT NULL, D TEXT NOT NULL, Correction TEXT, id_QCM INTEGER NOT NULL, FOREIGN KEY (id_QCM) REFERENCES QCM(id_QCM) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE Etudiant (id_Etud INTEGER PRIMARY KEY AUTOINCREMENT, Password TEXT NOT NULL, Nom_complet TEXT NOT NULL, Specialite TEXT NOT NULL);");
        db.execSQL("CREATE TABLE QCM_Passer (id_Etud INTEGER , id_QCM INTEGER, Note INTEGER, Nombre_passer INTEGER , FOREIGN KEY (id_Etud) REFERENCES Etudiant(id_Etud) ON DELETE CASCADE, FOREIGN KEY (id_QCM) REFERENCES QCM(id_QCM) ON DELETE CASCADE);");
    }


    //----------------------------------------------------------
    public String[] getEtudiantInfo(int id_Etud) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] result = new String[2]; // Un tableau pour stocker le nom complet et la spécialité

        String query = "SELECT Nom_complet, Specialite FROM Etudiant WHERE id_Etud = ?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id_Etud)});

        if (cursor.moveToFirst()) {
            String nomComplet = cursor.getString(cursor.getColumnIndex("Nom_complet"));
            String specialite = cursor.getString(cursor.getColumnIndex("Specialite"));
            result[0] = nomComplet;
            result[1] = specialite;
        }

        cursor.close();

        return result;
    }


    public void supprimerEtudiant(int id_Etud) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Supprimer l'étudiant de la table "Etudiant"
        String etudiantWhereClause = "id_Etud=?";
        String[] etudiantWhereArgs = {String.valueOf(id_Etud)};
        db.delete("Etudiant", etudiantWhereClause, etudiantWhereArgs);

        // Supprimer les enregistrements associés de la table "QCM_Passer"
        String qcmPasserWhereClause = "id_Etud=?";
        String[] qcmPasserWhereArgs = {String.valueOf(id_Etud)};
        db.delete("QCM_Passer", qcmPasserWhereClause, qcmPasserWhereArgs);

        // Fermez la base de données
        db.close();
    }


    public ArrayList<Object[]> getQCM_PasserInfo(int id_QCM) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Object[]> results = new ArrayList<>();

        String query = "SELECT id_Etud, Note, Nombre_passer FROM QCM_Passer WHERE id_QCM = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});

        while (cursor.moveToNext()) {
            int id_Etud = cursor.getInt(cursor.getColumnIndex("id_Etud"));
            int note = cursor.getInt(cursor.getColumnIndex("Note"));
            int nombre_passer = cursor.getInt(cursor.getColumnIndex("Nombre_passer"));

            Object[] result = {id_Etud, note, nombre_passer};
            results.add(result);
        }

        cursor.close();
        db.close();

        return results;
    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getTempsQstForQCM(int id_QCM) {
        SQLiteDatabase db = this.getReadableDatabase();
        int tempsQst = -1; // Valeur par défaut ou valeur pour indiquer une erreur

        String query = "SELECT Temps_qst FROM QCM WHERE id_QCM = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});

        if (cursor.moveToFirst()) {
            tempsQst = cursor.getInt(cursor.getColumnIndex("Temps_qst"));
        }

        cursor.close();
        db.close();

        return tempsQst;
    }
    public int getNombreQstForQCM(int id_QCM) {
        SQLiteDatabase db = this.getReadableDatabase();
        int NombreQst = -1; // Valeur par défaut ou valeur pour indiquer une erreur

        String query = "SELECT Nombre_qst FROM QCM WHERE id_QCM = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});


        if (cursor.moveToFirst()) {
            NombreQst = cursor.getInt(cursor.getColumnIndex("Nombre_qst"));
        }

        cursor.close();
        db.close();

        return NombreQst;
    }

    public byte[] getPhotoProf(int id_prof) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"Image"};
        String selection = "id_prof=?";
        String[] selectionArgs = {String.valueOf(id_prof)};

        Cursor cursor = db.query("Prof", columns, selection, selectionArgs, null, null, null);
        byte[] imageBytes = null;

        if (cursor.moveToFirst()) {
            int imageColumnIndex = cursor.getColumnIndex("Image");
            imageBytes = cursor.getBlob(imageColumnIndex);
        }

        cursor.close();
        db.close();

        return imageBytes;
    }
    public void insererPhotoProf(int id_prof, byte[] imageBytes) {

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(1);
       db.execSQL("UPDATE Prof SET Image = "+imageBytes+"  WHERE id_prof = "+id_prof+";");
        System.out.println(2);


        db.close();
    }

    public ArrayList<QCM> getQCMdeEtudiant(int id_etud){
        ArrayList<Integer> IdsQCMs = getidQCMdeEtud(id_etud);
        ArrayList<QCM> q = new ArrayList<>();
        for(int i =0 ; i<IdsQCMs.size();i++){
            q.add(getQCMparIdQcm(IdsQCMs.get(i)));
        }
        return q ;
    }

    public ArrayList<Integer> getidQCMdeEtud(int id_Etud) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> idQCMs = new ArrayList<>();

        String query = "SELECT id_QCM FROM QCM_Passer WHERE id_Etud = ? ORDER BY id_QCM DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_Etud)});

        while (cursor.moveToNext()) {
            int idQCM = cursor.getInt(cursor.getColumnIndex("id_QCM"));
            idQCMs.add(idQCM);
        }

        cursor.close();
        db.close();

        return idQCMs;
    }


    public int[] getNote_nP_QCMpasser(int id_etud, int id_QCM) {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] result = new int[2]; // Un tableau d'entiers pour stocker la note et le nombre de passages

        String query = "SELECT Note, Nombre_passer FROM QCM_Passer WHERE id_Etud = ? AND id_QCM = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_etud), String.valueOf(id_QCM)});

        if (cursor.moveToFirst()) {
            // L'enregistrement correspondant a été trouvé
            result[0] = cursor.getInt(cursor.getColumnIndex("Note")); // Récupérez la note
            result[1] = cursor.getInt(cursor.getColumnIndex("Nombre_passer")); // Récupérez le nombre de passages
        }

        cursor.close();
        db.close();

        return result;
    }
    //-----------------------------------------------------------------------
    public void chengerNoteIncrementerFois(int id_Etud, int id_QCM, int nouvelleNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Mettre à jour la colonne "Note" avec la nouvelle note
        values.put("Note", nouvelleNote);

        // Incrémenter la colonne "Nombre_passer" de 1
        db.execSQL("UPDATE QCM_Passer SET Nombre_passer = Nombre_passer + 1 WHERE id_Etud = ? AND id_QCM = ?",
                new String[] {String.valueOf(id_Etud), String.valueOf(id_QCM)});

        // Mettre à jour la ligne avec les nouvelles valeurs
        db.update("QCM_Passer", values, "id_Etud = ? AND id_QCM = ?",
                new String[] {String.valueOf(id_Etud), String.valueOf(id_QCM)});

        // Fermer la base de données
        db.close();
    }



    //-------------------------------------------------------------------
    public void InsererQCM_Passer(int id_etud, int id_qcm, int note, int n_fois) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Vérifier si l'enregistrement existe déjà
        String query = "SELECT * FROM QCM_Passer WHERE id_Etud = ? AND id_QCM = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_etud), String.valueOf(id_qcm)});

        if (cursor.getCount() == 0) {
            // L'enregistrement n'existe pas, nous pouvons l'insérer
            ContentValues values = new ContentValues();
            values.put("id_Etud", id_etud);
            values.put("id_QCM", id_qcm);
            values.put("Note", note);
            values.put("Nombre_passer", n_fois);

            long result = db.insert("QCM_Passer", null, values);

            if (result == -1) {
                Log.e("Insertion QCM_Passer", "Échec de l'insertion");
                System.out.println("Échec de l'insertion de QCM_Passer");
            } else {
                Log.d("Insertion QCM_Passer", "Insertion réussie");
                System.out.println("QCM_Passer est inséré");
            }
        } else {
            // L'enregistrement existe déjà, ne rien faire ou gérer le cas selon vos besoins
            Log.d("Insertion QCM_Passer", "L'enregistrement existe déjà");
            System.out.println("L'enregistrement QCM_Passer existe déjà");
        }

        cursor.close();
        db.close();
    }


    //--------------------------------------------------------------------------------
    public ArrayList<QCM_Passer> getQCM_Passer(int id_etud){
        System.out.println(1);
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println(2);
        String query = "SELECT * FROM QCM_Passer WHERE id_Etud = ? ORDER BY id_QCM DESC";
        System.out.println(3);
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(id_etud)});

        System.out.println(4);
        // Initialisez à null par défaut
        ArrayList<QCM_Passer> QCMs = new ArrayList<>();
        System.out.println(5);
        while (cur.moveToNext()) {
            System.out.println(6);
            int id_etudd = cur.getInt(cur.getColumnIndex("id_Etud"));
            int id_qcm = cur.getInt(cur.getColumnIndex("id_QCM"));
            int nt = cur.getInt(cur.getColumnIndex("Note"));
            int np = cur.getInt(cur.getColumnIndex("Nombre_passer"));
            QCMs.add(new QCM_Passer(id_etudd, id_qcm, nt,np));
        }

        cur.close();
        db.close();
        return QCMs;
    }

    //--------------------------------------------------------------------------------
    public void InsererProf(String Nom, String spec, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO Prof (Nom_complet, Specialite, Password) VALUES ('" + Nom + "', '" + spec + "', '" + pass + "');";

        db.execSQL(query);
        db.close();
    }
    public void InsererEtudiant(String nom , String spec , String pass){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO Etudiant (Nom_complet, Specialite, Password) VALUES ('" + nom + "', '" + spec + "', '" + pass + "');";

        db.execSQL(query);
        Log.d("Insertion", "Nouvel étudiant inséré : Nom = " + nom + ", Specialite = " + spec + ", Password = " + pass);
        db.close();
    }
    //---------------------------------------------------------------------


    //--------------------------------------------------------------------
    public Etudiant getEtudiant(String nom, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Etudiant e = null; // Initialisez à null par défaut

        String query = "SELECT * FROM Etudiant WHERE Nom_complet = ? AND Password = ?";
        Cursor cur = db.rawQuery(query, new String[]{nom, pass});

        if (cur.moveToFirst()==true) {
            int id = cur.getInt(cur.getColumnIndex("id_Etud"));
            String name = cur.getString(cur.getColumnIndex("Nom_complet"));
            String password = cur.getString(cur.getColumnIndex("Password"));
            String spec = cur.getString(cur.getColumnIndex("Specialite"));
            e = new Etudiant(id, name, spec, password);
        }

        cur.close();
        db.close();
        return e;
    }

    public Prof getProf(String nom, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Prof WHERE Nom_complet = ? AND Password = ?";
        Cursor cur = db.rawQuery(query, new String[]{nom, pass});

        Prof p = null; // Initialisez à null par défaut

        if (cur.moveToFirst()==true) {
            int id = cur.getInt(cur.getColumnIndex("id_prof"));
            String name = cur.getString(cur.getColumnIndex("Nom_complet"));
            String password = cur.getString(cur.getColumnIndex("Password"));
            String spec = cur.getString(cur.getColumnIndex("Specialite"));
            p = new Prof(id, name, spec, password);
        }

        cur.close();
        db.close();
        return p;
    }
    //-----------------------------------------------------------
    public void InsererQCM( int Nombre_qst , String Titre , int Temps_qst , int id_Prof){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO QCM ( Nombre_qst, Titre,Temps_qst,id_Prof) VALUES ( '" + Nombre_qst + "', '" + Titre+ "','" +Temps_qst+ "','" + id_Prof+ "');";

        db.execSQL(query);
        db.close();
    }
    //----------------------------------------------------------
    public  ArrayList<QCM> getQCM(int id_Prof){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM QCM WHERE id_Prof = ? ORDER BY id_QCM DESC";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(id_Prof)});

       // Initialisez à null par défaut
        ArrayList<QCM> QCMs = new ArrayList<>();
        while (cur.moveToNext()) {
            int id_qcm = cur.getInt(cur.getColumnIndex("id_QCM"));
            int n_qst = cur.getInt(cur.getColumnIndex("Nombre_qst"));
            String titre = cur.getString(cur.getColumnIndex("Titre"));
            int t_qst = cur.getInt(cur.getColumnIndex("Temps_qst"));
            int id_prof = cur.getInt(cur.getColumnIndex("id_Prof"));
            QCMs.add(new QCM(id_qcm, titre, n_qst, t_qst, id_prof));
        }

        cur.close();
        db.close();
        return QCMs;
    }
    //-----------------------------------------------------------
    public  ArrayList<QCM> getQCMbyIdQCM(int id_QCM){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM QCM WHERE id_QCM = ? ORDER BY id_QCM DESC";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});

        // Initialisez à null par défaut
        ArrayList<QCM> QCMs = new ArrayList<>();
        while (cur.moveToNext()) {
            int id_qcm = cur.getInt(cur.getColumnIndex("id_QCM"));
            int n_qst = cur.getInt(cur.getColumnIndex("Nombre_qst"));
            String titre = cur.getString(cur.getColumnIndex("Titre"));
            int t_qst = cur.getInt(cur.getColumnIndex("Temps_qst"));
            int id_prof = cur.getInt(cur.getColumnIndex("id_Prof"));
            QCMs.add(new QCM(id_qcm, titre, n_qst, t_qst, id_prof));
        }

        cur.close();
        db.close();
        return QCMs;
    }
    //---------------------------------------------------------
    public QCM getQCMparIdQcm(int id_QCM){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM QCM WHERE id_QCM = ? ";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});

        QCM q =null ;
        if (cur.moveToFirst())  {
            int id_qcm = cur.getInt(cur.getColumnIndex("id_QCM"));
            int n_qst = cur.getInt(cur.getColumnIndex("Nombre_qst"));
            String titre = cur.getString(cur.getColumnIndex("Titre"));
            int t_qst = cur.getInt(cur.getColumnIndex("Temps_qst"));
            int id_prof = cur.getInt(cur.getColumnIndex("id_Prof"));
           q= new QCM(id_qcm, titre, n_qst, t_qst, id_prof);
        }

        return q ;
    }
    //----------------------------------------------------------
    public int getIdQCM(String titre, int n, int t, int prof) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id_QCM FROM QCM WHERE Titre = ? AND Nombre_qst = ? AND Temps_qst = ? AND id_Prof = ?";
        Cursor cur = db.rawQuery(query, new String[]{titre, String.valueOf(n), String.valueOf(t), String.valueOf(prof)});

        if (cur.moveToFirst()) {
            int id_qcm = cur.getInt(cur.getColumnIndex("id_QCM"));
            cur.close();
            db.close();
            return id_qcm;
        } else {
            // Retournez une valeur par défaut ou une valeur indiquant qu'aucun enregistrement n'a été trouvé
            return -1;
        }
    }

    //-----------------------------------------------------------
    public void InsererQST(String qst, String A, String B, String C, String D, String corr, int id_QCM) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Question", qst);
        values.put("A", A);
        values.put("B", B);
        values.put("C", C);
        values.put("D", D);
        values.put("Correction", corr);
        values.put("id_QCM", id_QCM);
        db.insert("QST", null, values);
        db.close();
    }

    public ArrayList<QST> getQSTs(int id_QCM){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM QST WHERE id_QCM = ? ";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(id_QCM)});

        // Initialisez à null par défaut
        ArrayList<QST> QSTs = new ArrayList<>();
        while (cur.moveToNext()) {
            String qst = cur.getString(cur.getColumnIndex("Question"));
            String a= cur.getString(cur.getColumnIndex("A"));
            String b= cur.getString(cur.getColumnIndex("B"));
            String c= cur.getString(cur.getColumnIndex("C"));
            String d= cur.getString(cur.getColumnIndex("D"));
            String corr = cur.getString(cur.getColumnIndex("Correction"));
            int id_QCM_forQsts = cur.getInt(cur.getColumnIndex("id_QCM"));
           QSTs.add(new QST(qst,a,b,c,d,corr,id_QCM_forQsts));
        }

        cur.close();
        db.close();
        return QSTs;
    }

    /*
    public void supprimerProfAvecQCM(int id_prof) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Supprimer le prof de la table "Prof"
        String profWhereClause = "id_prof=?";
        String[] profWhereArgs = {String.valueOf(id_prof)};
        db.delete("Prof", profWhereClause, profWhereArgs);

        // Supprimer tous les QCM associés de la table "QCM"
        String qcmWhereClause = "id_Prof=?";
        String[] qcmWhereArgs = {String.valueOf(id_prof)};
        db.delete("QCM", qcmWhereClause, qcmWhereArgs);

        // Fermez la base de données
        db.close();
    }*/




    public void SupprimerQSM(int id_QCM) {
        SQLiteDatabase db = this.getWritableDatabase();


        String tableQCM = "QCM";
        String whereQCM = "id_QCM = ?";
        String[] whereArgsQCM = { String.valueOf(id_QCM) };

        String tableQCM_Passer = "QCM_Passer";
        String tableQST ="QST";
        db.delete(tableQCM_Passer, whereQCM, whereArgsQCM);
        db.delete(tableQST ,whereQCM,whereArgsQCM);
        db.delete(tableQCM, whereQCM, whereArgsQCM);
        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Prof;");
        db.execSQL("DROP TABLE IF EXISTS QCM;");
        db.execSQL("DROP TABLE IF EXISTS QST;");
        db.execSQL("DROP TABLE IF EXISTS Etudiant;");
        db.execSQL("DROP TABLE IF EXISTS QCM_Passer;");
        onCreate(db); // Recrée toutes les tables après les avoir supprimées
        db.close();
    }


}
