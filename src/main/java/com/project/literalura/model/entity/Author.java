package com.project.literalura.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Integer birthYear;
    private Integer deathYear;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }

    @Override
    public String toString() {
        String anos = "";
        if (birthYear != null || deathYear != null) {
            String b = birthYear != null ? String.valueOf(birthYear) : "?";
            String d = deathYear != null ? String.valueOf(deathYear) : "?";
            anos = " (" + b + "-" + d + ")";
        }
        return name + anos;
    }
}
