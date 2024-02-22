package com.example.examen_final_renebarber;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompteBaseSQLite extends SQLiteOpenHelper {
    // Définition des noms des colonnes de la table des comptes
    private static final String TABLE_COMPTES = "table_comptes";
    private static final String COL_NUMERO = "NUMERO";
    private static final String COL_TYPECOMPTE = "TYPECOMPTE";
    private static final String COL_SOLDE = "SOLDE";
    private static final String COL_DEVISE = "DEVISE";
    private static final String COL_IMAGE = "IMAGE";

    // Requête SQL pour créer la table des comptes
    private static final String CREATE_DB = "CREATE TABLE " +
            TABLE_COMPTES + "(" + COL_NUMERO + " TEXT(6) PRIMARY KEY NOT NULL, " +
            COL_TYPECOMPTE + " TEXT(14) NOT NULL, " +
            COL_SOLDE + " REAL NOT NULL, " +
            COL_DEVISE + " TEXT(3) NOT NULL, " +
            COL_IMAGE + " TEXT(50) NOT NULL);";

    // Constructeur
    public CompteBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Méthode appelée lors de la création de la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Exécute la requête de création de la table des comptes
        db.execSQL(CREATE_DB);
    }

    // Méthode appelée lors de la mise à jour de la base de données
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime la table des comptes existante si elle existe déjà
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTES);
        // Recrée la table des comptes
        onCreate(db);
    }
}