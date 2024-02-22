package com.example.examen_final_renebarber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CompteBD {
    private static final int VERSION = 1; // Version de la base de données
    private static final String NOM_BD = "Compte.db"; // Nom de la base de données
    private static final String TABLE_COMPTES = "table_comptes"; // Nom de la table des comptes

    // Colonnes de la table des comptes
    private static final String COL_NUMERO = "NUMERO";
    private static final String COL_TYPECOMPTE = "TYPECOMPTE";
    private static final String COL_SOLDE = "SOLDE";
    private static final String COL_DEVISE = "DEVISE";
    private static final String COL_IMAGE = "IMAGE";

    // Indices des colonnes
    private static final int NUM_COL_NUMERO = 0;
    private static final int NUM_COL_TYPECOMPTE = 1;
    private static final int NUM_COL_SOLDE = 2;
    private static final int NUM_COL_DEVISE = 3;
    private static final int NUM_COL_IMAGE = 4;

    private SQLiteDatabase db; // Base de données
    private CompteBaseSQLite comptes; // Classe d'aide à la gestion de la base de données

    // Constructeur
    public CompteBD(Context context) {
        comptes = new CompteBaseSQLite(context, NOM_BD, null, VERSION);
    }

    // Ouvrir la base de données en mode écriture
    public void openForWrite() {
        db = comptes.getWritableDatabase();
    }

    // Ouvrir la base de données en mode lecture
    public void openForRead() {
        db = comptes.getReadableDatabase();
    }

    // Fermer la base de données
    public void close() {
        db.close();
    }

    // Obtenir l'objet SQLiteDatabase
    public SQLiteDatabase getDB() {
        return db;
    }

    // Insérer un compte dans la base de données
    public long insertCompte(Compte compte) {
        ContentValues content = new ContentValues();
        content.put(COL_NUMERO, compte.getNumero());
        content.put(COL_TYPECOMPTE, compte.getTypeCompte());
        content.put(COL_SOLDE, compte.getSolde());
        content.put(COL_DEVISE, compte.getDevise());
        content.put(COL_IMAGE, compte.getImage());
        return db.insert(TABLE_COMPTES,  null, content);
    }

    // Mettre à jour un compte dans la base de données
    public int updateCompte(String numero, Compte compte) {
        ContentValues content = new ContentValues();
        content.put(COL_TYPECOMPTE, compte.getTypeCompte());
        content.put(COL_SOLDE, compte.getSolde());
        content.put(COL_DEVISE, compte.getDevise());
        content.put(COL_IMAGE, compte.getImage());
        return db.update(TABLE_COMPTES, content, COL_NUMERO + " = '" + numero + "'", null);
    }

    // Supprimer un compte de la base de données
    public int removeCompte(String numero) {
        return db.delete(TABLE_COMPTES, COL_NUMERO + " = ?" , new String[]{numero});
    }

    // Obtenir un compte à partir de son numéro
    public Compte getCompte(String numero) {
        Cursor c = db.query(TABLE_COMPTES, new String[]{COL_NUMERO, COL_TYPECOMPTE, COL_SOLDE, COL_DEVISE, COL_IMAGE},
                COL_NUMERO + " like \"" + numero + "\"", null, null, null, COL_NUMERO);
        return cursorToCompteTransaction(c);
    }

    // Convertir un curseur en objet Compte
    public Compte cursorToCompte(Cursor c) {
        if(c == null || c.getCount() == 0) {
            return null;
        }
        Compte compte = new Compte();
        compte.setNumero(c.getString(NUM_COL_NUMERO));
        compte.setTypeCompte(c.getString(NUM_COL_TYPECOMPTE));
        compte.setSolde(c.getFloat(NUM_COL_SOLDE));
        compte.setDevise(c.getString(NUM_COL_DEVISE));
        compte.setImage(c.getString(NUM_COL_IMAGE));
        return compte;
    }

    // Convertir un curseur en objet Compte pour la transaction
    public Compte cursorToCompteTransaction(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            String numero = cursor.getString(cursor.getColumnIndexOrThrow("NUMERO")); // Utiliser le bon nom de colonne
            String typeCompte = cursor.getString(cursor.getColumnIndexOrThrow("TYPECOMPTE"));
            float solde = cursor.getFloat(cursor.getColumnIndexOrThrow("SOLDE"));
            String devise = cursor.getString(cursor.getColumnIndexOrThrow("DEVISE"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"));

            return new Compte(numero, typeCompte, solde, devise, image);
        } else {
            return null;
        }
    }

    // Obtenir tous les comptes de la base de données
    public ArrayList<Compte> getAllComptes() {
        Cursor c = db.query(TABLE_COMPTES, new String[]{COL_NUMERO, COL_TYPECOMPTE, COL_SOLDE, COL_DEVISE, COL_IMAGE},
                null, null, null, null, COL_NUMERO);
        ArrayList<Compte> compteList = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {
                Compte compte = cursorToCompte(c);
                if (compte != null) {
                    compteList.add(compte);
                }
            } while (c.moveToNext());
            c.close();
        }
        return compteList;
    }
}