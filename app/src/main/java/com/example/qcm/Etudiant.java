package com.example.qcm;

public class Etudiant {
    int id_Etud ;
    String Nom_complet , Specialite , Password ;

    public Etudiant(int id , String nom , String spec , String pass){
        this.id_Etud=id ;
        this.Nom_complet= nom ;
        this.Specialite=spec;
        this.Password=pass ;
    }
}
