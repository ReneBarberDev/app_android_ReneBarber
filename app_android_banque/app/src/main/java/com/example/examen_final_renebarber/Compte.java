package com.example.examen_final_renebarber;

import java.io.Serializable;

public class Compte implements Serializable {
    private String numero;
    private String typeCompte;
    private float solde;
    private String devise;
    private String image;

    // Constructeur par défaut
    public Compte() {}

    // Constructeur avec paramètres
    public Compte(String numero, String typeCompte, float solde, String devise, String image) {
        this.numero = numero;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.devise = devise;
        this.image = image;
    }

    // Méthodes getters et setters pour accéder aux attributs de la classe
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Méthode toString pour afficher les informations du compte
    @Override
    public String toString() {
        return "Compte{" +
                "Numero de compte=" + numero +
                ", type de compte='" + typeCompte + '\'' +
                ", solde=" + solde +
                ", devise='" + devise + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
