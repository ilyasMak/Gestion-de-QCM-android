package com.example.qcm;

public class QST {
    String Question , A,B,C,D,Correction ;
    int id_QSM ;
    public QST(String qst , String a , String b , String c , String d , String corr , int id_qsm){
        this.Question = qst ;
        this.A =a ;
        this.B = b ;
        this.C = c ;
        this.D = d ;
        this.Correction=corr ;
        this.id_QSM=id_qsm;

    }
}
