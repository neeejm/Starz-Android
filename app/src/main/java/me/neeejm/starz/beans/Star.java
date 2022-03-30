package me.neeejm.starz.beans;

public class Star {
    private  int id;
    private String nom;
    private String prenom;
    private String ville;
    private boolean gender;
    private String imageURL;

    public Star(int id, String nom, String prenom, String ville, boolean gender, String imageURL) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.gender = gender;
        this.imageURL = imageURL;
    }

    public Star(String nom, String prenom, String ville, boolean gender, String imageURL) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.gender = gender;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
