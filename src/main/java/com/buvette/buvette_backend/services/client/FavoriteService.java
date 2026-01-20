package com.buvette.buvette_backend.services.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.client.Favorite;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.repository.client.FavoriteRepository;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository repo;
      @Autowired
    private MenuItemRepository repoMenu;
    public void addToFavorite(Favorite fav){
        fav.setCreateAt(new Date());
        repo.save(fav);
    }
    public void deleteFavorite(Favorite fav){
        repo.deleteByIdUserAndItemId(fav.getIdUser(),fav.getItemId());
    }
    public List<MenuItem> getFavorites(String email){
        List<Favorite>fs= repo.findByIdUser(email);
        List<MenuItem> list =new ArrayList<>();
        for ( Favorite f : fs){
          repoMenu.findById(f.getItemId()).ifPresent(list::add); 
       
        }
        return list;

    }

}
