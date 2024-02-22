package com.example.examen_final_renebarber;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Export {
    // Génère une représentation XML des comptes
    public static String generateXml(List<Compte> comptes) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<comptes>\n");
        // Parcourt la liste des comptes
        for (Compte compte : comptes) {
            xmlBuilder.append("  <compte>\n");
            // Ajoute les balises pour chaque attribut du compte
            xmlBuilder.append("    <numero>").append(compte.getNumero()).append("</numero>\n");
            xmlBuilder.append("    <typeCompte>").append(compte.getTypeCompte()).append("</typeCompte>\n");
            xmlBuilder.append("    <solde>").append(compte.getSolde()).append("</solde>\n");
            xmlBuilder.append("    <devise>").append(compte.getDevise()).append("</devise>\n");
            xmlBuilder.append("    <image>").append(compte.getImage()).append("</image>\n");
            xmlBuilder.append("  </compte>\n");
        }
        xmlBuilder.append("</comptes>");
        return xmlBuilder.toString();
    }

    // Génère une représentation JSON des comptes
    public static String generateJson(List<Compte> comptes) {
        JSONArray jsonArray = new JSONArray();
        try {
            // Parcourt la liste des comptes
            for (Compte compte : comptes) {
                JSONObject jsonObject = new JSONObject();
                // Ajoute chaque attribut du compte en tant qu'objet JSON
                jsonObject.put("numero", compte.getNumero());
                jsonObject.put("typeCompte", compte.getTypeCompte());
                jsonObject.put("solde", compte.getSolde());
                jsonObject.put("devise", compte.getDevise());
                jsonObject.put("image", compte.getImage());
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    // Écrit les données dans un fichier
    public static void writeToFile(Context context, String data, String fileName) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            // Écrit les données dans le fichier
            fos.write(data.getBytes());
            // Affiche un message de succès dans les logs
            Log.i("writeToFile", "Successfully wrote to " + fileName);
        } catch (IOException e) {
            // Affiche un message d'erreur dans les logs en cas d'échec
            Log.e("writeToFile", "Error writing to file " + fileName, e);
        }
    }
}