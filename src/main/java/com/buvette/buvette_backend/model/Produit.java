package com.buvette.buvette_backend.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="produits")
public class Produit{
@Id
private String id;
private Double prix;
private String nom;
private String description;

public String getId(){
    return this.id;
}
public void setId(String id){
    this.id=id;
}
public Double getPrix() {
    return this.prix;
}

public void setPrix(Double prix) {
    this.prix = prix;
}


public String getNom() {
    return this.nom;
}

public void setNom(String nom) {
    this.nom = nom;
}

public String getDescription() {
    return this.description;
}

public void setDescription(String description) {
    this.description = description;
}

}

