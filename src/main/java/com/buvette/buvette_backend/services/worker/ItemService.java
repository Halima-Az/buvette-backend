package com.buvette.buvette_backend.services.worker;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ItemService {

    private final MenuItemRepository itemRepository;
    private final String uploadDir = "src/main/resources/static/img"; // dossier de stockage

    public ItemService(MenuItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Sauvegarde l'image et retourne le chemin relatif
    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Vérifier que le dossier existe, sinon le créer
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Générer un nom unique
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);

        // Sauvegarder le fichier
        Files.write(filepath, file.getBytes());

        // Retourner chemin relatif utilisable dans le front
        return "img/" + filename;
    }

    // Sauvegarde l'item dans MongoDB
    public MenuItem saveItem(MenuItem item) {
        return itemRepository.save(item);
    }

    public MenuItem setAvailability(String id, boolean available) {
        MenuItem item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setAvailability(available);
        return itemRepository.save(item);
    }
}
