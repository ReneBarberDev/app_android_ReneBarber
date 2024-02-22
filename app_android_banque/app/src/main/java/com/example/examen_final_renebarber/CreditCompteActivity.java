package com.example.examen_final_renebarber;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreditCompteActivity extends AppCompatActivity {

    private EditText editTextSearch, editTextMontant;
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
        setContentView(R.layout.credit_compte_activity);

        // Configure l'action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Initialise les vues
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextMontant = findViewById(R.id.editTextMontant);

        // Initialise la base de données
        compteBD = new CompteBD(this);

        // Initialise les actions des boutons
        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditCompteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton imageButtonSearch = findViewById(R.id.imageButtonSearch);
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCompte();
            }
        });

        findViewById(R.id.buttonCrediter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crediterCompte();
            }
        });

        findViewById(R.id.buttonAnnuler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditCompteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Recherche un compte dans la base de données
    private void searchCompte() {
        // Cache le clavier virtuel
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);

        // Récupère le numéro de compte entré par l'utilisateur
        String numero = editTextSearch.getText().toString().trim();

        if (TextUtils.isEmpty(numero)) {
            // Affiche un message si le champ de recherche est vide
            Toast.makeText(this, "Entrer un numéro de compte à rechercher", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recherche le compte dans la base de données
        compteBD.openForRead();
        Compte compte = compteBD.getCompte(numero);
        compteBD.close();

        if (compte != null) {
            // Affiche les détails du compte trouvé
            populateUI(compte);
        } else {
            // Affiche un message si aucun compte correspondant n'est trouvé
            Toast.makeText(this, "Compte non trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    // Affiche les détails du compte dans l'interface utilisateur
    private void populateUI(Compte compte) {
        TextView textViewNumero = findViewById(R.id.textViewNumero);
        TextView textViewSolde = findViewById(R.id.textViewSolde);
        TextView textViewDevise = findViewById(R.id.textViewDevise);
        ImageView imageViewCompte = findViewById(R.id.imageViewCompte);

        // Affiche les informations du compte dans les vues correspondantes
        textViewNumero.setText(compte.getNumero());
        textViewSolde.setText(String.format("%.2f", compte.getSolde()));
        textViewDevise.setText(compte.getDevise());

        // Charge l'image du compte à partir des ressources
        int imageResourceId = getResources().getIdentifier(compte.getImage(), "drawable", getPackageName());
        imageViewCompte.setImageResource(imageResourceId);
    }

    // Crédite le compte avec le montant saisi
    private void crediterCompte() {
        String montantStr = editTextMontant.getText().toString().trim();

        if (TextUtils.isEmpty(montantStr)) {
            // Affiche un message si aucun montant n'est saisi
            Toast.makeText(this, "Veuillez entrer un montant à créditer", Toast.LENGTH_SHORT).show();
            return;
        }

        float montant = Float.parseFloat(montantStr);

        String numero = editTextSearch.getText().toString().trim().toUpperCase();

        // Ouvre la base de données en mode écriture
        compteBD.openForWrite();
        // Récupère le compte à créditer
        Compte compte = compteBD.getCompte(numero);
        if (compte != null) {
            // Calcule le nouveau solde après le crédit
            float solde = compte.getSolde();
            float newSolde = solde + montant;
            compte.setSolde(newSolde);
            // Met à jour le compte dans la base de données
            int rowsAffected = compteBD.updateCompte(numero, compte);
            if (rowsAffected > 0) {
                // Affiche le nouveau solde dans l'interface utilisateur
                TextView textViewSolde = findViewById(R.id.textViewSolde);
                textViewSolde.setText(String.format("%.2f", newSolde));
                editTextMontant.setText("");
                // Affiche un message de succès
                Toast.makeText(this, "Montant crédité avec succès", Toast.LENGTH_SHORT).show();
            } else {
                // Affiche un message d'erreur en cas de problème lors de la mise à jour du compte
                Toast.makeText(this, "Erreur lors de la mise à jour du compte", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Affiche un message si le compte n'est pas trouvé dans la base de données
            Toast.makeText(this, "Compte non trouvé", Toast.LENGTH_SHORT).show();
        }
        // Ferme la base de données
        compteBD.close();
    }
}