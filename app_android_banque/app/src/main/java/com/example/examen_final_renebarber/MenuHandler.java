package com.example.examen_final_renebarber;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MenuHandler {

    // Gère l'option de menu sélectionnée
    public static boolean handleMenuOption(MenuItem item, AppCompatActivity activity, CompteBD compteBD) {
        // Obtient l'identifiant de l'élément de menu sélectionné
        int id = item.getItemId();

        // Vérifie quel élément de menu a été sélectionné et effectue une action appropriée
        if (id == R.id.add) {
            // Redirige vers l'activité pour ajouter un nouveau compte
            activity.startActivity(new Intent(activity, NewCompteActivity.class));
            return true;
        } else if (id == R.id.home) {
            // Redirige vers l'activité principale
            activity.startActivity(new Intent(activity, MainActivity.class));
            return true;
        } else if (id == R.id.list) {
            // Redirige vers l'activité pour afficher la liste des comptes
            activity.startActivity(new Intent(activity, ListComptesActivity.class));
            return true;
        } else if (id == R.id.credit) {
            // Redirige vers l'activité pour effectuer une opération de crédit sur le compte
            activity.startActivity(new Intent(activity, CreditCompteActivity.class));
            return true;
        } else if (id == R.id.debit) {
            // Redirige vers l'activité pour effectuer une opération de débit sur le compte
            activity.startActivity(new Intent(activity, DebitCompteActivity.class));
            return true;
        } else if (id == R.id.close) {
            // Redirige vers l'activité pour fermer un compte
            activity.startActivity(new Intent(activity, DeleteCompteActivity.class));
            return true;
        } else if (id == R.id.xml) {
            // Exporte les comptes au format XML
            compteBD.openForRead();
            List<Compte> comptesXml = compteBD.getAllComptes();
            compteBD.close();
            String xmlContent = Export.generateXml(comptesXml);
            if(xmlContent != null) {
                Export.writeToFile(activity, xmlContent, "listeComptes.xml");
                Toast.makeText(activity, "Exportation XML réussie.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "XML vide.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.json) {
            // Exporte les comptes au format JSON
            compteBD.openForRead();
            List<Compte> comptesJson = compteBD.getAllComptes();
            compteBD.close();
            String jsonContent = Export.generateJson(comptesJson);
            if(jsonContent != null) {
                Export.writeToFile(activity, jsonContent, "listeComptes.json");
                Toast.makeText(activity, "Exportation JSON réussie.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "JSON vide.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}