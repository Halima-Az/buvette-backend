package com.buvette.buvette_backend.repository.client;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.model.client.Produit;

public interface ProduitRepository extends MongoRepository<Produit,String>{
    
}
