package com.example.qcm;

public class QCM {

    int id_QCM ,Nombre_qst , Temps_qst , id_Prof ;
    String Titre ;

    public QCM(int id_QCM,String Titre,int Nombre_qst,int Temps , int id_Prof){
        this.id_QCM=id_QCM ;
        this.Titre=Titre ;
        this.Nombre_qst=Nombre_qst ;
        this.Temps_qst=Temps ;
        this.id_Prof = id_Prof;
    }
}
