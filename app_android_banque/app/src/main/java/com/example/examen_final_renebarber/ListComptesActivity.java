package com.example.examen_final_renebarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListComptesActivity extends AppCompatActivity {
    private ListView listViewComptes;
    private CompteBD compteBD;

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
        setContentView(R.layout.layout_list);

        // Masque le titre de l'action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Initialise la liste des comptes
        listViewComptes = findViewById(R.id.idListeCompte);

        // Initialise la base de données des comptes
        compteBD = new CompteBD(this);

        // Récupère tous les comptes de la base de données
        compteBD.openForRead();
        final ArrayList<Compte> comptesList = compteBD.getAllComptes();
        compteBD.close();

        // Crée un adaptateur pour la liste des comptes
        final CompteAdapter adapter = new CompteAdapter(this, R.layout.item_list, comptesList);
        // Définit l'adaptateur pour la liste des comptes
        listViewComptes.setAdapter(adapter);

        // Configure un écouteur pour détecter le clic sur un élément de la liste des comptes
        listViewComptes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupère le compte sélectionné
                Compte selectedCompte = comptesList.get(position);
                // Crée une intention pour afficher les détails du compte sélectionné
                Intent intent = new Intent(ListComptesActivity.this, CompteDetailsActivity.class);
                // Ajoute le compte sélectionné en tant qu'extra pour l'intention
                intent.putExtra("selectedCompte", selectedCompte);
                // Démarre l'activité pour afficher les détails du compte
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListComptesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}