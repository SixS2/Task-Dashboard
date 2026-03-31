package com.dashboard.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aviso {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDate createdAt;
    private LocalDate expiresAt;

    public Aviso() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDate.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDate expiresAt) { this.expiresAt = expiresAt; }

    @JsonIgnore
    public boolean isExpired() {
        if (expiresAt == null) return false;
        return LocalDate.now().isAfter(expiresAt);
    }
}
