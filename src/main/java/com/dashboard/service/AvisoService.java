package com.dashboard.service;

import com.dashboard.dto.Aviso;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvisoService {

    private final ObjectMapper mapper;
    private final File storagePath = new File("data");
    private final File dataFile = new File("data/avisos.json");
    private final String IMAGES_DIR = "data/images/";
    private final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private List<Aviso> avisos = new ArrayList<>();

    public AvisoService() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule()); // Support LocalDate
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    public void init() {
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }
        File imagesPath = new File(IMAGES_DIR);
        if (!imagesPath.exists()) {
            imagesPath.mkdirs();
        }
        if (dataFile.exists()) {
            try {
                avisos = mapper.readValue(dataFile, new TypeReference<List<Aviso>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar avisos: " + e.getMessage());
            }
        }
    }

    public synchronized void saveToFile() {
        try {
            mapper.writeValue(dataFile, avisos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar avisos: " + e.getMessage());
        }
    }

    public synchronized List<Aviso> getAllAvisos() {
        // Retorna todos para o painel de admin
        return new ArrayList<>(avisos);
    }

    public synchronized List<Aviso> getActiveAvisos() {
        // Retorna apenas os não expirados para os alunos
        return avisos.stream()
                .filter(a -> !a.isExpired())
                .collect(Collectors.toList());
    }

    private String sanitize(String html) {
        if (html == null) return null;
        // Basic XSS protection: strip HTML tags
        return html.replaceAll("<[^>]*>", "").trim();
    }

    public synchronized void addAviso(Aviso aviso, MultipartFile image) {
        // Sanitize text inputs to prevent XSS
        aviso.setTitle(sanitize(aviso.getTitle()));
        aviso.setContent(sanitize(aviso.getContent()));

        if (image != null && !image.isEmpty()) {
            try {
                String originalFilename = image.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                // Generate a safe unique filename to prevent path traversal
                String safeFilename = java.util.UUID.randomUUID().toString() + extension.toLowerCase();
                
                if (ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                    Path imagePath = Paths.get(IMAGES_DIR + safeFilename);
                    Files.copy(image.getInputStream(), imagePath);
                    aviso.setImageUrl("/images/" + safeFilename);
                } else {
                    System.err.println("Extensão de imagem não permitida: " + extension);
                }
            } catch (IOException e) {
                System.err.println("Erro ao salvar imagem: " + e.getMessage());
            }
        }
        avisos.add(aviso);
        saveToFile();
    }

    public synchronized void addAviso(Aviso aviso) {
        addAviso(aviso, null);
    }

    public synchronized void removeAviso(String id) {
        Aviso toRemove = avisos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (toRemove != null) {
            String imgUrl = toRemove.getImageUrl();
            if (imgUrl != null && imgUrl.startsWith("/images/")) {
                String fileName = imgUrl.substring("/images/".length());
                Path filePath = Paths.get(IMAGES_DIR + fileName);
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Erro ao deletar arquivo de imagem: " + e.getMessage());
                }
            }
            avisos.remove(toRemove);
            saveToFile();
        }
    }
}
