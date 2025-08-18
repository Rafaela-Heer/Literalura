package com.project.literalura.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutenBook {
    private int id;
    private String title;
    private List<GutenAuthor> authors;
    private List<String> languages;


    @JsonAlias({"download_count"})
    private Integer downloadCount;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<GutenAuthor> getAuthors() { return authors; }
    public void setAuthors(List<GutenAuthor> authors) { this.authors = authors; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    @Override
    public String toString() {
        String firstAuthor = (authors != null && !authors.isEmpty()) ? authors.get(0).getName() : "Autor desconhecido";
        return "[" + id + "] " + title + " | Autor: " + firstAuthor +
                " | Idiomas: " + languages + " | Downloads: " + downloadCount;
    }
}