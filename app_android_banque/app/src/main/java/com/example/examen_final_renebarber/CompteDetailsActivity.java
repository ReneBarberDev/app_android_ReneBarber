package com.example.examen_final_renebarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CompteDetailsActivity extends AppCompatActivity {

    private CompteBD compteBD;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); // Infler le menu de l'action bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuHandler.handleMenuOption(item, this, compteBD) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compte_details_activity);

        // Masquer le titre de l'action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Initialiser la base de données de compte
        compteBD = new CompteBD(this);

        // Récupérer le compte sélectionné de l'intent
        Compte selectedCompte = (Compte) getIntent().getSerializableExtra("selectedCompte");

        // Initialiser les vues pour afficher les détails du compte
        TextView textViewNumero = findViewById(R.id.textViewNumero);
        TextView textViewSolde = findViewById(R.id.textViewSolde);
        TextView textViewDevise = findViewById(R.id.textViewDevise);
        ImageView imageViewCompte = findViewById(R.id.imageViewCompte);

        if (selectedCompte != null) {
            // Afficher les détails du compte
            textViewNumero.setText(selectedCompte.getNumero());
            textViewSolde.setText(String.format("%.2f", selectedCompte.getSolde()));
            textViewDevise.setText(selectedCompte.getDevise());

            // Charger l'image du compte à partir des ressources
            int imageResourceId = getResources().getIdentifier(selectedCompte.getImage(), "drawable", getPackageName());
            imageViewCompte.setImageResource(imageResourceId);
        }

        // Définir l'action du bouton pour retourner à l'écran principal
        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompteDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}