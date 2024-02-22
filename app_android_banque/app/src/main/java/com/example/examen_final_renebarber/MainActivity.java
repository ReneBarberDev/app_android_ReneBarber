package com.example.examen_final_renebarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Instance de la base de données des comptes
    private CompteBD compteBD;

    @Override
    // Crée le menu de l'activité
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    // Gère les options du menu
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuHandler.handleMenuOption(item, this, compteBD) || super.onOptionsItemSelected(item);
    }

    @Override
    // Initialise l'activité
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false); // Masque le titre de l'action bar
        }

        // Initialise la base de données des comptes
        compteBD = new CompteBD(this);

        // Remplit la liste de comptes avec des données initiales
       // initializeList();

        // Configuration des listeners pour les boutons de l'interface utilisateur
        findViewById(R.id.btn_list_comptes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité pour afficher la liste des comptes
                startActivity(new Intent(MainActivity.this, ListComptesActivity.class));
            }
        });

        findViewById(R.id.btn_new_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité pour créer un nouveau compte
                startActivity(new Intent(MainActivity.this, NewCompteActivity.class));
            }
        });

        findViewById(R.id.btn_debit_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité pour effectuer une opération de débit sur un compte
                startActivity(new Intent(MainActivity.this, DebitCompteActivity.class));
            }
        });

        findViewById(R.id.btn_credit_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité pour effectuer une opération de crédit sur un compte
                startActivity(new Intent(MainActivity.this, CreditCompteActivity.class));
            }
        });

        findViewById(R.id.btn_delete_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité pour supprimer un compte
                startActivity(new Intent(MainActivity.this, DeleteCompteActivity.class));
            }
        });
    }

    // Initialise la liste de comptes avec des données initiales
    private void initializeList() {
        compteBD.openForWrite(); // Ouvre la base de données en mode écriture

        // Ajoute des comptes initiaux
        Compte compte = new Compte("C-0001", "cheque", 1000.00f, "CAD", "drawable/depense");
        compteBD.insertCompte(compte);

        compte = new Compte("C-0002", "epargne", 2000.00f, "USD", "drawable/epargne");
        compteBD.insertCompte(compte);

        compte = new Compte("C-0003", "investissement", 3000.00f, "CAD", "drawable/investissement");
        compteBD.insertCompte(compte);

        compte = new Compte("C-0004", "epargne", 4000.00f, "EUR", "drawable/epargne");
        compteBD.insertCompte(compte);

        compte = new Compte("C-0005", "cheque", 5000.00f, "CAD", "drawable/depense");
        compteBD.insertCompte(compte);

        compteBD.close(); // Ferme la base de données
    }
}