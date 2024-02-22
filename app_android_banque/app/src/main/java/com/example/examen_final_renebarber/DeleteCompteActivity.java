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

public class DeleteCompteActivity extends AppCompatActivity {
    private EditText editTextSearch;
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
        setContentView(R.layout.delete_compte_activity);

        // Configure l'action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Initialise les vues
        editTextSearch = findViewById(R.id.editTextSearch);

        // Initialise la base de données
        compteBD = new CompteBD(this);

        // Initialise les actions des boutons
        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteCompteActivity.this, MainActivity.class);
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

        findViewById(R.id.buttonFermer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fermerCompte();
                Intent intent = new Intent(DeleteCompteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.buttonAnnuler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteCompteActivity.this, MainActivity.class);
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

    // Ferme le compte sélectionné
    private void fermerCompte() {
        // Récupère le numéro de compte à fermer
        String numero = editTextSearch.getText().toString().trim().toUpperCase();

        if (numero.isEmpty()) {
            // Affiche un message si aucun numéro de compte n'est saisi
            Toast.makeText(DeleteCompteActivity.this, "Veuillez entrer le numéro de compte à fermer", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Supprime le compte de la base de données
            compteBD.openForWrite();
            int rowsDeleted = compteBD.removeCompte(numero);
            if (rowsDeleted > 0) {
                // Affiche un message de succès si le compte est supprimé avec succès
                Toast.makeText(DeleteCompteActivity.this, "Compte fermé avec succès", Toast.LENGTH_SHORT).show();
                // Redirige vers l'écran principal
                Intent intent = new Intent(DeleteCompteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Affiche un message si aucun compte correspondant n'est trouvé pour la suppression
                Toast.makeText(DeleteCompteActivity.this, "Échec de la fermeture du compte : compte non trouvé", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Affiche un message d'erreur en cas d'exception
            e.printStackTrace();
            Toast.makeText(DeleteCompteActivity.this, "Une erreur s'est produite lors de la fermeture du compte", Toast.LENGTH_SHORT).show();
        } finally {
            // Ferme la base de données
            compteBD.close();
        }
    }
}