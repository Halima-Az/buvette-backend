package com.buvette.buvette_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.Produit;

public interface ProduitRepository extends MongoRepository<Produit,String>{
    
}
