package com.example.examen_final_renebarber;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CompteAdapter extends ArrayAdapter<Compte>{
    private Context context;
    private int layoutItemListe;
    private Resources res;
    ArrayList<Compte> listeComptes;

    // Constructeur
    public CompteAdapter(Context context, int layoutItemListe, ArrayList<Compte> listeCompte) {
        super(context, layoutItemListe, listeCompte);
        this.context = context;
        this.layoutItemListe = layoutItemListe;
        res = context.getResources();
        this.listeComptes = listeCompte;
    }

    // Méthode appelée pour obtenir la vue de chaque élément de la liste
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Si la vue est null, la créer à partir du layout spécifié
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutItemListe, parent, false);
        }
        // Récupérer le compte à la position donnée
        Compte compte = listeComptes.get(position);
        // Si le compte n'est pas null
        if (compte != null) {
            // Récupérer les vues pour afficher les informations du compte
            TextView textViewNumero = view.findViewById(R.id.numero);
            TextView textViewSolde = view.findViewById(R.id.solde);
            TextView textViewDevise = view.findViewById(R.id.devise);
            ImageView imageViewCompte = view.findViewById(R.id.icon);
            // Définir les valeurs des vues avec les informations du compte
            textViewNumero.setText(compte.getNumero());
            String formattedSolde = String.format("%.2f", compte.getSolde());
            textViewSolde.setText(formattedSolde);
            textViewDevise.setText(compte.getDevise());
            // Récupérer l'identifiant de la ressource image à partir du nom de l'image du compte
            int imageResourceId = context.getResources().getIdentifier(compte.getImage(), "drawable", context.getPackageName());
            // Définir l'image du compte
            imageViewCompte.setImageResource(imageResourceId);
        }
        return view;
    }

    // Méthode pour obtenir le nombre d'éléments dans la liste
    @Override
    public int getCount() {
        return listeComptes.size();
    }
}