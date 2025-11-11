package com.buvette.buvette_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.Produit;

public interface ProduitRepository extends MongoRepository<Produit,String> {
    
}
