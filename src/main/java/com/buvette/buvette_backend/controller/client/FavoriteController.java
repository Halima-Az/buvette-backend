package com.buvette.buvette_backend.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.model.client.Favorite;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.services.client.FavoriteService;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/client/favorites")
@PreAuthorize("hasRole('CLIENT')")
public class FavoriteController {
    private FavoriteService service;


    public FavoriteController(FavoriteService service) {
        this.service = service;
    
    }

    
    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(Authentication auth,
            @RequestBody Favorite fav) {

        fav.setIdUser(auth.getName()); // récupère email depuis JWT
        service.addToFavorite(fav);
        return ResponseEntity.ok("item added");
    }
    @PostMapping("/delete")
    public ResponseEntity<?> delete(Authentication auth,@RequestBody Favorite fav){
        fav.setIdUser(auth.getName()); // récupère email depuis JWT
        service.deleteFavorite(fav);

        return ResponseEntity.ok("item deleted");
    }
    @GetMapping
    public ResponseEntity<List<MenuItem>>afficher(Authentication auth){
        String email=auth.getName();
        List<MenuItem> favs= service.getFavorites(email);
        return ResponseEntity.ok(favs);

    }

}
