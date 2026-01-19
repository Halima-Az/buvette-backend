package com.buvette.buvette_backend.services.client;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.buvette.buvette_backend.model.client.Produit;
import com.buvette.buvette_backend.repository.client.ProduitRepository;
@Service
public class ProduitService {
    private ProduitRepository repo;
    public ProduitService(ProduitRepository repo){
        this.repo=repo;
    }
      public List<Produit> all() {
        return repo.findAll();
    }
    public Produit add(@RequestBody Produit p){
        return repo.save(p);
    } 

}
