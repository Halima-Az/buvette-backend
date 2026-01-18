package com.buvette.buvette_backend.controller;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.buvette.buvette_backend.model.Produit;
import com.buvette.buvette_backend.services.ProduitService;
@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProduitController {
 private final ProduitService service;

    public ProduitController(ProduitService service) {
        this.service = service;
    }

    @GetMapping
    public List<Produit>all(){
        return service.all();
    }


    @PostMapping
     public Produit add(@RequestBody Produit p){
        return service.add(p);
    }
}
