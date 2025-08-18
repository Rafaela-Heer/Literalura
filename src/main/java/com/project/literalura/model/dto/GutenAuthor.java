package com.project.literalura.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutenAuthor {
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    // getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }

    @Override
    public String toString() {
        return name + (birthYear != null ? " (" + birthYear + (deathYear != null ? "–" + deathYear : "") + ")" : "");
    }
}