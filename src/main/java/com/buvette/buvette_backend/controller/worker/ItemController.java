package com.buvette.buvette_backend.controller.worker;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.buvette.buvette_backend.enumAttribute.ItemCategory;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.services.worker.ItemService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/worker")
@PreAuthorize("hasRole('WORKER')")
public class ItemController {
    
    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public MenuItem addItem(@RequestParam("name") String name,
                        @RequestParam("price") double price,
                        @RequestParam("rating") double rating,
                        @RequestParam("itemCategory") String category,
                        @RequestParam("available") boolean available,
                        @RequestParam("image") MultipartFile imageFile)throws IOException {

        // Ici, tu peux enregistrer l'image dans un dossier local /img
        String imagePath = itemService.saveImage(imageFile);

        MenuItem item = new MenuItem();
        item.setName(name);
        item.setPrice(price);
        item.setRating(rating);
        item.setCategorie(ItemCategory.valueOf(category));
        item.setAvailability(available);
        item.setImage(imagePath);

        return itemService.saveItem(item);
    }
    
    @PutMapping("/menu-items/{id}/availability")
    public MenuItem updateAvailability(@PathVariable String id,
                                        @RequestParam boolean available ) 
    {
        return itemService.setAvailability(id, available);
    }

}
