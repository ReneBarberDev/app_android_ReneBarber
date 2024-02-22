package com.example.examen_final_renebarber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NewCompteActivity extends AppCompatActivity {
    private EditText editTextCompteSolde;
    private RadioGroup radioGroupTypeCompte, radioGroupDevise;
    private CompteBD compteBD;
    private int count = 5;
    private static final String PREF_NAME = "compte_prefs";
    private static final String COUNT_KEY = "count";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuHandler.handleMenuOption(item, this, compteBD) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_compte_activity);

        // Masquer le titre de l'action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        editTextCompteSolde = findViewById(R.id.edit_text_compte_solde);
        radioGroupTypeCompte = findViewById(R.id.radio_group_type_compte);
        radioGroupDevise = findViewById(R.id.radio_group_devise);

        // Initialiser la base de données des comptes
        compteBD = new CompteBD(this);

        // Récupérer la valeur du compteur à partir des préférences, si elle existe
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        count = prefs.getInt(COUNT_KEY, 5);

        findViewById(R.id.btn_save_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCompte();
            }
        });

        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewCompteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String generateCode() {
        count++; // Incrémenter le compteur
        count = Math.min(count, 9999); // Limiter le compteur à 9999

        // Enregistrer la nouvelle valeur du compteur dans les préférences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(COUNT_KEY, count).apply();

        // Formater le compteur pour qu'il ait toujours 4 chiffres
        String formattedCount = String.format("%04d", count);

        // Générer le code unique pour le compte
        return "C-" + formattedCount;
    }

    private void saveCompte() {
        String compteSolde = editTextCompteSolde.getText().toString().trim();

        if (compteSolde.isEmpty()) {
            // Afficher un message si le champ du solde est vide
            Toast.makeText(NewCompteActivity.this, "Please enter compte solde", Toast.LENGTH_SHORT).show();
            return;
        }

        // Formater le solde du compte
        String formattedSolde = String.format("%.2f", Float.parseFloat(compteSolde));

        // Récupérer le type de compte sélectionné
        RadioButton selectedTypeCompteRadioButton = findViewById(radioGroupTypeCompte.getCheckedRadioButtonId());
        String typeCompte = selectedTypeCompteRadioButton.getText().toString();

        // Récupérer la devise sélectionnée
        RadioButton selectedDeviseRadioButton = findViewById(radioGroupDevise.getCheckedRadioButtonId());
        String devise = selectedDeviseRadioButton.getText().toString();

        // Récupérer l'image selon le type de compte
        String image = "";
        switch (typeCompte) {
            case "Chèque":
                image = "drawable/depense";
                break;
            case "Épargne":
                image = "drawable/epargne";
                break;
            case "Investissement":
                image = "drawable/investissement";
                break;
            default:
                image = "drawable/default_image";
                break;
        }

        // Générer le code unique pour le compte
        String numero = generateCode();
        // Créer un nouvel objet Compte
        Compte newCompte = new Compte(numero, typeCompte, Float.parseFloat(formattedSolde), devise, image);

        // Insérer le nouveau compte dans la base de données
        compteBD.openForWrite();
        long result = compteBD.insertCompte(newCompte);
        compteBD.close();

        if (result != -1) {
            // Afficher un message si le compte est enregistré avec succès
            Toast.makeText(NewCompteActivity.this, "New Compte Saved", Toast.LENGTH_SHORT).show();
            // Retourner à l'activité principale
            Intent intent = new Intent(NewCompteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Afficher un message si l'enregistrement du compte a échoué
            Toast.makeText(NewCompteActivity.this, "Failed to save new compte", Toast.LENGTH_SHORT).show();
        }
    }
}